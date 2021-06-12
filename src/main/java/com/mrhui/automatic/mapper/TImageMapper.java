package com.mrhui.automatic.mapper;

import com.mrhui.automatic.entity.TImage;
import com.mrhui.automatic.utils.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
* @Entity com.mrhui.automatic.entity.TImage
*/
@Mapper
public interface TImageMapper extends BaseMapper<TImage> {
    Boolean add(TImage image);
    int getCountByQuery(@Param("query") Map<String, Object> map);
}
