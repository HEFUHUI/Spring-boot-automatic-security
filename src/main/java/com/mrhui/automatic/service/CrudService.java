package com.mrhui.automatic.service;

import com.mrhui.automatic.entity.TClass;
import com.mrhui.automatic.pojo.Page;
import com.mrhui.automatic.utils.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface CrudService<T> {

    Page<T> findAll(Page<T> page);

    Page<T> findByQuery(Map<String, Object> map,Page<T> page);

    T findById(String id);

    boolean update(T data,String id);

    boolean remove(T data);

    boolean remove(String id);

    Boolean add(T data);
}
