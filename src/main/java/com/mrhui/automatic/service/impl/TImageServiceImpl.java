package com.mrhui.automatic.service.impl;

import com.mrhui.automatic.entity.TImage;
import com.mrhui.automatic.entity.TUser;
import com.mrhui.automatic.mapper.TImageMapper;
import com.mrhui.automatic.pojo.Page;
import com.mrhui.automatic.service.TImageService;
import com.mrhui.automatic.utils.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
*
*/
@Service
public class TImageServiceImpl implements TImageService{

    @Autowired
    TImageMapper imageMapper;

    @Autowired
    IdWorker idWorker;

    @Override
    public Page<TImage> findAll(Page<TImage> page) {
        return null;
    }

    @Override
    public Page<TImage> findByQuery(Map<String, Object> map, Page<TImage> page) {
        if(page == null){
            page = new Page<TImage>();
        }
        // 计算跳过的个数
        int currentSkip = (page.getPage()-1)* page.getLimit();
        //取出所有放到Items里面
        page.setItems(imageMapper.findByQuery(map,page.getLimit(),currentSkip));
        page.setCurrentPage(page.getPage());
        //查出所有个数
        page.setTotal(imageMapper.getCountByQuery(map));
        return page;
    }

    @Override
    public TImage findById(String id) {
        return imageMapper.findById(id);
    }

    @Override
    public boolean update(TImage data, String id) {
        return false;
    }

    @Override
    public boolean remove(TImage data) {
        return false;
    }

    @Override
    public boolean remove(String id) {
        return imageMapper.remove(id);
    }

    @Override
    public Boolean add(TImage image) {
        image.setImageId(idWorker.nextId());
        return imageMapper.add(image);
    }

}
