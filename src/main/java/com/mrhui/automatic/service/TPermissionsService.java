package com.mrhui.automatic.service;

import com.mrhui.automatic.entity.TPermissions;
import com.mrhui.automatic.pojo.Page;

import java.util.List;

/**
*
*/
public interface TPermissionsService extends CrudService<TPermissions>{
    List<TPermissions> findByRoleId(String id);

    Page<TPermissions> findAllByPage(Page<TPermissions> page);
}
