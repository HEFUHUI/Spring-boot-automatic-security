package com.mrhui.automatic.controller;

import com.google.gson.Gson;
import com.mrhui.automatic.entity.vo.UserVO;
import com.mrhui.automatic.pojo.StandardResult;
import com.mrhui.automatic.service.TClassService;
import com.mrhui.automatic.service.TUserService;
import com.mrhui.automatic.service.WebSocketService;
import com.mrhui.automatic.service.impl.WebSocketServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@Slf4j
public class HomeController {
    @Autowired
    private TUserService userService;

    @Autowired
    private TClassService classService;

    @Autowired
    private WebSocketService webSocketService;

    @Autowired
    private Gson gson;

    @GetMapping({"/index","/"})
    public String index(Model model){
        UserVO tUser = (UserVO) SecurityUtils.getSubject().getPrincipal();
        model.addAttribute("user",tUser);
        return "index";
    }
}
