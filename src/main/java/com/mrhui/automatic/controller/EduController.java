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
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/edu")
public class EduController {

    @Autowired
    TUserService userService;

    @Autowired
    TClassService classService;
}
