package com.mrhui.automatic.realm;

import com.mrhui.automatic.entity.TPermissions;
import com.mrhui.automatic.entity.TUser;
import com.mrhui.automatic.entity.vo.UserVO;
import com.mrhui.automatic.service.LoggingService;
import com.mrhui.automatic.service.TPermissionsService;
import com.mrhui.automatic.service.TRoleService;
import com.mrhui.automatic.service.TUserService;
import com.mrhui.automatic.utils.MD5;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedList;
import java.util.List;


@Slf4j
public class UserRealm extends AuthorizingRealm {
    @Autowired
    public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
        super.setCredentialsMatcher(credentialsMatcher);
    }


    @Autowired
    LoggingService loggingService;

    @Autowired
    TPermissionsService permissionsService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        // 获取当期用户
        final Subject currentUser = SecurityUtils.getSubject();
        // 获取当前用户信息
        final UserVO userVO = (UserVO) currentUser.getPrincipal();
        // 获取权限并授权
        userVO.getPermissions().forEach(el->{
            authorizationInfo.addStringPermission(el.getCode());
        });
        return authorizationInfo;
    }

    @Autowired TUserService userService;

    @Autowired MD5 md5;

    @Autowired TRoleService roleService;

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        // 获取用户Token
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        //查询到用户信息
        TUser user = userService.findByUserName(token.getUsername());
        if(null == user){
            return null;
        }
        List<TPermissions> tPermissions = new LinkedList<>();
        // 通过用户ID查出用户身份
        val roles = roleService.findByUserId(user.getUserId());
        // 通过身份ID查出权限
        roles.forEach(element->{
            tPermissions.addAll(permissionsService.findByRoleId(element.getRoleId()));
        });
        UserVO userVo = new UserVO();
        // 设置用户信息到VO
        userVo.setUser(user);
        // 设置权限列表到VO
        userVo.setPermissions(tPermissions);
        // 设置角色到VO
        userVo.setRoles(roles);
        // 验证密码
        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(userVo,
                user.getPassword(),
                ByteSource.Util.bytes(user.getLoginName()),
                getName());
        //更新用户为登录状态
        loggingService.login(true,user.getUserId(),"登录成功!");
        return simpleAuthenticationInfo;
    }
}
