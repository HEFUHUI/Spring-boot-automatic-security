package com.mrhui.automatic.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.mrhui.automatic.entity.TUser;
import com.mrhui.automatic.entity.vo.UserVO;
import com.mrhui.automatic.pojo.LoginInput;
import com.mrhui.automatic.pojo.StandardResult;
import com.mrhui.automatic.service.TUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {
    public static String VERIRFCODE_NAME = "verifyCode";
    @Autowired
    private DefaultKaptcha captchaProducer;

    @Autowired
    TUserService userService;

    @PostMapping("/login-json")
    @ResponseBody
    public StandardResult<UserVO> login(@RequestBody LoginInput loginInput, HttpSession session){
//        if(!loginInput.getVerificationCode().equalsIgnoreCase((String) session.getAttribute(VERIRFCODE_NAME))){
//            return StandardResult.failed("验证码错误");
//        }
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(loginInput.getUserName(),loginInput.getPassword());
        try{
            // 发起登录
            subject.login(token);
            //登录成功后拿到用户信息
            UserVO userVO = (UserVO) subject.getPrincipal();
            //返回sessionID和用户信息
            return StandardResult.success(session.getId(), HttpStatus.OK.value(),userVO);
        }catch (UnknownAccountException unknownAccountException){
            return StandardResult.failed("用户不存在或已在别处登录！");
        }
        catch (Exception e){
            // 发生错误则表示账户有问题，直接返回
            return StandardResult.failed("用户名或者密码错误!");
        }
    }

    @GetMapping("login")
    public String login(){
        return "login";
    }


    @GetMapping("/unauth")
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public StandardResult<String> unauth(){
        return StandardResult.failed("请登录!");
    }

    @GetMapping("/no_permission")
    public StandardResult<String> no_permission(){
        return StandardResult.failed("用户无权限操作!");
    }

    @GetMapping("/ver")
    public void login(HttpServletRequest httpServletRequest,
                      HttpServletResponse httpServletResponse) throws IOException {
        byte[] captchaOutputStream = null;
        ByteArrayOutputStream imgOutputStream = new ByteArrayOutputStream();
        try {
            //生产验证码字符串并保存到session中
            String verifyCode = captchaProducer.createText();
            log.info("verifyCode={}",verifyCode);
            httpServletRequest.getSession().setAttribute(VERIRFCODE_NAME, verifyCode);
            BufferedImage challenge = captchaProducer.createImage(verifyCode);
            ImageIO.write(challenge, "jpg", imgOutputStream);
        } catch (IllegalArgumentException | IOException e) {
            httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        captchaOutputStream = imgOutputStream.toByteArray();
        httpServletResponse.setHeader("Cache-Control", "no-store");
        httpServletResponse.setHeader("Pragma", "no-cache");
        httpServletResponse.setDateHeader("Expires", 0);
        httpServletResponse.setContentType("image/jpeg");
        ServletOutputStream responseOutputStream = httpServletResponse.getOutputStream();
        responseOutputStream.write(captchaOutputStream);
        responseOutputStream.flush();
        responseOutputStream.close();
    }
}
