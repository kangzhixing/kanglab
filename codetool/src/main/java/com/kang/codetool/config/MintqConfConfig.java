package com.kang.codetool.config;

import com.mintq.conf.core.spring.MintqConfFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author mazhuang
 * Created by mazhuang on 2018/10/17.
 */
@Configuration
public class MintqConfConfig {

    private Logger logger = LoggerFactory.getLogger(MintqConfConfig.class);
    @Value("${mintq.conf.zkaddress}")
    private String zkaddress;
    @Value("${mintq.conf.zkdigest}")
    private String zkdigest;
    @Value("${mintq.conf.env}")
    private String env;
    @Bean
    public MintqConfFactory mintqConfFactory() {
        MintqConfFactory conf = new MintqConfFactory();
        conf.setZkaddress(zkaddress);
        conf.setZkdigest(zkdigest);
        conf.setEnv(env);
        logger.info(">>>>>>>>>>> mintq-conf config init.");
        return conf;
    }


}
