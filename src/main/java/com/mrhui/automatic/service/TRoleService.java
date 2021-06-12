package com.mrhui.automatic.service;

import com.mrhui.automatic.entity.TRole;
import com.mrhui.automatic.pojo.Page;

import java.util.List;

/**
*
*/
public interface TRoleService extends CrudService<TRole>{
    public List<TRole> findByUserId(String id);

    public Page<TRole> findAllByPage(Page<TRole> objectPage);
}
