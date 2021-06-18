package com.mrhui.automatic.config;

import com.mrhui.automatic.pojo.ProjectConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImageUploadConfig {
    @Bean
    public ProjectConfig fileUpload(){
        return new ProjectConfig();
    }
}