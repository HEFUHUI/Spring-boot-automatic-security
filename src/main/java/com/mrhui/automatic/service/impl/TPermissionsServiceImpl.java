package com.mrhui.automatic.service.impl;

import com.mrhui.automatic.entity.TClass;
import com.mrhui.automatic.entity.TPermissions;
import com.mrhui.automatic.mapper.TPermissionsMapper;
import com.mrhui.automatic.pojo.Page;
import com.mrhui.automatic.service.TPermissionsService;
import com.mrhui.automatic.utils.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
*
*/
@Service
public class TPermissionsServiceImpl implements TPermissionsService{

    @Autowired
    TPermissionsMapper permissionsMapper;
    @Autowired
    IdWorker idWorker;

    @Override
    public List<TPermissions> findByRoleId(String id) {
        return permissionsMapper.getAllByRoleId(id);
    }

    @Override
    public Page<TPermissions> findAllByPage(Page<TPermissions> page) {
        int currentSkip = (page.getPage()-1)* page.getLimit();
        page.setItems(permissionsMapper.findAllByPage(page.getLimit(),currentSkip));
        page.setCurrentPage(page.getPage());
        page.setTotal(permissionsMapper.getCount());
        return page;
    }

    @Override
    public Page<TPermissions> findAll(Page<TPermissions> page) {
        return null;
    }

    @Override
    public Page<TPermissions> findByQuery(Map<String, Object> map,Page<TPermissions> page) {
        int currentSkip = (page.getPage()-1)* page.getLimit();
        page.setItems(permissionsMapper.findByQuery(map,page.getLimit(),currentSkip));
        page.setCurrentPage(page.getPage());
        page.setTotal(permissionsMapper.getCount());
        return page;
    }

    @Override
    public TPermissions findById(String id) {
        return null;
    }

    @Override
    public boolean update(TPermissions data, String id) {
        return false;
    }

    @Override
    public boolean remove(TPermissions data) {
        return false;
    }

    @Override
    public boolean remove(String id) {
        return permissionsMapper.deleteByPermissionsId(id);
    }

    @Override
    public Boolean add(TPermissions permissions) {
        permissions.setPermissionsId(idWorker.nextId());
        return permissionsMapper.add(permissions);
    }
}
