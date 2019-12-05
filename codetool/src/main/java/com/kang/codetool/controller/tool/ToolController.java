package com.kang.codetool.controller.tool;

import com.kang.codetool.aop.anntion.ViewPage;
import com.kang.codetool.common.KlRequest;
import com.kang.codetool.util.RedisLockUtil;
import com.kang.framework.HttpClientUtil;
import com.kang.framework.net.KlPing;
import com.kang.framework.net.KlPingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("tool")
public class ToolController {

    @Autowired
    private RedisLockUtil redisLock;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @ViewPage(description = "RGB颜色值")
    @RequestMapping("color")
    public ModelAndView color() {
        return new ModelAndView("tool/color");
    }

    @ViewPage(description = "MD5编码")
    @RequestMapping("md5")
    public ModelAndView md5() {
        return new ModelAndView("tool/md5");
    }

    @ViewPage(description = "GUID字符串")
    @RequestMapping("guid")
    public ModelAndView guid() {
        return new ModelAndView("tool/guid");
    }

    @ViewPage(description = "字符编码")
    @RequestMapping("encode")
    public ModelAndView encode() {
        return new ModelAndView("tool/encode");
    }

    @ViewPage(description = "时间戳转换")
    @RequestMapping("timestamp")
    public ModelAndView timestamp() {
        return new ModelAndView("tool/timestamp");
    }

    @ViewPage(description = "并发测试")
    @RequestMapping("concurrency")
    public ModelAndView concurrency() {
        return new ModelAndView("tool/concurrency");
    }

    @ViewPage(description = "Ping监测")
    @RequestMapping("ping")
    public ModelAndView ping() {
        return new ModelAndView("tool/ping");
    }

    @RequestMapping("encodeString")
    @ResponseBody
    public KlRequest encodeString(String str, String encoding) {
        KlRequest result = new KlRequest();
        try {
            result.setBody(URLEncoder.encode(str, encoding));
            return result;

        } catch (Exception ex) {
            result.setCode(0);
            result.setMsg(ex.getMessage());
            return result;
        }
    }

    @RequestMapping("decodeString")
    @ResponseBody
    public KlRequest decodeString(String str, String encoding) {
        KlRequest result = new KlRequest();
        try {
            result.setBody(URLDecoder.decode(str, encoding));
            return result;

        } catch (Exception ex) {
            result.setCode(0);
            result.setMsg(ex.getMessage());
            return result;
        }
    }

    @RequestMapping("pingIp")
    @ResponseBody
    public KlRequest pingIp(String ip, Integer times) {
        KlRequest result = new KlRequest();
        try {
            KlPingResult pingResult = KlPing.get(ip, times);
            result.setBody(pingResult);
            return result;

        } catch (Exception ex) {
            result.setCode(0);
            result.setMsg(ex.getMessage());
            return result;
        }
    }

    @RequestMapping("get")
    public KlRequest get(String url, Integer count, Boolean method, String postData) throws InterruptedException {
        KlRequest result = new KlRequest();
        StopWatch sw = new StopWatch();
        sw.start();
        String cacheKey = "alreadyPushConcurrency";
        if (!redisLock.pollingTryLock(cacheKey, "1", 10)) {
            result.setCode(0);
            result.setMsg("正在发送请求，请稍后再试");
        }
        String postDataDecode = URLDecoder.decode(postData);
        StringBuffer httpResult = new StringBuffer();
        String finalUrl = URLDecoder.decode(url);
        CountDownLatch cdl = new CountDownLatch(count);
        for (int i = 0; i < count; i++) {
            Executors.newFixedThreadPool(1000).execute(() -> {
                String httpResponse = method ? HttpClientUtil.doGet(finalUrl) : HttpClientUtil.doPost(finalUrl, postDataDecode);
                httpResult.append(httpResponse + "\r\n");
                cdl.countDown();
            });
        }
        cdl.await();
        redisTemplate.delete(cacheKey);
        result.setCode(1);
        sw.stop();
        result.setBody(httpResult + "\n\n共用时" + sw.getTotalTimeSeconds() + "秒");
        return result;
    }
}
