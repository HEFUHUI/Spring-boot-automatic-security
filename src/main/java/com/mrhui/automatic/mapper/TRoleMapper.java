package com.mrhui.automatic.mapper;

import com.mrhui.automatic.entity.TRole;
import com.mrhui.automatic.utils.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @Entity com.mrhui.automatic.entity.TRole
*/
@Mapper
public interface TRoleMapper extends BaseMapper<TRole> {
    List<TRole> findByUserId(String id);
    boolean deleteByRoleId(String id);
    Boolean add(@Param("role") TRole data);
    boolean addPermissions(@Param("per_id") String per_id,@Param("role_id") String role_id);
}
