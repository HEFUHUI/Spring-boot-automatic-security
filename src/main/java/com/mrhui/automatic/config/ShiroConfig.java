package com.mrhui.automatic.config;

import com.mrhui.automatic.realm.UserRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

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
        Map<String, String> filterMap = new LinkedHashMap<>();
        //设置不需要验证的页面
        filterMap.put("/auth/**", "anon");
        filterMap.put("/send", "anon");
        filterMap.put("/ws", "anon");
        filterMap.put("/index/**", "anon");
        filterMap.put("/js/**", "anon");
        filterMap.put("/image/get/*", "anon");
        filterMap.put("/css/**", "anon");
        filterMap.put("/images/**", "anon");
        filterMap.put("/user/logout", "anon");
//        filterMap.put("/user","perms[user:get]");
        //拦截所有请求 需要验证
        filterMap.put("/**", "authc");

        //设置登录页面
        filterFactoryBean.setLoginUrl("/auth/unauth");
        filterFactoryBean.setUnauthorizedUrl("/auth/no_permission");
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
        manager.setRealm(userRealm);
        manager.setSessionManager(defaultWebSessionManager);
        return manager;
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
    public DefaultWebSessionManager sessionManager(){
        MyWebSessionManager myWebSessionManager = new MyWebSessionManager();
        myWebSessionManager.setSessionIdUrlRewritingEnabled(false);
        return myWebSessionManager;
    }
}
