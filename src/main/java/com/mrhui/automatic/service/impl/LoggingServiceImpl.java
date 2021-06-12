package com.mrhui.automatic.service.impl;

import com.mrhui.automatic.entity.Logging;
import com.mrhui.automatic.exception.DatabaseException;
import com.mrhui.automatic.mapper.LoggingMapper;
import com.mrhui.automatic.pojo.Page;
import com.mrhui.automatic.service.LoggingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.mrhui.automatic.pojo.Common.*;

/**
*
*/
@Service
@Slf4j
public class LoggingServiceImpl implements LoggingService{

    @Autowired
    LoggingMapper loggingMapper;


    @Override
    public Page<Logging> findAll(Page<Logging> page) {
        return null;
    }

    @Override
    public Page<Logging> findByQuery(Map<String, Object> map, Page<Logging> page) {
        if(page == null){
            page = new Page<>();
        }
        try {
            // 计算跳过的个数
            int currentSkip = (page.getPage()-1)* page.getLimit();
            //取出所有放到Items里面
            page.setItems(loggingMapper.findByQuery(map,page.getLimit(),currentSkip));
            page.setCurrentPage(page.getPage());
            //查出所有个数
            page.setTotal(loggingMapper.getCountByQuery(map));
            return page;
        }catch (RuntimeException e){
            log.error("日志信息获取失败："+e.getMessage());
            throw new DatabaseException("日志信息获取失败！");
        }
    }

    @Override
    public Logging findById(String id) {
        return loggingMapper.findById(id);
    }

    @Override
    public boolean update(Logging data, String id) {
        return false;
    }

    @Override
    public boolean remove(Logging data) {
        return false;
    }

    @Override
    public boolean remove(String id) {
        return false;
    }

    @Override
    public Boolean add(Logging data) {
        return loggingMapper.add(data);
    }

    @Override
    public void log(int logType, boolean logOutcome, String userId, String comment) {
        Logging logging = new Logging();
        logging.setLogType(logType);
        logging.setLogOutcome(logOutcome);
        logging.setLogUser(userId);
        logging.setLogComment(comment);
        add(logging);
    }

    @Override
    public void error(boolean logOutcome, String userId, String comment) {
        log(LOG_ERROR,logOutcome,userId,comment);
    }

    @Override
    public void login(boolean logOutcome, String userId, String comment) {
        log(LOG_USER_LOGIN,logOutcome,userId,comment);
    }

    @Override
    public void logout(boolean logOutcome, String userId, String comment) {
        log(LOG_USER_LOGOUT,logOutcome,userId,comment);
    }

    @Override
    public void face(boolean logOutcome, String userId, String comment) {
        log(LOG_FACE_RECOGNITION,logOutcome,userId,comment);
    }

    @Override
    public void warning(boolean logOutcome, String userId, String comment) {
        log(LOG_WARNING,logOutcome,userId,comment);
    }
}
