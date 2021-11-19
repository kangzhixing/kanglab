package com.kang.lab.utils.net;

import lombok.Data;

@Data
public class PingResult {

    private int loss;

    private double lossProbability;

    private int received;

    private double averageTime;
}
