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

    private final LoggingService loggingService = SpringUtil.getBean(LoggingService.class);

    private final WebSocketServiceImpl webSocketService = SpringUtil.getBean(WebSocketServiceImpl.class);

    private final Gson gson = SpringUtil.getBean(Gson.class);

    private String buf;

    private final IdWorker idWorker = SpringUtil.getBean(IdWorker.class);

    private final TUserService userService= SpringUtil.getBean(TUserService.class);

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
        log.info("UserVO={}",userVO);
        if (userVO == null) {
            TUser user = userService.findByUserName(username);
            if (webSocketService.login(username, password, session)) {
                webSocketService.sendWithSession(gson.toJson(StandardResult.success("登录成功!")), session);
                assert user != null;
                //打印登陆成功日志
                loggingService.login(true,user.getUserId(),"登录成功!");
            } else {
                if(user!=null){
//                    打印登录失败日志
                    loggingService.login(false,user.getUserId(),"登录失败!");
                }
                webSocketService.sendWithSession(gson.toJson(StandardResult.failed("用户名或者密码错误！")), session);
                if (session.isOpen()) {
                    session.close();
                }
            }
        } else {
            //保存用户信息
            webSocketService.addClient(session.getId(), new WebsocketClient(session, userVO.getUser()));
        }
    }

    /**
     * 客户端关闭
     *
     * @param session session
     */
    @OnClose
    public void onClose(Session session) {
        log.info("有用户断开了, id为:{}", session.getId());
        webSocketService.logout(session.getId());
    }

    /**
     * 发生错误
     *
     * @param throwable e
     */
    @OnError
    public void onError(Throwable throwable, Session session) {
        webSocketService.logout(session.getId());
        log.error("用户ID为{}发生错误！：{}", session.getId(), throwable.getMessage());
    }

    /**
     * 收到客户端发来消息
     *
     * @param message 消息对象
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        String userId = WebSocketService.dbAndSessionIdMapper.get(session.getId());
        //获取到当前发送消息的用户用户信息
        TUser current_user = WebSocketService.clients.get(userId).getUser();
        try {
            WebsocketReceive result = new WebsocketReceive();
            try {
                result = gson.fromJson(message, WebsocketReceive.class);
            } catch (Exception e) {
                log.error("WebsocketReceive 解析错误！=>{}", e.getMessage());
            }
            switch (result.getCode()) {
                case 0:
                    // 发生错误
                    break;
                case 1:
                    String imgName = idWorker.nextId() + ".png";
                    if (result.getIsPage() == 1) {
                        if (result.getCurrent() == 1) {
                            buf = (String) result.getData();
                        } else if (result.getCurrent() == result.getTotal()) {
                            buf += result.getData();
                            webSocketService.decodeBase64ToImage(buf, imgName);
                        } else {
                            buf += result.getData();
                        }
                    } else {
                        webSocketService.decodeBase64ToImage((String) result.getData(), imgName);
                    }
                    webSocketService.sendWithSession(StandardResult.success("上传成功！", imgName), session);
                    break;
                case WS_COMMAND://发送命令
                    LinkedTreeMap<String, Object> command = (LinkedTreeMap<String, Object>) result.getData();
                    if (webSocketService.sendCommand((String) command.get("destination"), command.get("data"))) {
                        webSocketService.sendWithSession(StandardResult.success(), session);
                    } else {
                        webSocketService.sendWithSession(StandardResult.failed(), session);
                    }
                    break;
                case WS_GET_INFO://在线用户信息更新，客户端主动请求
                    LinkedTreeMap<String, Object> data = (LinkedTreeMap<String, Object>) result.getData();
                    //请求所有
                    if(data.get("type").equals("all")){
                        webSocketService.sendAllAdmin(gson.toJson(StandardResult.success("获取成功！", WS_GET_INFO +1, WebSocketService.dbAndSessionIdMapper)));
                    }
                    //请求单个用户信息
                    if(data.get("type").equals("user")){
                        String id = (String) data.get("id");
                        final TUser byId = userService.findById(id);
                        webSocketService.sendAllAdmin(gson.toJson(StandardResult.success("获取成功！", WS_GET_INFO +1,byId)));
                    }
                    break;
                case WS_SEND_MESSAGE:
                    LinkedTreeMap<String, Object> resultData = (LinkedTreeMap<String, Object>) result.getData();
                    webSocketService.sendWithUserId((String) resultData.get("destination"),StandardResult.success("有消息来!", WS_SEND_MESSAGE +1,resultData.get("data"),current_user));
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            log.error("数据解析发生问题了!");
        }
        log.info("服务端收到客户端发来的消息: {}", message);
        webSocketService.sendAllAdmin(message);
    }
}