package com.mrhui.automatic.mapper;

import com.mrhui.automatic.entity.TUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @Entity com.mrhui.automatic.entity.TUser
*/
@Mapper
public interface TUserMapper {
    TUser findById(@Param("id") String id);

    TUser findByUserName(@Param("username") String username);

    List<TUser> findAll(@Param("limit") int limit, @Param("skip") int skip);

    int getCount();

    int getCountByQuery(@Param("query") Map<String,Object> map);

    boolean remove(@Param("id") String id);

    List<TUser> findByQuery(@Param("query") Map<String, Object> map,@Param("limit") int limit,@Param("skip") int skip);

    Boolean add(TUser data);

    void setRole(@Param("role") String role,@Param("userId") String userId);

    boolean update(@Param("data") TUser data, @Param("id") String id);

    boolean revoke(@Param("roleId") String roleId,@Param("userId") String userId);

    boolean setClass(@Param("userId") String userId,@Param("classId") String classId);
}
