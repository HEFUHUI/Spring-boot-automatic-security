package com.mrhui.automatic;

import com.mrhui.automatic.config.SpringUtil;
import com.mrhui.automatic.pojo.ProjectConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.SessionCookieConfig;
import javax.servlet.SessionTrackingMode;
import java.util.Collections;

@SpringBootApplication
@EnableScheduling
@Slf4j
@ServletComponentScan(basePackages = "com.fuhui.automatic")
public class AutomaticApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(AutomaticApplication.class, args);
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);
        servletContext.setSessionTrackingModes(
                Collections.singleton(SessionTrackingMode.COOKIE)
        );
        SessionCookieConfig sessionCookieConfig = servletContext.getSessionCookieConfig();
        sessionCookieConfig.setHttpOnly(true);
    }
}
