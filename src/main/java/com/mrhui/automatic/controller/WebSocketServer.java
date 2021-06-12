package com.mrhui.automatic.controller;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.mrhui.automatic.config.SpringUtil;
import com.mrhui.automatic.config.WebSocketConfig;
import com.mrhui.automatic.entity.TUser;
import com.mrhui.automatic.entity.vo.UserVO;
import com.mrhui.automatic.pojo.StandardResult;
import com.mrhui.automatic.pojo.WebsocketClient;
import com.mrhui.automatic.pojo.WebsocketReceive;
import com.mrhui.automatic.service.LoggingService;
import com.mrhui.automatic.service.TUserService;
import com.mrhui.automatic.service.WebSocketService;
import com.mrhui.automatic.service.impl.WebSocketServiceImpl;
import com.mrhui.automatic.utils.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

import static com.mrhui.automatic.pojo.Common.*;

@Component
@ServerEndpoint(value = "/websocket/{username}/{password}", configurator = WebSocketConfig.class)
@Slf4j
public class WebSocketServer {

    private final WebSocketServiceImpl webSocketService = SpringUtil.getBean(WebSocketServiceImpl.class);

    /**
     * 连接 用户登录
     *
     * @param session  Session
     * @param username String
     * @param password String
     * @throws IOException e
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username, @PathParam("password") String password) throws IOException {
        UserVO userVO = (UserVO) session.getUserProperties().get("user");
    }

    /**
     * 客户端关闭
     *
     * @param session session
     */
    @OnClose
    public void onClose(Session session) {
        log.info("有用户断开了, id为:{}", session.getId());
    }

    /**
     * 发生错误
     *
     * @param throwable e
     */
    @OnError
    public void onError(Throwable throwable, Session session) {
        log.error("用户ID为{}发生错误！：{}", session.getId(), throwable.getMessage());
    }

    /**
     * 收到客户端发来消息
     *
     * @param message 消息对象
     */
    @OnMessage
    public void onMessage(String message, Session session) {

    }
}