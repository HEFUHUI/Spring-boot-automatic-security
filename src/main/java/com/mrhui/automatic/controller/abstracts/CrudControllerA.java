package com.mrhui.automatic.controller.abstracts;

import com.mrhui.automatic.controller.CrudController;
import com.mrhui.automatic.entity.TUser;
import com.mrhui.automatic.exception.ParamsException;
import com.mrhui.automatic.pojo.Page;
import com.mrhui.automatic.pojo.StandardResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class CrudControllerA<T> implements CrudController<T> {
    public Map<String,Object> map;
    @Override
    public StandardResult<Page<T>> get(List<String> query, int page, int limit) {
        map = new HashMap<>();
        if(query != null){
            if(query.size() % 2 != 0){
                throw new ParamsException("查询参数必须是2的倍数！");
            }
            for (int i = 0; i < query.size() - 1; i+=2) {
                map.put(query.get(i), query.get(i + 1));
            }
        }
        if(page < 1){
            throw new ParamsException("没有第0页!");
        }
        if(limit < 5){
            throw new ParamsException("每页的个数必须是5个或者以上!");
        }
//        将接收到的数组转换为Map<String,Object>
        return null;
    }

    @Override
    public StandardResult<T> add(T data) {
        return null;
    }

    @Override
    public StandardResult<T> update(T data, String id) {
        return null;
    }

    @Override
    public StandardResult<T> delete(String id) {
        return null;
    }

    @Override
    public StandardResult<T> getWithId(String id) {
        return null;
    }
}
