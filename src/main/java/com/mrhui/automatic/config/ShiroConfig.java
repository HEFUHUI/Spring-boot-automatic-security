package com.mrhui.automatic.config;

import com.mrhui.automatic.interceptor.ShiroSessionListen;
import com.mrhui.automatic.shiro.JwtRealm;
import com.mrhui.automatic.shiro.UserRealm;
import lombok.val;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.*;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.*;

@Configuration
public class ShiroConfig {
    /**
     * 配置权限过滤器
     * @param webSecurityManager SecurityManager
     * @return ShiroFilterFactoryBean
     */
    @Bean
    public ShiroFilterFactoryBean filterFactoryBean(@Qualifier("webSecurityManager") SecurityManager webSecurityManager) {
        ShiroFilterFactoryBean filterFactoryBean = new ShiroFilterFactoryBean();
        filterFactoryBean.setSecurityManager(webSecurityManager);

        Map<String, Filter> stringFilterMap = new LinkedHashMap<>();
        stringFilterMap.put("jwt",jwtFilter());
        filterFactoryBean.setFilters(stringFilterMap);

        Map<String, String> filterMap = new LinkedHashMap<>();
//        //设置不需要验证的页面
        filterMap.put("/auth/**", "anon");
        filterMap.put("/send", "anon");
        filterMap.put("/ws", "anon");
        filterMap.put("/user/logout", "anon");
        filterMap.put("/image/get/*", "anon");
//        拦截所有请求 需要验证
        filterMap.put("/**","jwt");
//        filterMap.put("/**", "authc");
////        设置登录页面
        filterFactoryBean.setLoginUrl("/auth/login-json");
//        filterFactoryBean.setUnauthorizedUrl("/auth/no_permission");
        filterFactoryBean.setFilterChainDefinitionMap(filterMap);
        return filterFactoryBean;
    }

    /**
     * 配置安全管理器
     * @param userRealm UserRealm
     * @return SecurityManager
     */
    @Bean
    public SecurityManager webSecurityManager(
            @Qualifier("realm") UserRealm userRealm,
            @Qualifier("sessionManager") DefaultWebSessionManager defaultWebSessionManager
    ) {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        List<Realm> realmList = new ArrayList<>();
        realmList.add(userRealm);
        realmList.add(jwtRealm());
        manager.setRealms(realmList);
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        subjectDAO.setSessionStorageEvaluator(sessionStorageEvaluator());
        manager.setSubjectDAO(subjectDAO);
//        manager.setSessionManager(defaultWebSessionManager);
        return manager;
    }



    @Bean
    public SessionStorageEvaluator sessionStorageEvaluator(){
        DefaultSessionStorageEvaluator sessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        sessionStorageEvaluator.setSessionStorageEnabled(false);
        return sessionStorageEvaluator;
    }


    /**
     * 配置加密策略
     * @return HashedCredentialsMatcher
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        hashedCredentialsMatcher.setHashIterations(2);
        return hashedCredentialsMatcher;
    }

    /**
     * 配置Realm对象
     * @return UserRealm
     */
    @Bean
    public UserRealm realm() {
        return new UserRealm();
    }


    @Bean
    public JwtRealm jwtRealm(){
        return new JwtRealm();
    }

    /**
     * 配置授权
     * @param securityManager SecurityManager
     * @return AuthorizationAttributeSourceAdvisor
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(@Qualifier("webSecurityManager") SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    @Bean
    public JwtFilter jwtFilter(){
        val jwtFilter = new JwtFilter();
        jwtFilter.setLoginUrl("/auth/login");
        return jwtFilter;
    }

    @Bean
    public ShiroSessionListen sessionListen(){
        return new ShiroSessionListen();
    }

    @Bean
    public DefaultWebSessionManager sessionManager(@Qualifier("sessionListen") ShiroSessionListen sessionListen){
        Collection<SessionListener> listeners = new ArrayList<>();
        listeners.add(sessionListen);
        MyWebSessionManager myWebSessionManager = new MyWebSessionManager();
        myWebSessionManager.setSessionListeners(listeners);
        myWebSessionManager.setSessionIdUrlRewritingEnabled(false);
        return myWebSessionManager;
    }
}
