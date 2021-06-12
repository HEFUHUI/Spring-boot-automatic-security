package com.mrhui.automatic.service.impl;

import com.mrhui.automatic.entity.TCourse;
import com.mrhui.automatic.pojo.Page;
import com.mrhui.automatic.service.TCourseService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
*
*/
@Service
public class TCourseServiceImpl implements TCourseService{

    @Override
    public Page<TCourse> findAll(Page<TCourse> page) {
        return null;
    }

    @Override
    public Page<TCourse> findByQuery(Map<String, Object> map, Page<TCourse> page) {
        return null;
    }

    @Override
    public TCourse findById(String id) {
        return null;
    }

    @Override
    public boolean update(TCourse data, String id) {
        return false;
    }

    @Override
    public boolean remove(TCourse data) {
        return false;
    }

    @Override
    public boolean remove(String id) {
        return false;
    }

    @Override
    public Boolean add(TCourse data) {
        return null;
    }
}
