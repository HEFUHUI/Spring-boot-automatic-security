package com.mrhui.automatic.controller;

import com.mrhui.automatic.entity.TUser;
import com.mrhui.automatic.exception.ParamsException;
import com.mrhui.automatic.pojo.Page;
import com.mrhui.automatic.pojo.StandardResult;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface CrudController<T> {
    @GetMapping
    @ResponseBody
    public StandardResult<Page<T>> get(
            @RequestParam(required = false,value = "q") List<String> query,
            @RequestParam(value = "page",defaultValue = "1",required = false) int page,
            @RequestParam(value = "limit",defaultValue = "10",required = false) int limit
    );

    @PostMapping
    @ResponseBody
    public StandardResult<T> add(@RequestBody  T data);

    @PutMapping("/{id}")
    @ResponseBody
    public StandardResult<T> update(@RequestBody T data,@PathVariable("id") String id);

    @DeleteMapping("/{id}")
    @ResponseBody
    public StandardResult<T> delete(@PathVariable("id") String id);


    @GetMapping("/find/{id}")
    @ResponseBody
    public StandardResult<T> getWithId(@PathVariable("id") String id);
}
