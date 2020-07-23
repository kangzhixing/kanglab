package com.kang.codetool.common;

import lombok.Data;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

@Data
public class KlResponse<T> implements Serializable {

    private String host;
    private Date requestTime;
    private Integer code;
    private String msg;
    private T body;

    public KlResponse() {
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

    public static KlResponse success(){
        return new KlResponse();
    }

    public static KlResponse success(Object body){
        KlResponse res = new KlResponse();
        res.setBody(body);
        return res;
    }

    public static KlResponse fail(String msg){
        KlResponse res = new KlResponse();
        res.setMsg(msg);
        res.setCode(0);
        return res;
    }

    public static KlResponse error(Exception ex){
        KlResponse res = new KlResponse();
        res.setMsg(ex.getMessage());
        res.setCode(0);
        return res;
    }
}
