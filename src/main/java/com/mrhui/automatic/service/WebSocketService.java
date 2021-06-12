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
    boolean login(String username, String password, Session session);
    void logout(String sid);
    public static Map<String, WebsocketClient> clients = new ConcurrentHashMap<>();
    public static Map<String,String> dbAndSessionIdMapper = new HashMap<>();
    void addClient(String sId,WebsocketClient client);
    void removeClient(String sId);
    WebsocketClient getClientByUserId(String sId);
    void sendAllAdmin(String message);
    void sendAllEqu(String message);
    void sendAll(String message);
    void sendStudent(String message);
    void sendWithUserId(String userId,Object message);
    StandardResult<String> sendWithSessionId(String message, String sid);
    void sendWithSession(Object message,Session session);
    void decodeBase64ToImage(String base64, String imgName);
    boolean sendCommand(String destination, Object data);
}
