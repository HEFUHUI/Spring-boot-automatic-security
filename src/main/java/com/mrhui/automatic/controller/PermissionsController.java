package com.mrhui.automatic.controller;

import com.mrhui.automatic.controller.abstracts.CrudControllerA;
import com.mrhui.automatic.entity.TPermissions;
import com.mrhui.automatic.entity.TUser;
import com.mrhui.automatic.entity.vo.UserVO;
import com.mrhui.automatic.pojo.Page;
import com.mrhui.automatic.pojo.StandardResult;
import com.mrhui.automatic.service.TPermissionsService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/permissions")
public class PermissionsController extends CrudControllerA<TPermissions> {

    @Autowired
    TPermissionsService permissionsService;

    @Override
    public StandardResult<Page<TPermissions>> get(
            @RequestParam(required = false,value = "q") List<String>  query,
            @RequestParam(value = "page",defaultValue = "1",required = false) int page,
            @RequestParam(value = "limit",defaultValue = "10",required = false) int limit
    ) {
        super.get(query,page,limit);
        return StandardResult.success("获取成功！",permissionsService.findByQuery(map,new Page<>(limit,page)));
    }

    @Override
    public StandardResult<TPermissions> add(TPermissions data) {
        if (permissionsService.add(data)) {
            return StandardResult.success(HttpStatus.NO_CONTENT.value());
        }else {
            return StandardResult.failed(HttpStatus.NOT_ACCEPTABLE.value());
        }
    }

    @Override
    public StandardResult<TPermissions> update(TPermissions data, String id) {
        return null;
    }



    @Override
    public StandardResult<TPermissions> delete(String id) {
            if(permissionsService.remove(id)){
                return StandardResult.success(HttpStatus.NO_CONTENT.value());
            }else{
                return StandardResult.failed(HttpStatus.NOT_ACCEPTABLE.value());
            }
    }

    @GetMapping("index")
    public String index(Model model){
        UserVO tUser = (UserVO) SecurityUtils.getSubject().getPrincipal();
        model.addAttribute("user",tUser);
        model.addAttribute("title","权限管理");
        model.addAttribute("pages",permissionsService.findAllByPage(new Page<>()));
        return "pages/permissions";
    }

    @PostMapping("/add")
    public String addPer(TPermissions permissions){
        try {
            permissionsService.add(permissions);
            return "redirect:/permissions/index";
        }catch (Exception e){
            return "redirect:/error";
        }
    }
}
