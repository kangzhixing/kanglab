package com.kang.codetool.vo.request.converter;

import com.kang.codetool.vo.request.JarInfo;
import com.kang.lab.utils.UUIDUtil;
import com.kang.lab.utils.jar.ApiListRequest;
import com.kang.lab.utils.jar.JarDownloadParams;

import java.util.ArrayList;
import java.util.List;

public class ApiListRequestConverter {

    public static List<JarDownloadParams> toJarDownloadParamsList(ApiListRequest request, String target) {
        List<JarDownloadParams> result = new ArrayList(request.getJarInfoList().size());
        for (JarInfo jarInfo : request.getJarInfoList()) {
            result.add(JarDownloadParams.builder()
                    .artifactId(jarInfo.getArtifactId())
                    .groupId(jarInfo.getGroupId())
                    .version(jarInfo.getVersion())
                    .repository(request.getRepository())
                    .username(request.getUsername())
                    .password(request.getPassword())
                    .target(target)
                    .build());
        }
        return result;
    }
}
