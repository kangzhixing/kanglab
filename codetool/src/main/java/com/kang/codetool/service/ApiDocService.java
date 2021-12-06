package com.kang.codetool.service;

import com.kang.codetool.vo.response.ApiInfo;
import com.kang.codetool.vo.response.MethodInfo;
import com.kang.lab.utils.FileUtil;
import com.kang.lab.utils.UUIDUtil;
import com.kang.lab.utils.jar.JarDownloadParams;
import com.kang.lab.utils.jar.JarUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

import static com.kang.lab.utils.jar.JarUtil.DEFAULT_TARGET;

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
                        result.put(clazz.getName(), getApiInfo(clazz));
                    }
                } catch (NoClassDefFoundError | ClassNotFoundException ex) {
                    System.err.println("couldn't find " + className);
                }
            }
        }
        return result.values().stream().collect(Collectors.toList());
    }

    private static ApiInfo getApiInfo(Class clazz) {
        ApiInfo apiInfo = ApiInfo.builder().apiName(clazz.getName()).apiSimpleName(clazz.getSimpleName()).build();
        List<MethodInfo> methodInfoList = new ArrayList<>();

        for (Method method : clazz.getMethods()) {
            List<String> parameterTypeList = new ArrayList<>();
            for (Type genericParameterType : method.getGenericParameterTypes()) {
                parameterTypeList.add(genericParameterType.getTypeName());
            }
            MethodInfo methodInfo = MethodInfo.builder()
                    .methodName(method.getName())
                    .returnType(method.getGenericReturnType().getTypeName())
                    .parameterTypeList(parameterTypeList)
                    .build();
            methodInfoList.add(methodInfo);
        }
        apiInfo.setMethodInfoList(methodInfoList);
        return apiInfo;
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
