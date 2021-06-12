package com.mrhui.automatic.service.impl;

import com.mrhui.automatic.entity.TClass;
import com.mrhui.automatic.entity.TPermissions;
import com.mrhui.automatic.entity.TRole;
import com.mrhui.automatic.mapper.TRoleMapper;
import com.mrhui.automatic.pojo.Page;
import com.mrhui.automatic.service.TRoleService;
import com.mrhui.automatic.utils.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 *
 */
@Service
public class TRoleServiceImpl implements TRoleService {

    @Autowired
    TRoleMapper roleMapper;

    @Autowired
    IdWorker idWorker;

    @Override
    public List<TRole> findByUserId(String id) {
        return roleMapper.findByUserId(id);
    }

    @Override
    public Page<TRole> findAllByPage(Page<TRole> page) {
        // 计算跳过的个数
        int currentSkip = (page.getPage()-1)* page.getLimit();
        //取出所有放到Items里面
        page.setItems(roleMapper.findAll(page.getLimit(),currentSkip));
        page.setCurrentPage(page.getPage());
        //查出所有个数
        page.setTotal(roleMapper.getCount());
        return page;
    }

    @Override
    public Page<TRole> findAll(Page<TRole> page) {
        page.setItems(roleMapper.findAll(1000,0));
        page.setCurrentPage(page.getPage());
        return page;
    }

    @Override
    public Page<TRole> findByQuery(Map<String, Object> map,Page<TRole> page) {
        // 计算跳过的个数
        int currentSkip = (page.getPage()-1)* page.getLimit();
        //取出所有放到Items里面
        page.setItems(roleMapper.findByQuery(map,page.getLimit(),currentSkip));
        page.setCurrentPage(page.getPage());
        //查出所有个数
        page.setTotal(roleMapper.getCount());
        return page;
    }

    @Override
    public TRole findById(String id) {
        return null;
    }

    @Override
    public boolean update(TRole data, String id) {
        return false;
    }

    @Override
    public boolean remove(TRole data) {
        return false;
    }

    @Override
    public boolean remove(String id) {
        return roleMapper.deleteByRoleId(id);
    }

    @Override
    public Boolean add(TRole data) {
        if(data.getComment().isEmpty()){
            data.setComment(" ");
        }
        //设置角色ID
        data.setRoleId(idWorker.nextId());
        //存储并返回角色ID
        roleMapper.add(data);
        //给角色授权
        for (TPermissions permission : data.getPermissions()) {
            roleMapper.addPermissions(permission.getPermissionsId(),data.getRoleId());
        }
        return true;
    }
}
