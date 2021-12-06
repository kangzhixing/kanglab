package com.kang.codetool.controller.tool;

import com.kang.codetool.aop.annotation.ViewPage;
import com.kang.codetool.common.RestResponse;
import com.kang.codetool.service.ApiDocService;
import com.kang.codetool.vo.request.Dependency;
import com.kang.codetool.vo.request.converter.ApiListRequestConverter;
import com.kang.codetool.vo.response.ApiInfo;
import com.kang.lab.utils.FileUtil;
import com.kang.lab.utils.UUIDUtil;
import com.kang.lab.utils.XmlUtil;
import com.kang.lab.utils.jar.ApiListRequest;
import com.kang.lab.utils.jar.JarDownloadParams;
import com.kang.lab.utils.jar.JarUtil;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.DocumentException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

@RequestMapping("api")
@RestController
@Slf4j
public class ApiController {

    @ViewPage(description = "RPC接口定义")
    @RequestMapping("view")
    public ModelAndView view() {
        return new ModelAndView("tool/api");
    }

    @PostMapping("list")
    public RestResponse<List<ApiInfo>> list(ApiListRequest req) {
        String target = "./repo/" + UUIDUtil.newUuid();
        List<JarDownloadParams> jarDownloadParamsList = ApiListRequestConverter.toJarDownloadParamsList(req, target);
        CopyOnWriteArraySet<ApiInfo> result = new CopyOnWriteArraySet();
        try {
            jarDownloadParamsList.parallelStream().forEach(jarDownloadParams -> {
                try {
                    long startTime = System.currentTimeMillis();
                    log.info("{}开始下载", jarDownloadParams.getArtifactId());
                    List<File> fileList = JarUtil.downLoad(jarDownloadParams);
                    log.info("{}下载完成, cost: {}s", jarDownloadParams.getArtifactId(), (System.currentTimeMillis() - startTime) / 1000);
                    result.addAll(ApiDocService.getInterfaceList(fileList));
                } catch (Exception e) {
                    log.error("{}加载失败: {}", jarDownloadParams.getArtifactId(), e.getMessage());
                }
            });
        } finally {
            FileUtil.delete(new File(target));
        }
        return RestResponse.success(result.stream().collect(Collectors.toList()));
    }

}
