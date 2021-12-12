package com.kang.codetool.service;

import com.google.common.collect.Lists;
import com.kang.codetool.util.ClassUtil;
import com.kang.codetool.vo.response.ApiInfo;
import com.kang.codetool.vo.response.MethodInfo;
import com.kang.codetool.vo.response.ParameterInfo;
import com.kang.codetool.vo.response.ParameterTypeEnum;
import com.kang.lab.utils.FileUtil;
import com.kang.lab.utils.StringUtil;
import com.kang.lab.utils.UUIDUtil;
import com.kang.lab.utils.jar.JarDownloadParams;
import com.kang.lab.utils.jar.JarUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.function.Function;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

@Slf4j
public class ApiDocService {

    public static List<ApiInfo> getInterfaceList(List<File> fileList) throws IOException {
        if (CollectionUtils.isEmpty(fileList)) {
            return new ArrayList();
        }
        File jar = fileList.stream().findFirst().get();
        URL[] jarFileURLArr = new URL[fileList.size()];
        for (int i = 0; i < fileList.size(); i++) {
            jarFileURLArr[i] = fileList.get(i).toURI().toURL();
        }
        Map<String, ApiInfo> result = new HashMap<>();
        try (JarFile jarFile = new JarFile(jar.getAbsolutePath()); URLClassLoader urlClassLoader = new URLClassLoader(jarFileURLArr, Thread.currentThread().getContextClassLoader())) {
            Enumeration<JarEntry> jarEntries = jarFile.entries();
            while (jarEntries.hasMoreElements()) {
                JarEntry jarEntry = jarEntries.nextElement();
                if (jarEntry.isDirectory() || !jarEntry.getName().endsWith(".class")) {
                    continue;
                }
                String className = jarEntry.getName().substring(0, jarEntry.getName().length() - 6).replace("/", ".");
                try {
                    Class clazz = urlClassLoader.loadClass(className);
                    if (clazz.isInterface()) {
                        result.put(clazz.getName(), getApiInfo(clazz, urlClassLoader));
                    }
                } catch (NoClassDefFoundError | ClassNotFoundException ex) {
                    log.warn("couldn't find " + className, ex);
                }
            }
        }
        return result.values().stream().collect(Collectors.toList());
    }

    private static ApiInfo getApiInfo(Class clazz, URLClassLoader urlClassLoader) throws ClassNotFoundException {
        ApiInfo apiInfo = ApiInfo.builder().apiName(clazz.getName()).apiSimpleName(clazz.getSimpleName()).build();
        List<MethodInfo> methodInfoList = new ArrayList<>();

        for (Method method : clazz.getMethods()) {
            List<ParameterInfo> parameterTypeList = new ArrayList<>();
            String methodParameterSignature = "";
            for (Type genericParameterType : method.getGenericParameterTypes()) {
                parameterTypeList.add(getParameterInfoByType(genericParameterType, null, urlClassLoader));
                methodParameterSignature += getSimpleClassName(genericParameterType.getTypeName()) + " " + StringUtil.toLowerFirst(getSimpleClassName(genericParameterType.getTypeName())) + ", ";
            }
            MethodInfo methodInfo = MethodInfo.builder()
                    .methodName(method.getName())
                    .returnType(getParameterInfoByType(method.getGenericReturnType(), null, urlClassLoader))
                    .returnTypeSimpleName(method.getReturnType().getSimpleName())
                    .parameterTypeList(Lists.newArrayList(ParameterInfo.builder().build()))
                    .build();

            methodInfo.setMethodSignature(getReturnTypeSignature(method.getGenericReturnType().getTypeName())
                    + " " + method.getName() + "("
                    + (methodParameterSignature.length() > 0 ? methodParameterSignature.substring(0, methodParameterSignature.length() - 2) : "")
                    + ")");
            methodInfo.setParameterTypeList(parameterTypeList);
            methodInfoList.add(methodInfo);
        }
        apiInfo.setMethodInfoList(methodInfoList);
        return apiInfo;
    }
    private static String getReturnTypeSignature(String returnFullName) {
        if (StringUtils.isBlank(returnFullName)) {
            return "";
        }
        if (!returnFullName.contains("<")) {
            if (returnFullName.contains(".")) {
                return returnFullName.substring(returnFullName.lastIndexOf(".") + 1);
            }
            return returnFullName;
        }
        StringBuilder result = new StringBuilder();
        StringBuilder temp = new StringBuilder();
        for (char c : returnFullName.toCharArray()) {
            if (c == '<' || c == '>') {
                if (temp.length() > 0) {
                    result.append(temp.substring(temp.lastIndexOf(".") + 1));
                    temp = new StringBuilder();
                }
                result.append(c);
            } else {
                temp.append(c);
            }
        }
        return result.toString();
    }

    private static ParameterInfo getParameterInfoByType(Type type, String paramName, URLClassLoader urlClassLoader) throws ClassNotFoundException {
        ParameterInfo.ParameterInfoBuilder builder = ParameterInfo.builder();
        Class<?> rawType;
        if (type instanceof ParameterizedType) {
            rawType = ((ParameterizedTypeImpl) type).getRawType();
            // 获取泛型内部类型的参数列表
            List<ParameterInfo> rawTypeParams = new ArrayList<>();
            for (Type actualTypeArgument : ((ParameterizedType) type).getActualTypeArguments()) {
                ParameterInfo parameterByType = getParameterInfoByType(actualTypeArgument, "$item", urlClassLoader);
                rawTypeParams.add(parameterByType);
            }
            builder.childParamList(rawTypeParams);
        } else {
            // 获取原始类
            if (type instanceof Class) {
                rawType = (Class) type;
            } else {
                rawType = urlClassLoader.loadClass(type.getTypeName());
            }
            List<ParameterInfo> childParameters = new ArrayList<>();
            // 如果数组 则判断其原类型是否为数组
            // 如果不是数组 则直接判断该类型是否为数组
            if ((rawType.isArray() && !ClassUtil.isBasic(rawType.getComponentType())) || !ClassUtil.isBasic(rawType)) {
                for (Field declaredField : rawType.getDeclaredFields()) {
                    childParameters.add(getParameterInfoByType(declaredField.getGenericType(), declaredField.getName(), urlClassLoader));
                }
            }
            builder.childParamList(childParameters);
        }
        // 判断泛型是否是集合或者数据
        if (isSignByCollection(rawType)) {
            builder.type(ParameterTypeEnum.ARRAY.name());
        } else {
            builder.type(ParameterTypeEnum.OBJECT.name());
        }
        return builder.parameterName(paramName == null ? rawType.getSimpleName() : paramName)
                .className(rawType.isArray() ? rawType.getComponentType().getName() + "[]" : rawType.getName())
                .simpleClassName(rawType.isArray() ? rawType.getComponentType().getSimpleName() + "[]" : rawType.getSimpleName())
                .build();
    }

    private static boolean isSignByCollection(Class clazz) {
        return clazz.isArray() || clazz.isAssignableFrom(List.class) || clazz.isAssignableFrom(Set.class);
    }

    private static String getSimpleClassName(String className) {
        if (className.indexOf(".") < 0) {
            return className;
        }
        return className.substring(className.lastIndexOf(".") + 1);
    }

    public static <T> T downloadJar(JarDownloadParams params, Function<List<File>, T> function, boolean isDelete) {
        if (isDelete) {
            params.setTarget("./" + UUIDUtil.newUuid());
        }
        try {
            List<File> files = JarUtil.downLoad(params);
            return function.apply(files);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (isDelete) {
                File dic = new File(params.getTarget());
                if (!dic.exists()) {
                    return null;
                }
                FileUtil.delete(dic);
            }
        }
    }
}
