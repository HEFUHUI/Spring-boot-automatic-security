package com.mrhui.automatic.service.impl;

import com.google.gson.Gson;
import com.mrhui.automatic.entity.TUser;
import com.mrhui.automatic.pojo.Page;
import com.mrhui.automatic.pojo.StandardResult;
import com.mrhui.automatic.pojo.ProjectConfig;
import com.mrhui.automatic.pojo.WebsocketClient;
import com.mrhui.automatic.service.LoggingService;
import com.mrhui.automatic.service.TUserService;
import com.mrhui.automatic.service.WebSocketService;
import com.mrhui.automatic.utils.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.websocket.Session;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static com.mrhui.automatic.pojo.Common.*;

@Service
public class WebSocketServiceImpl implements WebSocketService {

    @Autowired
    private TUserService userService;

    @Autowired
    private MD5 md5;

    @Autowired
    ProjectConfig projectConfig;

    @Autowired
    LoggingService loggingService;

    @Autowired
    Gson gson;

    /**
     * 登录
     * @param username 用户名String
     * @param password 密码String
     * @param session Session 会话对象
     * @return Boolean
     */
    @Override
    public boolean login(String username, String password, Session session) {
        Map<String,Object> map = new HashMap<>();
        //查出用户名
        map.put("login_name",username);
        final Page<TUser> userPage = userService.findByQuery(map, null);
        if (userPage.getItems().size() != 0) {
            //获取当前用户信息
            TUser user = userPage.getItems().get(0);
            // 验证密码匹配
            if (md5.match(password,user.getPassword(),username)) {
                WebsocketClient client = new WebsocketClient();
                client.setSession(session);
                client.setUser(user);
                //内存中存放用户信息和会话
                addClient(session.getId(),client);
                return true;
            }
        }
        return false;
    }


    /**
     * 登出
     * @param sid 会话ID
     */
    @Override
    public void logout(String sid) {
        removeClient(sid);
    }

    /**
     * 添加用户到在线用户列表
     * @param sId 会话ID
     * @param client client对象
     */
    @Override
    public void addClient(String sId, WebsocketClient client) {
        // 保存用户信息、回话信息。
        clients.put(client.getUser().getUserId(),client);
        // 保存用户的回话ID到SessionId的映射
        dbAndSessionIdMapper.put(sId,client.getUser().getUserId());
//        client.getUser().setLoginIp("127.0.0.1");
        client.getUser().setSessionId(sId);
        client.getUser().setOnline(true);
        userService.update(client.getUser(),client.getUser().getUserId());
        sendAllAdmin(gson.toJson(StandardResult.success("用户数据更新！", WS_GET_INFO +1,dbAndSessionIdMapper)));
    }

    /**
     * 移出用户于在线用户列表
     * @param sId 会话ID
     */
    @Override
    public void removeClient(String sId) {
        // 通过sessionId获取到用户的ID
        String userId = dbAndSessionIdMapper.get(sId);
        System.out.println(dbAndSessionIdMapper);
        dbAndSessionIdMapper.remove(sId);
        //如果为获取到ID或者ID为空
        if(userId == null || userId.isEmpty()){
            return;
        }
        // 通过用户ID获取到用户信息和Session
        WebsocketClient client = clients.get(userId);
        if(client!=null){
            TUser user = client.getUser();
            sendAllAdmin(gson.toJson(StandardResult.success("设备离线!", WS_LOGOUT,client)));
            user.setLoginIp("0.0.0.0");
            user.setSessionId("");
            user.setOnline(false);
            userService.update(user,user.getUserId());
            if(user.getUserType()==2){
                loggingService.logout(true,user.getUserId(),"退出登录！");
            }
            clients.remove(userId);
        }
    }

    /**
     * 通过UserId获取用户的会话和信息
     * @param userId String 用户ID
     * @return WebsocketClient
     */
    @Override
    public WebsocketClient getClientByUserId(String userId) {
        return clients.get(userId);
    }

    /**
     * 发送给所有管理员
     * @param message String
     */
    @Override
    public void sendAllAdmin(String message) {
        for (Map.Entry<String, WebsocketClient> sessionEntry : WebSocketService.clients.entrySet()) {
            if (sessionEntry.getValue().getSession().isOpen()
                    && sessionEntry.getValue().getUser().getUserType() == 0) {
                sessionEntry.getValue().getSession().getAsyncRemote().sendText(message);
            }
        }
    }

    /**
     * 发送给所有设备
     * @param message String
     */
    @Override
    public void sendAllEqu(String message) {
        for (Map.Entry<String, WebsocketClient> sessionEntry : WebSocketService.clients.entrySet()) {
            if (sessionEntry.getValue().getSession().isOpen()
                    && sessionEntry.getValue().getUser().getUserType() == 2) {
                sessionEntry.getValue().getSession().getAsyncRemote().sendText(message);
            }
        }
    }

    /**
     * 广播
     * @param message String
     */
    @Override
    public void sendAll(String message) {
        for (Map.Entry<String, WebsocketClient> sessionEntry : WebSocketService.clients.entrySet()) {
            if(sessionEntry.getValue().getSession().isOpen()){
                sessionEntry.getValue().getSession().getAsyncRemote().sendText(message);
            }
        }
    }

    /**
     * 发送给所有学生
     * @param message 消息体 String
     */
    @Override
    public void sendStudent(String message) {
        for (Map.Entry<String, WebsocketClient> sessionEntry : WebSocketService.clients.entrySet()) {
            if (sessionEntry.getValue().getSession().isOpen()
                    && sessionEntry.getValue().getUser().getUserType() == 1) {
                sessionEntry.getValue().getSession().getAsyncRemote().sendText(message);
            }
        }
    }

    @Override
    public void sendWithUserId(String userId,Object message) {
        final WebsocketClient client = clients.get(userId);
        if(client== null){
            return;
        }
        sendWithSession(message,client.getSession());
    }

    /**
     * 使用SessionId发送消息
     * @param message 消息体
     * @param sid Session ID
     * @return 标准返回
     */
    @Override
    public StandardResult<String> sendWithSessionId(String message, String sid) {
        // 获取用户ID
        String userId = dbAndSessionIdMapper.get(sid);
        // 如果没有获取到用户ID，则跳过
        if(!userId.isEmpty()) {
            // 如果会话已断开，则跳过
            if (clients.get(userId) != null && clients.get(sid).getSession().isOpen()) {
                //发送消息给获取到的Session对象
                clients.get(userId).getSession().getAsyncRemote().sendText(message);
                return StandardResult.success();
            }
        }
        return StandardResult.failed("该设备已断开连接！");
    }

    /**
     * 使用Session对象发送消息
     * @param message 消息体
     * @param session Session对象
     */
    @Override
    public void sendWithSession(Object message, Session session) {
        if (session.isOpen()) {
            session.getAsyncRemote().sendText(gson.toJson(message));
        }
    }

    /**
     * 解码Base64到本地图片文件
     * @param base64 Base64 String
     * @param imgName String 文件名称
     */
    @Override
    public void decodeBase64ToImage(String base64, String imgName) {
        // 获得decoder对象
        Base64.Decoder decoder = Base64.getDecoder();
        try {
            // 创建写入文件流
            FileOutputStream write = new FileOutputStream(projectConfig.getLocation()+"/"
                    + imgName);
            //解码base64得到byte[]
            byte[] decoderBytes = decoder.decode(base64);
//            写入到文件
            write.write(decoderBytes);
            //关闭文件流
            write.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送命令 ----- 到设备
     * @param destination String 目标设备UserId
     * @param data 携带数据 Object
     * @return Boolean
     */
    @Override
    public boolean sendCommand(String destination, Object data) {
        final WebsocketClient client = clients.get(destination);
        //如果为空，表示已断开连接
        if(client == null){
            return false;
        }
        // 获取到Session对象
        final Session session = client.getSession();
        // 如果为空或者未开启，表示已断开连接
        if (session != null && session.isOpen()) {
            sendWithSession(data,session);
            return true;
        }else{
            return false;
        }
    }
}
