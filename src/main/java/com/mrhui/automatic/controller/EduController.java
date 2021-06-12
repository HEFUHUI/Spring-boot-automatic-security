package com.mrhui.automatic.controller;

import com.mrhui.automatic.entity.vo.UserVO;
import com.mrhui.automatic.pojo.Common;
import com.mrhui.automatic.pojo.Page;
import com.mrhui.automatic.service.TClassService;
import com.mrhui.automatic.service.TUserService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;

@Controller
@RequestMapping("/edu")
public class EduController {

    @Autowired
    TUserService userService;

    @Autowired
    TClassService classService;

    @GetMapping("/student")
    public String student(Model model,
                          @RequestParam(value = "page",defaultValue = "1",required = false) int page,
                          @RequestParam(value = "limit",defaultValue = "10",required = false) int limit){
        UserVO tUser = (UserVO) SecurityUtils.getSubject().getPrincipal();
        model.addAttribute("user",tUser);
        model.addAttribute("title","学生");
        // 查询user_type为学生的类型
        HashMap<String,Object> map = new HashMap<>();
        map.put("user_type", Common.TYPE_STUDENT);
        model.addAttribute("pages",userService.findByQuery(map,new Page<>(limit,page)));
        model.addAttribute("classes",classService.findAll(new Page<>()));
        return "pages/student";
    }

    @GetMapping("/student/add")
    public String student(Model model){
        UserVO tUser = (UserVO) SecurityUtils.getSubject().getPrincipal();
        model.addAttribute("user",tUser);
        model.addAttribute("title","学生");
        model.addAttribute("pages",userService.findAll(new Page<>()));
        return "pages/student_add";
    }
}
