package com.mrhui.automatic.service;

import com.mrhui.automatic.pojo.StandardResult;
import com.mrhui.automatic.pojo.WebsocketClient;
import org.springframework.stereotype.Service;

import javax.websocket.Session;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Gjing
 **/
@Service
public interface WebSocketService {
    void sendAllAdmin(String message);
    void sendAllEqu(String message);
    void sendAll(String message);
    void sendStudent(String message);
    void sendWithUserId(String userId,Object message);
    void sendWithSessionId(String message, String sid);
    void sendWithSession(Object message,Session session);
    void decodeBase64ToImage(String base64, String imgName);
    void sendCommand(String destination, Object data);
}
