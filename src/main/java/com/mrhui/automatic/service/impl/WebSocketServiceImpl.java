package com.mrhui.automatic.service.impl;

import com.google.gson.Gson;
import com.mrhui.automatic.entity.TUser;
import com.mrhui.automatic.entity.vo.UserVO;
import com.mrhui.automatic.pojo.*;
import com.mrhui.automatic.service.LoggingService;
import com.mrhui.automatic.service.TUserService;
import com.mrhui.automatic.service.WebSocketService;
import com.mrhui.automatic.utils.MD5;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.websocket.Session;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static com.mrhui.automatic.pojo.Common.*;

@Service
@Slf4j
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

    @Override
    public void onOpen(Session session, UserVO userVO, String sessionId) {
        websocketClients.add(session);
        if(userVO!=null){
            certifiedHashTable.put(sessionId,new WebsocketClient(session,userVO.getUser()));
            onlineUsers.add(userVO.getUser());
            broadWithUserType(gson.toJson(StandardResult.success("新用户登录",WS_UPDATE_INFO,userVO.getUser())),TYPE_ADMIN);
        }else{
            notCertifiedHashTable.put(session.getId(),new WebsocketClient(session,null));
            sendWithSession(StandardResult.failed("请提供身份信息！",WS_ERROR_USER_UNAUTHORIZED),session);
        }
    }

    @Override
    public void onClose(Session session, UserVO userVO, String sessionId) {
        websocketClients.remove(session);
        if(userVO!=null){
            certifiedHashTable.remove(sessionId);
            onlineUsers.remove(userVO.getUser());
            broadWithUserType(gson.toJson(StandardResult.success("用户离线",WS_LOGOUT,userVO.getUser())),TYPE_ADMIN);
        }else{
            notCertifiedHashTable.remove(session.getId());
        }
    }

    @Override
    public void onError(Session session, Throwable throwable) {
        // TODO: 2021/6/13 发生错误的处理
    }

    /**
     * 接收信息
     * 大致流程：
     * 如果用户信息为空的向管理员广播该匿名消息后直接返回
     *  @param session 会话信息
     * @param message 消息体
     * @param userVO 发送者用户信息
     * @param sessionId HTTP会话ID
     */
    @Override
    public void onMessage(Session session, String message, UserVO userVO, String sessionId) {
        if (userVO == null) {
            broadWithUserType("匿名用户发送消息："+message,TYPE_ADMIN);
            return;
        }
        TUser user = userVO.getUser();
        try {
            //解析JSON数据
            WebsocketReceive websocketReceive = gson.fromJson(message, WebsocketReceive.class);
            int code = websocketReceive.getCode();
           if (code >= WS_BROADCAST && code <= WS_GET_INFO) {
                if(user.getUserType()==0||user.getUserType()==2){
                    broadcast(websocketReceive,user);
                }else{
                    sendWithSession(StandardResult.failed("无权限！广播失败",WS_ERROR_USER_UNAUTHORIZED),session);
                }
            } else if(code >= WS_GET_INFO && code <= WS_SEND_MESSAGE){
                if(user.getUserType()==0||user.getUserType()==2){
                    //权限控制
                    getInfo(websocketReceive,session);
                }else{
                    sendWithSession(StandardResult.failed("无权限！获取失败",WS_ERROR_USER_UNAUTHORIZED),session);
                }
            }else if(code >= WS_SEND_MESSAGE && code <= WS_SEND_COMMEND){
                sendMessage(websocketReceive,user);
            }else if(code >= WS_SEND_COMMEND && code <= 500){
                if(user.getUserType() == 0 || user.getUserType()==2){
                    //权限控制
                    sendCommend(websocketReceive,user);
                }else{
                    sendWithSession(StandardResult.failed("无权限！发送失败",WS_ERROR_USER_UNAUTHORIZED),session);
                }
            }
        }catch (Exception e){
            log.error("用户{}发送的数据解析发生错误！",user.getUserId());
//            broadWithUserType(message,TYPE_ADMIN);
        }
        //Debug
        broadWithUserType(message,TYPE_ADMIN);
    }

    private void sendMessage(WebsocketReceive receive, TUser user) {
        int code = receive.getCode();
        if(code == WS_SEND_MESSAGE+1){
            log.info("receive.getData()={}",receive.getData());
           sendWithHttpSessionId(receive.getDestination(),StandardResult.success("有消息",WS_SEND_MESSAGE+11,receive.getData(),user));
        }
    }

    private void sendCommend(WebsocketReceive receive, TUser user) {
        val code = receive.getCode();
        sendWithHttpSessionId(receive.getDestination(), StandardResult.success("有命令",code,receive.getData()));
    }

    /**
     * 广播消息处理
     * @param receive 接收对象
     * @param user 发送者的信息
     */
    private void broadcast(WebsocketReceive receive,TUser user){
        val code = receive.getCode();
        try {
            if (code == WS_BROADCAST+1) {
                // 广播到所有管理员
                send_broadcast(receive, user,TYPE_ADMIN);
            }else if(code == WS_BROADCAST+2){
                // 广播到全部学生
                send_broadcast(receive, user, TYPE_STUDENT);
            }else if(code==WS_BROADCAST+3){
                // 广播到所有设备
                send_broadcast(receive, user, TYPE_HARDWARE);
            }else if(code==WS_BROADCAST+4){
                //广播到所有在线设备
                broadcast(StandardResult.success("收到消息",WS_BROADCAST,receive.getData(),user));
            }
        }catch (Exception e){
            sendWithHttpSessionId(user.getSessionId(),StandardResult.failed("服务器错误",WS_SERVER_ERROR));
        }
    }

    private void send_broadcast(WebsocketReceive receive, TUser user,int userType) {
        broadWithUserType(gson.toJson(StandardResult.success("收到消息", WS_BROADCAST, receive.getData(), user)), userType);
    }

    private void getInfo(WebsocketReceive receive,Session session) {
        val code = receive.getCode();
        try {
            if(code == WS_GET_INFO+1){
                //获取所有在线的管理员
                sendUserInfo(session,WS_GET_INFO + 11,TYPE_ADMIN);
            }else if(code == WS_GET_INFO+2){
                // 获取所有在线的学生
                sendUserInfo(session, WS_GET_INFO+12,TYPE_STUDENT);
            }else if(code == WS_GET_INFO+3){
                // 获取所有在线的设备
                sendUserInfo(session,  WS_GET_INFO+13,+TYPE_HARDWARE);
            }else if(code == WS_GET_INFO+4){
                // 获取所有在线的用户
                sendWithSession(StandardResult.success("获取成功",WS_GET_INFO+14,onlineUsers),session);
            }else if(code == WS_GET_INFO+5){
                //获取单个用户信息
                sendWithSession(StandardResult.success("获取成功",WS_GET_INFO+15,userService.findById(receive.getData().toString())),session);
            }else if(code == WS_GET_INFO+6){
                // 判断该用户是否在线
                isOnline(receive.getData().toString(),session);
            }
        }catch (Exception e){
            sendWithSession(StandardResult.failed("获取失败",WS_ERROR_GET_INFO_FAIL),session);
        }
    }

    private void sendUserInfo(Session session, int returnCode, int userType) {
        sendWithSession(StandardResult.success("获取信息", returnCode,getAllOnlineWithType(userType)), session);
    }
    private void isOnline(String userId, Session session){
        AtomicBoolean result = new AtomicBoolean(false);
        AtomicReference<TUser> tUser = new AtomicReference<>();
        onlineUsers.forEach(user -> {
            if(user.getUserId().equals(userId)){
                result.set(true);
                tUser.set(user);
            }
        });
        if(result.get()){
            sendWithSession(StandardResult.success("用户在线",WS_GET_INFO+16,tUser.get()),session);
        }else{
            sendWithSession(StandardResult.failed("用户离线",WS_GET_INFO+26),session);
        }

    }

    private List<TUser> getAllOnlineWithType(int userType){
        List<TUser> users = new LinkedList<>();
        for (TUser onlineUser : onlineUsers) {
            if(onlineUser.getUserType() == userType){
                users.add(onlineUser);
            }
        }
        return users;
    }

    @Override
    public void broadWithUserType(String message, int userType) {
        certifiedHashTable.forEach((key,websocketClient)->{
            if (websocketClient.getUser().getUserType() != userType) {
                return;
            }
            try {
                websocketClient.getSession().getAsyncRemote().sendText(message);
            }catch(Exception e){
                log.error("发送消息到用户 {} 失败：{}",websocketClient.getUser().getUserId(),e.getMessage());
            }
        });
    }

    /**
     * 广播
     * @param message String
     */
    @Override
    public void broadcast(Object message) {
        websocketClients.forEach(session -> session.getAsyncRemote().sendText(gson.toJson(message)));
    }


    @Override
    public void sendWithUserId(String userId,Object message) {
        certifiedHashTable.forEach(((s, websocketClient) -> {
            if(websocketClient.getUser().getUserId().equals(userId)){
                websocketClient.getSession().getAsyncRemote().sendText(gson.toJson(message));
            }
        }));
    }

    @Override
    public void sendWithHttpSessionId(String sessionId, Object message) {
        final WebsocketClient websocketClient = certifiedHashTable.get(sessionId);
        if(websocketClient!=null){
            if(websocketClient.getSession().isOpen()){
                websocketClient.getSession().getAsyncRemote().sendText(gson.toJson(message));
            }
        }
    }

    /**
     * 使用SessionId发送消息
     * @param message 消息体
     * @param sid Session ID
     */
    @Override
    public void sendWithSessionId(Object message, String sid) {
        websocketClients.forEach(session -> {
            if(session.getId().equals(sid)){
                session.getAsyncRemote().sendText(gson.toJson(message));
            }
        });
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
}
