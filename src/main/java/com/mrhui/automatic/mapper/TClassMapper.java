package com.mrhui.automatic.mapper;

import com.mrhui.automatic.entity.TClass;
import com.mrhui.automatic.utils.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
* @Entity com.mrhui.automatic.entity.TClass
*/
@Mapper
public interface TClassMapper extends BaseMapper<TClass> {
    int getCountByQuery(@Param("query") Map<String, Object> map);
    boolean updateState(@Param("working") boolean working,@Param("id") String classId);
}
