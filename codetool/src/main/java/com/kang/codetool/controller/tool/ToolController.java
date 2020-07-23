package com.kang.codetool.controller.tool;

import com.kang.codetool.aop.anntion.ViewPage;
import com.kang.codetool.common.KlResponse;
import com.kang.framework.HttpClientUtil;
import com.kang.framework.net.KlPing;
import com.kang.framework.net.KlPingResult;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

@RestController
@RequestMapping("tool")
public class ToolController {

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

    @ViewPage(description = "Json格式化")
    @RequestMapping("json")
    public ModelAndView json() {
        return new ModelAndView("tool/json");
    }

    @RequestMapping("encodeString")
    @ResponseBody
    public KlResponse encodeString(String str, String encoding) {
        KlResponse result = new KlResponse();
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
    public KlResponse decodeString(String str, String encoding) {
        KlResponse result = new KlResponse();
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
    public KlResponse pingIp(String ip, Integer times) {
        KlResponse result = new KlResponse();
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
    public KlResponse get(String url, Integer count, Boolean method, String postData) throws InterruptedException {
        try {
            if (!LOCK.tryLock()) {
                return KlResponse.fail("正在发送请求，请稍后再试");
            }
            StopWatch sw = new StopWatch();
            sw.start();
            String postDataDecode = URLDecoder.decode(postData);
            String finalUrl = URLDecoder.decode(url);
            ThreadPoolExecutor executor = new ThreadPoolExecutor(86400, 86400,
                    0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
            for (int i = 0; i < count; i++) {
                executor.execute(() -> {
                    if (method) {
                        HttpClientUtil.doGet(finalUrl);
                    } else {
                        HttpClientUtil.doPost(finalUrl, postDataDecode);
                    }
                });
            }
            executor.shutdown();
            sw.stop();
            return KlResponse.success("共用时" + sw.getTotalTimeSeconds() + "秒");
        } finally {
            LOCK.unlock();
        }
    }

    private static final ReentrantLock LOCK = new ReentrantLock();
}
