package com.kang.framework.net;

import com.kang.framework.KlConvert;
import com.kang.framework.KlOS;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class KlPingResult {

    private int loss;

    private double lossProbability;

    private int received;

    private double averageTime;

    public int getLoss() {
        return loss;
    }

    protected void setLoss(int loss) {
        this.loss = loss;
    }

    public double getLossProbability() {
        return lossProbability;
    }

    protected void setLossProbability(double lossProbability) {
        this.lossProbability = lossProbability;
    }

    public int getReceived() {
        return received;
    }

    protected void setReceived(int received) {
        this.received = received;
    }

    public double getAverageTime() {
        return averageTime;
    }

    protected void setAverageTime(double averageTime) {
        this.averageTime = averageTime;
    }
}
