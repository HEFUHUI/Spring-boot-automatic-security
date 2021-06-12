package com.mrhui.automatic.service;

import com.mrhui.automatic.entity.TClass;

/**
*
*/
public interface TClassService extends CrudService<TClass> {
    boolean updateWorking(boolean working,String id);
}
