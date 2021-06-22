package com.mrhui.automatic.controller;


import com.mrhui.automatic.controller.abstracts.CrudControllerA;
import com.mrhui.automatic.entity.TPermissions;
import com.mrhui.automatic.entity.TRole;
import com.mrhui.automatic.entity.TUser;
import com.mrhui.automatic.entity.vo.UserVO;
import com.mrhui.automatic.pojo.Page;
import com.mrhui.automatic.pojo.StandardResult;
import com.mrhui.automatic.service.TPermissionsService;
import com.mrhui.automatic.service.TRoleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/role")
@Slf4j
public class RoleController extends CrudControllerA<TRole> {


    @Autowired
    TRoleService roleService;

    @Autowired
    TPermissionsService tPermissionsService;

    @Override
    public StandardResult<Page<TRole>> get(
            @RequestParam(required = false,value = "q") List<String> query,
            @RequestParam(value = "page",defaultValue = "1",required = false) int page,
            @RequestParam(value = "limit",defaultValue = "10",required = false) int limit
    ) {
        super.get(query, page, limit);
        return StandardResult.success("获取成功!",roleService.findByQuery(map,new Page<>(limit,page)));
    }

    @Override
    public StandardResult<TRole> add(TRole data) {
        if (roleService.add(data)) {
            return StandardResult.success(HttpStatus.NO_CONTENT.value());
        }
        return StandardResult.failed(HttpStatus.NOT_ACCEPTABLE.value());
    }

    @Override
    public StandardResult<TRole> update(TRole data, String id) {
        return null;
    }

    @Override
    public StandardResult<TRole> delete(String id) {
        log.info("id={}",id);
        try {
            if(roleService.remove(id)){
                return StandardResult.success(200);
            }else{
                return StandardResult.failed("删除失败!",400);
            }
        }catch (Exception e){
            return StandardResult.failed("删除失败,服务器错误!");
        }
    }

    @GetMapping("index")
    public String index(Model model,
                        @RequestParam(value = "page",defaultValue = "1",required = false) int page,
                        @RequestParam(value = "limit",defaultValue = "10",required = false) int limit) {
        //获取当前登录用户并设置到页面上
        UserVO tUser = (UserVO) SecurityUtils.getSubject().getPrincipal();
        model.addAttribute("user",tUser);
        //设置页面标题
        model.addAttribute("title","角色");
        // 获取到所有角色数据
        model.addAttribute("pages",roleService.findAllByPage(new Page<>(limit,page)));
        //获取到所有权限
        model.addAttribute("permissions",tPermissionsService.findAllByPage(new Page<>()).getItems());
        return "pages/role";
    }

    @PostMapping("add")
    public String add(TRole tRole, @RequestParam("permission")List<String> permissions, Model model){
        //设置权限列表到角色上面
        permissions.forEach(el->{
            final TPermissions tPermissions = new TPermissions();
            tPermissions.setPermissionsId(el);
            tRole.getPermissions().add(tPermissions);
        });
        //添加到数据库
        roleService.add(tRole);
        return "redirect:/role/index";
    }
}
