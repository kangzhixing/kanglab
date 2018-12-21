package com.kang.codetool.controller.tool;

import com.kang.codetool.aop.anntion.ViewPage;
import com.kang.codetool.common.KlRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.net.URLDecoder;
import java.net.URLEncoder;

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
}
