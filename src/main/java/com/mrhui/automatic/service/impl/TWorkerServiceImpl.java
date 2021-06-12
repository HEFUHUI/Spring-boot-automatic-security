package com.mrhui.automatic.service.impl;

import com.mrhui.automatic.entity.TWorker;
import com.mrhui.automatic.pojo.Page;
import com.mrhui.automatic.service.TWorkerService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
*
*/
@Service
public class TWorkerServiceImpl
implements TWorkerService{

    @Override
    public Page<TWorker> findAll(Page<TWorker> page) {
        return null;
    }

    @Override
    public Page<TWorker> findByQuery(Map<String, Object> map, Page<TWorker> page) {
        return null;
    }

    @Override
    public TWorker findById(String id) {
        return null;
    }

    @Override
    public boolean update(TWorker data, String id) {
        return false;
    }

    @Override
    public boolean remove(TWorker data) {
        return false;
    }

    @Override
    public boolean remove(String id) {
        return false;
    }

    @Override
    public Boolean add(TWorker data) {
        return null;
    }
}
