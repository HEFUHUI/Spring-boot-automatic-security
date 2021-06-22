package com.mrhui.automatic.controller;

import com.mrhui.automatic.config.SpringUtil;
import com.mrhui.automatic.config.WebSocketConfig;
import com.mrhui.automatic.entity.vo.UserVO;
import com.mrhui.automatic.service.impl.WebSocketServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@Component
@ServerEndpoint(value = "/ws", configurator = WebSocketConfig.class)
@Slf4j
public class WebSocketServer {

    private final WebSocketServiceImpl webSocketService = SpringUtil.getBean(WebSocketServiceImpl.class);

    /**
     * 连接 用户登录
     *
     * @param session  Session
     * @throws IOException e
     */
    @OnOpen
    public void onOpen(Session session) throws IOException {
        UserVO userVO = (UserVO) session.getUserProperties().get("user");
        String sessionId = (String) session.getUserProperties().get("sessionId");
        log.info("用户已连接={}",session.getId());
        webSocketService.onOpen(session,userVO,sessionId);
    }

    /**
     * 客户端关闭
     *
     * @param session session
     */
    @OnClose
    public void onClose(Session session) {
        UserVO userVO = (UserVO) session.getUserProperties().get("user");
        String sessionId = (String) session.getUserProperties().get("sessionId");
        log.info("有用户断开了, id为:{}", session.getId());
        webSocketService.onClose(session,userVO,sessionId);
    }

    /**
     * 发生错误
     *
     * @param throwable e
     */
    @OnError
    public void onError(Throwable throwable, Session session) {
        log.error("用户ID为{}发生错误！：{}", session.getId(), throwable.getMessage());
        webSocketService.onError(session, throwable);
    }

    /**
     * 收到客户端发来消息
     *
     * @param message 消息对象
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        UserVO userVO = (UserVO) session.getUserProperties().get("user");
        String sessionId = (String) session.getUserProperties().get("sessionId");
        log.info("收到用户{}的消息：{}",session.getId(),message);
        webSocketService.onMessage(session, message,userVO,sessionId);
    }
}