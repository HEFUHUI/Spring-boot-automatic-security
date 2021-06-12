package com.mrhui.automatic.service.impl;

import com.mrhui.automatic.entity.TNotification;
import com.mrhui.automatic.pojo.Page;
import com.mrhui.automatic.service.TNotificationService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
*
*/
@Service
public class TNotificationServiceImpl implements TNotificationService{

    @Override
    public Page<TNotification> findAll(Page<TNotification> page) {
        return null;
    }

    @Override
    public Page<TNotification> findByQuery(Map<String, Object> map, Page<TNotification> page) {
        return null;
    }

    @Override
    public TNotification findById(String id) {
        return null;
    }

    @Override
    public boolean update(TNotification data, String id) {
        return false;
    }

    @Override
    public boolean remove(TNotification data) {
        return false;
    }

    @Override
    public boolean remove(String id) {
        return false;
    }

    @Override
    public Boolean add(TNotification data) {
        return null;
    }
}
