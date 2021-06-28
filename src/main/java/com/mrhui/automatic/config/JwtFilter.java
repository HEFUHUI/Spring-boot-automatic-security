package com.mrhui.automatic.config;

import com.google.gson.Gson;
import com.mrhui.automatic.pojo.StandardResult;
import com.mrhui.automatic.shiro.JwtToken;
import com.mrhui.automatic.utils.JwtUtils;
import lombok.val;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.codehaus.groovy.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class JwtFilter extends BasicHttpAuthenticationFilter {

    @Autowired
    Gson gson;

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }

    @Override
    protected void postHandle(ServletRequest request, ServletResponse response) throws Exception {
        this.fillCorsHeader(WebUtils.toHttp(request),WebUtils.toHttp(response));
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if(this.isLoginRequest(request,response)) return true;
        boolean allowed = false;
        try {
            allowed = executeLogin(request,response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        final Subject subject = getSubject(request, response);
        return allowed || isPermissive(mappedValue);
    }

    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest httpServletRequest = (HttpServletRequest)request;
        String token = getQueryToken(httpServletRequest);
        return httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION) == null && token == null;
    }

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        AuthenticationToken authenticationToken = createToken(request, response);
        if(authenticationToken == null){
            throw new IllegalStateException("token为null");
        }
        try {
            Subject subject = getSubject(request, response);
            subject.login(authenticationToken); // 交给 Shiro 去进行登录验证
            return onLoginSuccess(authenticationToken, subject, request, response);
        } catch (AuthenticationException e) {
            return onLoginFailure(authenticationToken, e, request, response);
        }
    }

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        final String token = getQueryToken(httpServletRequest);
        if(token!=null){
            return new JwtToken(token);
        }
        return super.createToken(request, response);
    }

    private String getQueryToken(HttpServletRequest request){
        val queryString = request.getQueryString();
        String token = null;
        if(queryString!=null){
            final String[] split = queryString.split("token=");
            if(split.length > 1){
                token = split[1];
            }
        }
        return token;
    }

    @Override
    protected AuthenticationToken createToken(String username, String password, ServletRequest request, ServletResponse response) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        final String requestHeader = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        return new JwtToken(requestHeader);
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        final PrintWriter writer = httpServletResponse.getWriter();
        writer.write(gson.toJson(StandardResult.failed("无权限访问!",401)));
        fillCorsHeader(WebUtils.toHttp(request),httpServletResponse);
        return false;
    }

    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        HttpServletResponse servletResponse = WebUtils.toHttp(response);
        String newToken = null;
        if(token instanceof JwtToken){
            newToken = JwtUtils.refreshTokenExpired(token.getCredentials().toString());
        }
        if(newToken!=null){
            servletResponse.setHeader(JwtUtils.AUTH_HEADER, newToken);
        }
        return true;
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        return false;
    }

    protected void fillCorsHeader(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,HEAD");
        httpServletResponse.setHeader("Access-Control-Allow-Headers",
                httpServletRequest.getHeader("Access-Control-Request-Headers"));
    }
}
