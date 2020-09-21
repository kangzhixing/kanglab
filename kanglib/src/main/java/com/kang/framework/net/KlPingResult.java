package com.kang.framework.net;

import com.kang.framework.KlConvert;
import com.kang.framework.KlOS;
import lombok.Data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

@Data
public class KlPingResult {

    private int loss;

    private double lossProbability;

    private int received;

    private double averageTime;
}
