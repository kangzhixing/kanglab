package com.kang.codetool.controller.tool;

import com.kang.codetool.common.RestResponse;
import com.kang.codetool.service.ApiDocService;
import com.kang.codetool.vo.request.converter.ApiListRequestConverter;
import com.kang.codetool.vo.response.ApiInfo;
import com.kang.lab.utils.FileUtil;
import com.kang.lab.utils.UUIDUtil;
import com.kang.lab.utils.jar.ApiListRequest;
import com.kang.lab.utils.jar.JarDownloadParams;
import com.kang.lab.utils.jar.JarUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

@RequestMapping("api")
@RestController
@Slf4j
public class ApiController {

    @PostMapping("list")
    public RestResponse<List<ApiInfo>> list(ApiListRequest req) {
        String target = "./" + UUIDUtil.newUuid();
        List<JarDownloadParams> jarDownloadParamsList = ApiListRequestConverter.toJarDownloadParamsList(req, target);
        CopyOnWriteArraySet<ApiInfo> result = new CopyOnWriteArraySet();
        jarDownloadParamsList.parallelStream().forEach(jarDownloadParams -> {
            try {
                List<File> fileList = JarUtil.downLoad(jarDownloadParams);
                result.addAll(ApiDocService.getInterfaceList(fileList));
            } catch (Exception e) {
                log.error("{}加载失败:{}", jarDownloadParams.getArtifactId(), e.getMessage());
            }
        });
        FileUtil.delete(new File(target));
        return RestResponse.success(result.stream().collect(Collectors.toList()));
    }

}
