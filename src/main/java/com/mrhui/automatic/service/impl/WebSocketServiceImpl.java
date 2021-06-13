package com.mrhui.automatic.service.impl;

import com.google.gson.Gson;
import com.mrhui.automatic.entity.TUser;
import com.mrhui.automatic.entity.vo.UserVO;
import com.mrhui.automatic.pojo.ProjectConfig;
import com.mrhui.automatic.pojo.StandardResult;
import com.mrhui.automatic.pojo.WebsocketClient;
import com.mrhui.automatic.pojo.WebsocketReceive;
import com.mrhui.automatic.service.LoggingService;
import com.mrhui.automatic.service.TUserService;
import com.mrhui.automatic.service.WebSocketService;
import com.mrhui.automatic.utils.MD5;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.websocket.Session;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Hashtable;
import java.util.concurrent.CopyOnWriteArrayList;

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
    public void onOpen(Session session, UserVO userVO) {
        websocketClients.add(session);
        if(userVO!=null){
            certifiedHashTable.put(userVO.getUser().getUserId(),new WebsocketClient(session,userVO.getUser()));
            onlineUsers.add(userVO.getUser());
        }else{
            notCertifiedHashTable.put(session.getId(),new WebsocketClient(session,null));
            sendWithSession(StandardResult.failed("请提供身份信息！"),session);
        }
    }

    @Override
    public void onClose(Session session,UserVO userVO) {
        websocketClients.remove(session);
        if(userVO!=null){
            certifiedHashTable.remove(userVO.getUser().getUserId());
            onlineUsers.remove(userVO.getUser());
        }else{
            notCertifiedHashTable.remove(session.getId());
        }
    }

    @Override
    public void onError(Session session, Throwable throwable) { }

    @Override
    public void onMessage(Session session, String message,UserVO userVO) {
        final WebsocketReceive websocketReceive = gson.fromJson(message, WebsocketReceive.class);
        log.info("websocketReceive={}",websocketReceive);
        if (websocketReceive.getCode() == WS_BROADCAST) {
            websocketClients.forEach(sion->sion.getAsyncRemote().sendText(message));
        } else {
            log.info("WS：{}", message);
        }
    }

    @Override
    public void broadWithUserType(Object message, int userType) {
        certifiedHashTable.forEach((key,websocketClient)->{
            if(websocketClient.getUser().getUserType() == userType){
                try {
                    websocketClient.getSession().getAsyncRemote().sendText(gson.toJson(message));
                }catch(Exception e){
                    log.error("发送消息到用户 {} 失败：{}",websocketClient.getUser().getUserId(),e.getMessage());
                }
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
        final WebsocketClient websocketClient = certifiedHashTable.get(userId);
        if (websocketClient!=null) {
            websocketClient.getSession().getAsyncRemote().sendText(gson.toJson(message));
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
