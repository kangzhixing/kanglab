package com.kang.lab.utils.net;

import com.kang.lab.utils.ConvertUtil;
import com.kang.lab.utils.OSUtil;
import com.kang.lab.utils.enums.OSEnum;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PingUtil {

    public static PingResult get(String ipAddress, int pingTimes) {
        if (pingTimes <= 0) {
            return null;
        }
        PingResult result = new PingResult();

        Runtime r = Runtime.getRuntime();
        String pingCommand;
        if (OSEnum.WINDOWS.equals(OSUtil.currentOs)) {
            //将要执行的ping命令,此命令是windows格式的命令
            pingCommand = "ping " + ipAddress + " -n " + pingTimes;
        } else {
            //将要执行的ping命令,此命令是Linux格式的命令
            //-c:次数,-w:超时时间(单位/ms)  ping -c 10 -w 0.5 192.168.120.206
            pingCommand = "ping -c " + pingTimes + " " + ipAddress;
        }

        try {
            Process process = r.exec(pingCommand);
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));

            int receivedTimes = 0;
            double totalTime = 0;

            String line;
            while ((line = br.readLine()) != null) {
                if (!line.toLowerCase().contains("ttl=") || !line.toLowerCase().contains("ms")) {
                    continue;
                }
                receivedTimes++;
                if (OSEnum.WINDOWS.equals(OSUtil.currentOs)) {
                    if (line.indexOf("time=") != -1) {
                        totalTime += ConvertUtil.tryToDouble(line.substring(line.indexOf("time=") + 5, line.indexOf("ms")));
                        break;
                    } else if (line.indexOf("时间=") != -1) {
                        totalTime += ConvertUtil.tryToDouble(line.substring(line.indexOf("时间=") + 3, line.indexOf("ms")));
                        break;
                    }
                } else {
                    if (line.indexOf("time=") != -1) {
                        totalTime += ConvertUtil.tryToDouble(line.substring(line.indexOf("time=") + 5, line.indexOf(" ms")));
                        break;
                    }
                }
            }

            result.setReceived(receivedTimes);
            result.setLoss(pingTimes - receivedTimes);
            result.setLossProbability(pingTimes - receivedTimes);
            if (receivedTimes == 0) {
                result.setAverageTime(0);
            } else {
                result.setAverageTime(totalTime / receivedTimes);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        return result;
    }


}
