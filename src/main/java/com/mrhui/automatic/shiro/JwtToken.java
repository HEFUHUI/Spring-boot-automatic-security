package com.mrhui.automatic.shiro;

import com.mrhui.automatic.utils.JwtUtils;
import org.apache.shiro.authc.AuthenticationToken;

public class JwtToken implements AuthenticationToken
{

    private final String loginName ;
    private final String token;


    public JwtToken(String token) {
        this.loginName = JwtUtils.getClaimFiled(token, "loginName");
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return loginName;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
