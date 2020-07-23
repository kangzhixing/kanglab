package com.kang.codetool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

@SpringBootApplication(scanBasePackages = {"com.kang.codetool"}, exclude = {DataSourceAutoConfiguration.class, MongoAutoConfiguration.class})
public class CodetoolApplication {

    public static void main(String[] args) {
        SpringApplication.run(CodetoolApplication.class, args);
    }

}
