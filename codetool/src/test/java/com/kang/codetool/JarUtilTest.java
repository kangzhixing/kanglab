package com.kang.codetool;

import com.alibaba.fastjson.JSON;
import com.kang.codetool.common.RestResponse;
import com.kang.codetool.controller.tool.ApiController;
import com.kang.codetool.service.ApiDocService;
import com.kang.codetool.vo.request.ApiListRequest;
import com.kang.codetool.vo.response.ApiInfo;
import com.kang.lab.utils.UUIDUtil;
import com.kang.lab.utils.jar.JarDownloadParams;
import com.kang.lab.utils.jar.JarUtil;
import junit.framework.TestCase;
import lombok.SneakyThrows;
import org.junit.Test;

import java.util.List;

public class JarUtilTest extends TestCase {

    @SneakyThrows
    @Test
    public void testDownload() {
        JarUtil.downLoad(new JarDownloadParams("com.jd.jr.open", "open-lab", "1.3.8",
                "http://nexus.cbpmgt.com/nexus/content/groups/public/", null,
                "snapshot", "4AC8EMK#FrykJfG7"));
    }

    @Test
    public void testApi() {
        List<ApiInfo> snapshot = ApiDocService.downloadJar(new JarDownloadParams("com.jdt.tradecenter",
                        "trade-center-api",
                        "1.11.1-SNAPSHOT",
                        "http://nexus.cbpmgt.com/nexus/content/groups/public/", "./target/" + UUIDUtil.newUuid(),
                        "snapshot", "4AC8EMK#FrykJfG7"),
                (list) -> {
                    try {
                        return ApiDocService.getInterfaceList(list);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }
                , true);
        System.out.println(JSON.toJSONString(snapshot));
    }

    @Test
    public void testCtrl() {
        String xml2 = "<dependency>\n" +
                "            <groupId>com.jdt.tradecenter</groupId>\n" +
                "            <artifactId>trade-center-api</artifactId>\n" +
                "            <version>1.11.1-SNAPSHOT</version>\n" +
                "        </dependency>";
        String xml = "<dependency>\n" +
                "                <artifactId>fastjson</artifactId>\n" +
                "                <groupId>com.alibaba</groupId>\n" +
                "                <version>1.2.73</version>\n" +
                "            </dependency>";
        long start = System.currentTimeMillis();
        ApiController c = new ApiController();
        ApiListRequest req = ApiListRequest.builder()
                .dependencies(xml)
                .repository("https://repo1.maven.org/maven2/")
//                .repository("http://nexus.cbpmgt.com/nexus/content/groups/public/")
//                .username("snapshot")
//                .password("4AC8EMK#FrykJfG7")
                .build();
        RestResponse<List<ApiInfo>> list = null;
        try {
            list = c.list(req);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(JSON.toJSONString(list));
        System.out.println(System.currentTimeMillis() - start);
    }


}