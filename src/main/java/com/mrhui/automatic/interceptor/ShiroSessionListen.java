package com.mrhui.automatic.interceptor;

import com.mrhui.automatic.service.LoggingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

@Slf4j
public class ShiroSessionListen implements SessionListener {
    @Autowired
    LoggingService loggingService;
    @Override
    public void onStart(Session session) {
        log.info("生命周期开始：{},IP={}",session.getId(),session.getHost());
    }

    @Override
    public void onStop(Session session) {
        log.info("生命周期结束：{},IP={}",session.getId(),session.getHost());
    }

    @Override
    public void onExpiration(Session session) {
        log.info("生命周期开始：{},IP={}",session.getId(),session.getHost());
    }
}
