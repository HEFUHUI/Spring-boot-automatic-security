package com.mrhui.automatic.controller;

import com.mrhui.automatic.controller.abstracts.CrudControllerA;
import com.mrhui.automatic.entity.TImage;
import com.mrhui.automatic.entity.TRole;
import com.mrhui.automatic.entity.TUser;
import com.mrhui.automatic.entity.vo.UserVO;
import com.mrhui.automatic.exception.ParamsException;
import com.mrhui.automatic.pojo.Page;
import com.mrhui.automatic.pojo.StandardResult;
import com.mrhui.automatic.pojo.WebsocketClient;
import com.mrhui.automatic.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.websocket.Session;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.SubmissionPublisher;

@Controller
@Slf4j
@RequestMapping("/user")
public class UserController extends CrudControllerA<TUser> {
    @Autowired
    TUserService userService;

    @Autowired
    TImageService imageService;

    @Autowired
    TRoleService roleService;

    @Autowired
    LoggingService loggingService;

    @Override
    public StandardResult<Page<TUser>> get(List<String> query, int page, int limit) {
        super.get(query,page,limit);
        Page<TUser> userPage = userService.findByQuery(map,new Page<>(limit,page));
        return StandardResult.success("获取成功！",userPage);
    }


    @GetMapping("{id}")
    @ResponseBody
    public StandardResult<TUser> get(@PathVariable String id) {
        return StandardResult.success("获取成功！",userService.findById(id));
    }

    @Override
    public StandardResult<TUser> add(TUser data) {
        if (userService.add(data)) {
            return StandardResult.success("用户"+data.getLoginName()+"添加成功！",HttpStatus.NO_CONTENT.value());
        }else{
            return StandardResult.failed("添加失败！",HttpStatus.NOT_ACCEPTABLE.value());
        }
    }

    /**
     * 用户获取个人信息包括权限
     * @return StandardResult<UserVO>
     */
    @GetMapping("me")
    @ResponseBody
    public StandardResult<UserVO> me(){
        UserVO userVO = (UserVO) SecurityUtils.getSubject().getPrincipal();
        return StandardResult.success(userVO);
    }

    /**
     * 给用户设置角色
     * @param id 用户ID
     * @param role 角色ID
     * @return 标准输出
     */
    @PutMapping("/grant/{id}")
    @ResponseBody
    public StandardResult<TUser> grant(@RequestBody TRole role,@PathVariable String id){
        if (userService.setRole(new String[]{role.getRoleId()},id)) {
            return StandardResult.success(HttpStatus.NO_CONTENT.value());
        }else{
            return StandardResult.failed(HttpStatus.NOT_ACCEPTABLE.value());
        }
    }

    @PutMapping("/revoke/{userId}/{roleId}")
    @ResponseBody
    public StandardResult<TUser> revoke(@PathVariable String roleId, @PathVariable String userId){
        if (userService.revoke(roleId,userId)) {
            return StandardResult.success(HttpStatus.NO_CONTENT.value());
        }else{
            return StandardResult.failed(HttpStatus.NOT_ACCEPTABLE.value());
        }
    }

    @RequiresAuthentication
    @GetMapping("/logout")
    @ResponseBody
    public StandardResult<String> logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return StandardResult.success("已退出登录！");
    }

    @Override
    public StandardResult<TUser> update(TUser data, String id) {
        if (userService.update(data,id)) {
            return StandardResult.success(HttpStatus.NO_CONTENT.value());
        }else{
            return StandardResult.failed(HttpStatus.NOT_ACCEPTABLE.value());
        }
    }

    /**
     * 删除用户
     * @param id 用户ID
     * @return StandardResult<TUser>
     */
    @Override
    public StandardResult<TUser> delete(String id) {
            if(userService.remove(id)){
                return StandardResult.success(HttpStatus.NO_CONTENT.value());
            }else{
                return StandardResult.failed(HttpStatus.NOT_ACCEPTABLE.value());
            }
    }


    /**
     *
     * @param model 视图模型
     * @param page 第几页
     * @param limit 一页几条
     * @return String
     */
    @GetMapping("index")
    public String index(Model model,
                        @RequestParam(value = "page",defaultValue = "1",required = false) int page,
                        @RequestParam(value = "limit",defaultValue = "10",required = false) int limit){
        //获取当前登录用户的信息
        UserVO userVO = (UserVO) SecurityUtils.getSubject().getPrincipal();
        model.addAttribute("pages",userService.findAll(new Page<>(limit,page)));
        model.addAttribute("roles",roleService.findAll(new Page<>(10,1)));
        model.addAttribute("user",userVO);
        model.addAttribute("title","用户");
        return "pages/user";
    }

    @PutMapping("setClass/{userId}/{classId}")
    @ResponseBody
    public StandardResult<String> setClass(@PathVariable String classId, @PathVariable String userId){
        if (userService.setClass(userId,classId)) {
            return StandardResult.success(HttpStatus.NO_CONTENT.value());
        }
        return StandardResult.failed(HttpStatus.NOT_ACCEPTABLE.value());
    }


    /**
     *
     * @param avatar 用户头像
     * @param user 用户信息
     * @return String template
     * @throws IOException 写入图片可能出错!!!
     */
    @PostMapping("/add")
    public String addUser(@RequestParam("ava") MultipartFile avatar,TUser user) throws IOException {
        // 获取上传路径
        String path = ResourceUtils.getURL("classpath:").getPath()+"static/images/";
        TImage image = new TImage();
        // 判断用户是否上传图片,未上传则跳过
        if(!avatar.isEmpty()){
            avatar.transferTo(new File(path+avatar.getOriginalFilename()));
            image.setAlias(avatar.getOriginalFilename());
            image.setUrl(avatar.getOriginalFilename());
            //添加到数据库
            imageService.add(image);
            //将刚刚添加到数据库的图片设置为用户头像
        }
        user.setAvatar(image);
       try{
           //添加到数据库
           userService.add(user);
       }catch (Exception e){
           return "redirect:/error";
       }
        return "redirect:/user/index";
    }
}
