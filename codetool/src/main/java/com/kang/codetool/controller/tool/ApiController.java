package com.kang.codetool.controller.tool;

import com.kang.codetool.aop.annotation.ViewPage;
import com.kang.lab.utils.vo.RestResponse;
import com.kang.codetool.service.ApiDocService;
import com.kang.codetool.vo.request.ApiListRequest;
import com.kang.codetool.vo.request.converter.ApiListRequestConverter;
import com.kang.codetool.vo.response.ApiInfo;
import com.kang.lab.utils.ExecutorServiceUtil;
import com.kang.lab.utils.jar.JarDownloadParams;
import com.kang.lab.utils.jar.JarUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@RequestMapping("api")
@RestController
@Slf4j
public class ApiController {

    private static final ExecutorServiceUtil executors = ExecutorServiceUtil.getInstance(
            new ThreadPoolExecutor(10, 200,
                    0L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<>()));

    @ViewPage(description = "RPC接口定义")
    @RequestMapping("view")
    public ModelAndView view() {
        return new ModelAndView("tool/api");
    }

    @PostMapping("list")
    public RestResponse<List<ApiInfo>> list(ApiListRequest req) throws InterruptedException {
        String target = "./repo/";
        List<JarDownloadParams> jarDownloadParamsList = ApiListRequestConverter.toJarDownloadParamsList(req, target);
        CopyOnWriteArraySet<ApiInfo> result = new CopyOnWriteArraySet<>();
        CountDownLatch cdl = new CountDownLatch(jarDownloadParamsList.size());
        for (JarDownloadParams jarDownloadParams : jarDownloadParamsList) {
            executors.execute(() -> {
                try {
                    long startTime = System.currentTimeMillis();
                    log.info("{}开始下载", jarDownloadParams.getArtifactId());
                    List<File> fileList = JarUtil.downLoad(jarDownloadParams);
                    log.info("{}下载完成, cost: {}s", jarDownloadParams.getArtifactId(), (System.currentTimeMillis() - startTime) / 1000);
                    result.addAll(ApiDocService.getInterfaceList(fileList));
                } catch (Exception e) {
                    log.error("{}加载失败", jarDownloadParams.getArtifactId(), e);
                }
                cdl.countDown();
            });
        }
        cdl.await(1, TimeUnit.MINUTES);
        return RestResponse.success(result.stream().sorted(Comparator.comparing(ApiInfo::getApiName)).collect(Collectors.toList()));
    }

}
