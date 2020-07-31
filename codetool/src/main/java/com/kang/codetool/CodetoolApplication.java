package com.kang.codetool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication(scanBasePackages = {"com.kang.codetool"}, exclude = {DataSourceAutoConfiguration.class, MongoAutoConfiguration.class})
public class CodetoolApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder){
        return builder.sources(CodetoolApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(CodetoolApplication.class, args);
    }
}
