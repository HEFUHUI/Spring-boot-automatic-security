package com.mrhui.automatic;

import com.mrhui.automatic.config.SpringUtil;
import com.mrhui.automatic.pojo.ProjectConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@Slf4j
@ServletComponentScan(basePackages = "com.fuhui.automatic")
public class AutomaticApplication {
    public static void main(String[] args) {
        SpringApplication.run(AutomaticApplication.class, args);
    }
}
