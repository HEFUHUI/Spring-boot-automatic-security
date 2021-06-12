package com.mrhui.automatic.mapper;

import com.mrhui.automatic.entity.Logging;
import com.mrhui.automatic.utils.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
* @Entity com.mrhui.automatic.entity.Logging
*/
@Mapper
public interface LoggingMapper extends BaseMapper<Logging> {
    public int getCountByQuery(@Param("query") Map<String, Object> map);
}
