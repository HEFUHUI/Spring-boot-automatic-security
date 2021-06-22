package com.mrhui.automatic.controller;

import com.mrhui.automatic.controller.abstracts.CrudControllerA;
import com.mrhui.automatic.entity.TRole;
import com.mrhui.automatic.entity.TUser;
import com.mrhui.automatic.entity.vo.UserVO;
import com.mrhui.automatic.pojo.Page;
import com.mrhui.automatic.pojo.StandardResult;
import com.mrhui.automatic.service.LoggingService;
import com.mrhui.automatic.service.TImageService;
import com.mrhui.automatic.service.TRoleService;
import com.mrhui.automatic.service.TUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
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
    @RequiresPermissions("user:me")
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
    public StandardResult<TUser> grant(@RequestBody TRole role,@PathVariable String id){
        if (userService.setRole(new String[]{role.getRoleId()},id)) {
            return StandardResult.success(HttpStatus.NO_CONTENT.value());
        }else{
            return StandardResult.failed(HttpStatus.NOT_ACCEPTABLE.value());
        }
    }

    @PutMapping("/revoke/{userId}/{roleId}")
    public StandardResult<TUser> revoke(@PathVariable String roleId, @PathVariable String userId){
        if (userService.revoke(roleId,userId)) {
            return StandardResult.success(HttpStatus.NO_CONTENT.value());
        }else{
            return StandardResult.failed(HttpStatus.NOT_ACCEPTABLE.value());
        }
    }

    @RequiresAuthentication
    @GetMapping("/logout")
    public StandardResult<String> logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return StandardResult.success("已退出登录！");
    }

    @Override
    @RequiresPermissions("user:update")
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
    @RequiresPermissions("user:delete")
    public StandardResult<TUser> delete(String id) {
            if(userService.remove(id)){
                return StandardResult.success(HttpStatus.NO_CONTENT.value());
            }else{
                return StandardResult.failed(HttpStatus.NOT_ACCEPTABLE.value());
            }
    }


    @PutMapping("setClass/{userId}/{classId}")
    public StandardResult<String> setClass(@PathVariable String classId, @PathVariable String userId){
        if (userService.setClass(userId,classId)) {
            return StandardResult.success(HttpStatus.NO_CONTENT.value());
        }
        return StandardResult.failed(HttpStatus.NOT_ACCEPTABLE.value());
    }
}
