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

@Controller
@RequestMapping("equ")
public class EquipmentController extends CrudControllerA<TUser> {
    @Autowired
    TUserService userService;


    @GetMapping("index")
    public String index(Model model,
                        @RequestParam(value = "page",defaultValue = "1",required = false) int page,
                        @RequestParam(value = "limit",defaultValue = "10",required = false) int limit){
        UserVO tUser = (UserVO) SecurityUtils.getSubject().getPrincipal();
        model.addAttribute("user",tUser);
        model.addAttribute("title","设备");
        // 查询user_type为设备的类型
        HashMap<String,Object> map = new HashMap<>();
        map.put("user_type", Common.TYPE_HARDWARE);
        model.addAttribute("pages",userService.findByQuery(map,new Page<>(limit,page)));
        return "pages/equipment";
    }

    @Override
    public StandardResult<TUser> getWithId(String id) {
        final TUser tu = userService.findById(id);
        if(tu!=null){
            return StandardResult.success("获取成功！",tu);
        }
        return StandardResult.failed("设备不存在！");
    }
}
