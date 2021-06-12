package com.mrhui.automatic.config;

import com.google.gson.Gson;
import com.mrhui.automatic.utils.IdWorker;
import com.mrhui.automatic.utils.MD5;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UtilsConfig {
    @Bean
    public MD5 md5(){
        return new MD5();
    }

    @Bean
    public IdWorker idWorker(){
        return new IdWorker(0,0,0);
    }

    @Bean
    public Gson gson(){
        return new Gson();
    }
}
