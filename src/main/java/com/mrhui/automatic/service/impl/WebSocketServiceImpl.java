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
     * 发送给所有管理员
     * @param message String
     */
    @Override
    public void sendAllAdmin(String message) {

    }

    /**
     * 发送给所有设备
     * @param message String
     */
    @Override
    public void sendAllEqu(String message) {
    }

    /**
     * 广播
     * @param message String
     */
    @Override
    public void sendAll(String message) {
    }

    /**
     * 发送给所有学生
     * @param message 消息体 String
     */
    @Override
    public void sendStudent(String message) {
    }

    @Override
    public void sendWithUserId(String userId,Object message) {
    }

    /**
     * 使用SessionId发送消息
     * @param message 消息体
     * @param sid Session ID
     */
    @Override
    public void sendWithSessionId(String message, String sid) {
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
     */
    @Override
    public void sendCommand(String destination, Object data) {
    }
}
