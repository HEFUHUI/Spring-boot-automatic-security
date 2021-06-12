package com.mrhui.automatic.utils;

import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface BaseMapper<T> {
    T findById(@Param("id") String id);

    T findByName(String name);

    List<T> findAll(@Param("limit") int limit,@Param("skip") int skip);

    int getCount();

    boolean remove(@Param("id") String id);

    List<T> findByQuery(@Param("query") Map<String, Object> map,@Param("limit") int limit,@Param("skip") int skip);

    Boolean add(T data);
}
