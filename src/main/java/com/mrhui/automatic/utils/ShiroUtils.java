package com.mrhui.automatic.utils;

import com.mrhui.automatic.entity.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;

import java.io.Serializable;

@Slf4j
public class ShiroUtils {
    public static UserVO getUserInfoBySessionId(Serializable sessionId){
        UserVO result = null;
        try{
            Session se = SecurityUtils.getSecurityManager().getSession(new DefaultSessionKey(sessionId));
            Object obj = se.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
            SimplePrincipalCollection coll = (SimplePrincipalCollection) obj;
            result = (UserVO) coll.getPrimaryPrincipal();
        }catch (UnknownSessionException e){
            log.error("There is no session with id [{}]",sessionId);
        }
        return result;
    }
}
