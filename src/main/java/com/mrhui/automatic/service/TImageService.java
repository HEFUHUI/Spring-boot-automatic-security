package com.mrhui.automatic.service;

import com.mrhui.automatic.entity.TImage;

/**
*
*/
public interface TImageService extends CrudService<TImage>{
    Boolean add(TImage image);
}
