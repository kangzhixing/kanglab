package com.kang.codetool.common;

import lombok.Data;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

@Data
public class RestResponse<T> implements Serializable {

    private String host;
    private Date requestTime;
    private Integer code;
    private String msg;
    private T body;

    public RestResponse() {
        String host = "";

        try {
            host = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException var3) {
            var3.printStackTrace();
        }

        this.host = host;
        this.requestTime = new Date();
        this.code = 1;
    }

    public static RestResponse success(){
        return new RestResponse();
    }

    public static RestResponse success(Object body){
        RestResponse res = new RestResponse();
        res.setBody(body);
        return res;
    }

    public static RestResponse fail(String msg){
        RestResponse res = new RestResponse();
        res.setMsg(msg);
        res.setCode(0);
        return res;
    }

    public static RestResponse error(Exception ex){
        RestResponse res = new RestResponse();
        res.setMsg(ex.getMessage());
        res.setCode(0);
        return res;
    }
}
