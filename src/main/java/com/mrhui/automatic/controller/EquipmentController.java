package com.mrhui.automatic.controller;

import com.mrhui.automatic.controller.abstracts.CrudControllerA;
import com.mrhui.automatic.entity.TUser;
import com.mrhui.automatic.entity.vo.UserVO;
import com.mrhui.automatic.pojo.Common;
import com.mrhui.automatic.pojo.Page;
import com.mrhui.automatic.pojo.StandardResult;
import com.mrhui.automatic.service.TUserService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("equ")
public class EquipmentController extends CrudControllerA<TUser> {
    @Autowired
    TUserService userService;

    @Override
    public StandardResult<TUser> getWithId(String id) {
        final TUser tu = userService.findById(id);
        if(tu!=null){
            return StandardResult.success("获取成功！",tu);
        }
        return StandardResult.failed("设备不存在！");
    }
}
