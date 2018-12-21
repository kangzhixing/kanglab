package com.kang.codetool.common;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

public class KlRequest<T> implements Serializable {

    private String host;
    private Date requestTime;
    private Integer code;
    private String msg;
    private T body;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Date getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public KlRequest() {
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
}
