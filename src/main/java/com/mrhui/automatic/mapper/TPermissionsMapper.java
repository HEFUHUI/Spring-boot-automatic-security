package com.mrhui.automatic.mapper;

import com.mrhui.automatic.entity.TPermissions;
import com.mrhui.automatic.pojo.Page;
import com.mrhui.automatic.utils.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @Entity com.mrhui.automatic.entity.TPermissions
*/
@Mapper
public interface TPermissionsMapper extends BaseMapper<TPermissions> {
    List<TPermissions> getAllByRoleId(String id);

    List<TPermissions> findAllByPage(int limit, int skip);

    int getCount();

    boolean deleteByPermissionsId(String id);

    Boolean add(TPermissions permissions);
}
