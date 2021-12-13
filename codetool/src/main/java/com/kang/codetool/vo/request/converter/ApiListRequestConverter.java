package com.kang.codetool.vo.request.converter;

import com.kang.codetool.vo.request.ApiListRequest;
import com.kang.codetool.vo.request.Dependency;
import com.kang.lab.utils.XmlUtil;
import com.kang.lab.utils.exception.BizException;
import com.kang.lab.utils.jar.JarDownloadParams;
import org.dom4j.DocumentException;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ApiListRequestConverter {

    public static List<JarDownloadParams> toJarDownloadParamsList(ApiListRequest request, String target) {
        List<Dependency> dependencies = parseXmlToEntity(URLDecoder.decode(request.getDependencies()));
        List<JarDownloadParams> result = new ArrayList(dependencies.size());
        for (Dependency jarInfo : dependencies) {
            result.add(JarDownloadParams.builder()
                    .artifactId(jarInfo.getArtifactId())
                    .groupId(jarInfo.getGroupId())
                    .version(jarInfo.getVersion())
                    .repository(URLDecoder.decode(request.getRepository()))
                    .username(request.getUsername())
                    .password(request.getPassword())
                    .target(target)
                    .build());
        }
        return result;
    }

    private static List<Dependency> parseXmlToEntity(String xml) {
        try {
            List<Dependency> result = new ArrayList<>();
            Map<String, Object> stringStringMap = XmlUtil.xml2Map("<root>" + xml + "</root>");
            for (Object root : (List) stringStringMap.get("root")) {
                Map<String, String> dependency = (Map) root;
                result.add(Dependency.builder()
                        .artifactId(dependency.get("artifactId"))
                        .groupId(dependency.get("groupId"))
                        .version(dependency.get("version"))
                        .build());
            }
            return result;
        } catch (DocumentException e) {
            throw new BizException("依赖信息格式有误");
        }
    }
}
