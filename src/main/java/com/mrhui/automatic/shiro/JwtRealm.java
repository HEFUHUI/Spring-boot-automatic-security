package com.mrhui.automatic.shiro;

import com.mrhui.automatic.entity.TPermissions;
import com.mrhui.automatic.entity.TRole;
import com.mrhui.automatic.entity.TUser;
import com.mrhui.automatic.entity.vo.UserVO;
import com.mrhui.automatic.service.TPermissionsService;
import com.mrhui.automatic.service.TRoleService;
import com.mrhui.automatic.service.TUserService;
import com.mrhui.automatic.utils.JwtUtils;
import lombok.val;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class JwtRealm extends AuthorizingRealm {

    @Autowired
    TUserService userService;

    @Autowired
    TRoleService roleService;

    @Autowired
    TPermissionsService permissionsService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        JwtToken jwtToken = (JwtToken) token;
        if (jwtToken == null) {
            throw new AccountException("jwt Token 参数异常");
        }
        final String loginName = jwtToken.getPrincipal().toString();
        final String myToken = jwtToken.getCredentials().toString();
        final TUser user = userService.findByUserName(loginName);
        if (user == null) {
            throw new AccountException("用户不存在或者已被删除!");
        }
        List<TPermissions> tPermissions = new LinkedList<>();
        // 通过用户ID查出用户身份
        val roles = roleService.findByUserId(user.getUserId());
        // 通过身份ID查出权限
        roles.forEach(element-> tPermissions.addAll(permissionsService.findByRoleId(element.getRoleId())));
        UserVO userVo = new UserVO();
        // 设置用户信息到VO
        userVo.setUser(user);
        // 设置权限列表到VO
        userVo.setPermissions(tPermissions);
        // 设置角色到VO
        userVo.setRoles(roles);
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(userVo,myToken,getName());
        val sessionId = SecurityUtils.getSubject().getSession().getId();
        val host = SecurityUtils.getSubject().getSession().getHost();
        user.setSessionId(sessionId.toString());
        user.setLoginIp(host);
        return info;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        // 获取当前用户
        UserVO currentUser = (UserVO) SecurityUtils.getSubject().getPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        Set<String> roles = new HashSet<>();
        currentUser.getRoles().forEach(role-> roles.add(role.getName()));
        Set<String> perms = new HashSet<>();
        currentUser.getPermissions().forEach((TPermissions perm) -> perms.add(perm.getCode()));
        info.setRoles(roles);
        info.setStringPermissions(perms);
        return info;
    }
}
