package com.mrhui.automatic.controller;

import com.mrhui.automatic.controller.abstracts.CrudControllerA;
import com.mrhui.automatic.entity.TClass;
import com.mrhui.automatic.pojo.Page;
import com.mrhui.automatic.pojo.StandardResult;
import com.mrhui.automatic.service.TClassService;
import groovy.util.logging.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/class")
public class ClassController extends CrudControllerA<TClass> {

    @Autowired
    TClassService classService;

    @Override
    public StandardResult<Page<TClass>> get(List<String> query, int page, int limit) {
         super.get(query, page, limit);
         Page<TClass> classPage = classService.findByQuery(map,new Page<>(limit,page));
         return StandardResult.success("获取成功!",classPage);
    }

    @Override
    public StandardResult<TClass> add(TClass data) {
        if (classService.add(data)) {
            return StandardResult.success(HttpStatus.NO_CONTENT.value());
        }
        return StandardResult.failed("添加失败!", HttpStatus.NOT_ACCEPTABLE.value());
    }

    @Override
    public StandardResult<TClass> delete(String id) {
        if (classService.remove(id)) {
            return StandardResult.success(HttpStatus.NO_CONTENT.value());
        }
        return StandardResult.failed(HttpStatus.NOT_ACCEPTABLE.value());
    }

    @PutMapping("updateState/{id}/{working}")
    @ResponseBody
    StandardResult<TClass> updateState(@PathVariable String id, @PathVariable boolean working){
        if (classService.updateWorking(working,id)) {
            return StandardResult.success(HttpStatus.NO_CONTENT.value());
        }else{
            return StandardResult.failed(HttpStatus.NOT_ACCEPTABLE.value());
        }
    }
}
