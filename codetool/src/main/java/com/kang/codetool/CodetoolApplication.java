package com.kang.codetool;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

@SpringBootApplication(scanBasePackages = {"com.kang.codetool"}, exclude = MongoAutoConfiguration.class)
public class CodetoolApplication extends SpringBootServletInitializer {

    private static final Log log = LogFactory.getLog(CodetoolApplication.class);

    private static final String APPLICATION_CONFIG_DIRECTORY = "/opt/dbconfig/codetool/";

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        File fileDirectorys = new File(APPLICATION_CONFIG_DIRECTORY);
        File[] files = fileDirectorys.listFiles();
        for (File file : files) {
            try {
                log.info("=====配置文件初始化，path={}=====" + APPLICATION_CONFIG_DIRECTORY + file.getName());
                obtainFile(file.getName());
            } catch (IOException e) {
                log.error(e);
            }
        }
        return application.sources(CodetoolApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(CodetoolApplication.class, args);
    }

    /**
     * 获得文件
     *
     * @param fileName
     * @throws IOException
     */
    private void obtainFile(String fileName) throws IOException {
        File file = new File(APPLICATION_CONFIG_DIRECTORY + fileName);
        FileInputStream ins = null;
        if (file.exists()) {
            ins = new FileInputStream(file);
        }

        File toFile = new File(CodetoolApplication.class.getClassLoader().getResource("").getPath() + fileName);

        try (FileOutputStream out = new FileOutputStream(toFile)) {
            byte[] b = new byte[1024];
            int n = 0;

            if (ins == null) {
                return;
            }
            while ((n = ins.read(b)) != -1) {
                out.write(b, 0, n);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        } finally {
            if (ins != null) {
                ins.close();
            }
        }
    }
}
