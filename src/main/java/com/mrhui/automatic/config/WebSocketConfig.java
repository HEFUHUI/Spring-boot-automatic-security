package com.mrhui.automatic.config;

import com.mrhui.automatic.controller.AuthController;
import com.mrhui.automatic.entity.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

@Configuration
@Slf4j
public class WebSocketConfig extends ServerEndpointConfig.Configurator{
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    /**
     * 获取用户信息
     * @param sec ServerEndpointConfig
     * @param request HandshakeRequest
     * @param response HandshakeResponse
     */
    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        try {
            // 取出Session中的用户信息
            sec.getUserProperties().put("user", SecurityUtils.getSubject().getPrincipal());
            sec.getUserProperties().put("sessionId", SecurityUtils.getSubject().getSession().getId());
        }catch (Exception e){
            log.warn("用户未认证");
        }
        super.modifyHandshake(sec, request, response);
    }
}
