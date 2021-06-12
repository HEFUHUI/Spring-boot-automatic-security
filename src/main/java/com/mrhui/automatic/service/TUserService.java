package com.mrhui.automatic.service;

import com.mrhui.automatic.entity.TUser;

import java.util.List;

/**
* TUser服务
*/
public interface TUserService extends CrudService<TUser>{
    TUser findByUserName(String username);
    boolean setRole(String[] roles, String userId);
    boolean revoke(String roleId, String userId);
    boolean setClass(String userId,String classId);
}
