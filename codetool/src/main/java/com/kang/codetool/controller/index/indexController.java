package com.kang.codetool.controller.index;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kang.codetool.common.Common;
import com.rabbitmq.client.*;
import com.sun.management.OperatingSystemMXBean;
import com.sun.management.ThreadMXBean;
import org.assertj.core.util.Maps;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@RestController
public class indexController {

    @RequestMapping("/")
    public ModelAndView testPage() {
        return new ModelAndView("main/index");
    }

    @RequestMapping("searchFunction")
    @ResponseBody
    public String searchFunction(String content) {
        Map<String, Method> pages = Common.getAllPageMethod();
        if (content.isEmpty() || "all".equals(content)) {
            return "/searchPage?isall=1";
        }
        List<String> list = new ArrayList();
        for (Map.Entry entry : pages.entrySet()) {
            if (entry.getKey().toString().toLowerCase().contains(content.toLowerCase())) {
                list.add(entry.getKey().toString());
            }
        }

        if (list.size() == 0) {
            return "empty";
        } else if (list.size() == 1) {
            return Common.getUrlByControllerMethod(pages.get(list.get(0)));
        } else {
            return "/searchPage?keyword=" + content;
        }
    }

    @RequestMapping("searchPage")
    public ModelAndView searchPage() {
        return new ModelAndView("/main/searchPage");
    }

    @RequestMapping("getPageByKeyword")
    @ResponseBody
    public String getPageByKeyword(String keyword, int isall) {
        Map<String, Method> pages = Common.getAllPageMethod();
        Map<String, Method> sortMap = new TreeMap<String, Method>(Comparator.naturalOrder());
        sortMap.putAll(pages);
        JSONArray result = new JSONArray();
        for (Map.Entry entry : sortMap.entrySet()) {
            if (isall == 1 || entry.getKey().toString().contains(URLDecoder.decode(keyword))) {
                JSONObject obj = new JSONObject();
                obj.put("text", entry.getKey());
                obj.put("url", Common.getUrlByControllerMethod((Method) entry.getValue()));
                result.add(obj);
            }
        }

        return JSONObject.toJSONString(result);
    }

}
