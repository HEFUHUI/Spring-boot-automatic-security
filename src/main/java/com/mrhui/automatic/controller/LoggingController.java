package com.mrhui.automatic.controller;

import com.mrhui.automatic.controller.abstracts.CrudControllerA;
import com.mrhui.automatic.entity.Logging;
import com.mrhui.automatic.entity.TUser;
import com.mrhui.automatic.pojo.Page;
import com.mrhui.automatic.pojo.StandardResult;
import com.mrhui.automatic.service.LoggingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@Slf4j
@RequestMapping("/logging")
public class LoggingController extends CrudControllerA<Logging> {
    @Autowired
    LoggingService loggingService;

    @Override
    public StandardResult<Page<Logging>> get(List<String> query, int page, int limit) {
        super.get(query, page, limit);
        Page<Logging> loggingPage = loggingService.findByQuery(map,new Page<>(limit,page));
        return StandardResult.success("获取成功！",loggingPage);
    }

    @Override
    public StandardResult<Logging> add(Logging data) {
        return super.add(data);
    }

    @Override
    public StandardResult<Logging> update(Logging data, String id) {
        return super.update(data, id);
    }

    @Override
    public StandardResult<Logging> delete(String id) {
        return super.delete(id);
    }

    @Override
    public StandardResult<Logging> getWithId(String id) {
        return super.getWithId(id);
    }
}
