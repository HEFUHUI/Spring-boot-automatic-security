package com.mrhui.automatic.service.impl;

import com.mrhui.automatic.entity.TRole;
import com.mrhui.automatic.entity.TUser;
import com.mrhui.automatic.mapper.TUserMapper;
import com.mrhui.automatic.pojo.Page;
import com.mrhui.automatic.service.TUserService;
import com.mrhui.automatic.exception.DatabaseException;
import com.mrhui.automatic.utils.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class TUserServiceImpl implements TUserService {
    @Autowired
    TUserMapper userMapper;

    @Autowired
    IdWorker idWorker;

    @Override
    public TUser findById(String id) {
        return userMapper.findById(id);
    }

    @Override
    public boolean update(TUser data, String id) {
        return userMapper.update(data,id);
    }

    @Override
    public boolean remove(TUser data) {
        return false;
    }

    @Override
    public boolean remove(String id) {
        return userMapper.remove(id);
    }

    @Override
    public Boolean add(TUser data) {
        // 设置雪花算法为用户唯一ID
        data.setUserId(idWorker.nextId());
        // 对密码进行加密
        data.setPassword(new Md5Hash(data.getPassword(),data.getLoginName(),2).toString());
        Boolean add = userMapper.add(data);
        for (TRole role : data.getRoles()) {
            userMapper.setRole(role.getRoleId(),data.getUserId());
        }
        return add;
    }

    @Override
    public TUser findByUserName(String username) {
        return userMapper.findByUserName(username);
    }

    @Override
    public boolean setRole(String[] roles, String userId) {
//        遍历每一个角色，然后添加到用户上面
        for (String role : roles) {
            userMapper.setRole(role,userId);
        }
        return true;
    }

    @Override
    public boolean revoke(String roleId, String userId) {
        return userMapper.revoke(roleId,userId);
    }

    @Override
    public boolean setClass(String userId, String classId) {
        return userMapper.setClass(userId, classId);
    }

    @Override
    public Page<TUser> findAll(Page<TUser> page) {
        // 计算跳过的个数
        int currentSkip = (page.getPage()-1)* page.getLimit();
        //取出所有放到Items里面
        page.setItems(userMapper.findAll(page.getLimit(),currentSkip));
        page.setCurrentPage(page.getPage());
        //查出所有个数
        page.setTotal(userMapper.getCount());
        return page;
    }

    @Override
    public Page<TUser> findByQuery(Map<String, Object> map,Page<TUser> page) {
        if(page == null){
            page = new Page<>();
        }
        try {
            // 计算跳过的个数
            int currentSkip = (page.getPage()-1)* page.getLimit();
            //取出所有放到Items里面
            page.setItems(userMapper.findByQuery(map,page.getLimit(),currentSkip));
            page.setCurrentPage(page.getPage());
            //查出所有个数
            page.setTotal(userMapper.getCountByQuery(map));
            return page;
        }catch (RuntimeException e){
            log.error("用户信息获取失败："+e.getMessage());
            throw new DatabaseException("用户信息获取失败！");
        }
    }
}
