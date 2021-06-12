package com.mrhui.automatic.service.impl;

import com.mrhui.automatic.entity.TClass;
import com.mrhui.automatic.exception.DatabaseException;
import com.mrhui.automatic.exception.ParamsException;
import com.mrhui.automatic.mapper.TClassMapper;
import com.mrhui.automatic.mapper.TUserMapper;
import com.mrhui.automatic.pojo.Page;
import com.mrhui.automatic.service.TClassService;
import com.mrhui.automatic.utils.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;

/**
*
*/
@Service
@Slf4j
public class TClassServiceImpl implements TClassService{

    @Autowired
    TClassMapper classMapper;

    @Autowired
    IdWorker idWorker;

    @Autowired
    TUserMapper userMapper;

    @Override
    public Page<TClass> findAll(Page<TClass> page) {
        // 计算跳过的个数
        int currentSkip = (page.getPage()-1)* page.getLimit();
        //取出所有放到Items里面
        page.setItems(classMapper.findAll(page.getLimit(),currentSkip));
        page.setCurrentPage(page.getPage());
        //查出所有个数
        page.setTotal(classMapper.getCount());
        return page;
    }

    @Override
    public Page<TClass> findByQuery(Map<String, Object> map,Page<TClass> page) {
        if(page == null){
            page = new Page<>();
        }
        try {
            // 计算跳过的个数
            int currentSkip = (page.getPage()-1)* page.getLimit();
            //取出所有放到Items里面
            page.setItems(classMapper.findByQuery(map,page.getLimit(),currentSkip));
            page.setCurrentPage(page.getPage());
            //查出所有个数
            page.setTotal(classMapper.getCountByQuery(map));
            return page;
        }catch (RuntimeException e){
            throw new DatabaseException("班级获取失败！");
        }
    }

    @Override
    public TClass findById(String id) {
        return null;
    }

    @Override
    public boolean update(TClass data, String id) {
        return false;
    }

    @Override
    public boolean remove(TClass data) {
        return false;
    }

    @Override
    public boolean remove(String id) {
        return classMapper.remove(id);
    }

    @Override
    public Boolean add(TClass data) {
        try {
            data.setClassId(idWorker.nextId());
            return classMapper.add(data);
        }catch (Exception e){
            throw new ParamsException("班级添加失败："+e.getMessage());
        }
    }

    @Override
    public boolean updateWorking(boolean working, String id) {
        return classMapper.updateState(working,id);
    }
}
