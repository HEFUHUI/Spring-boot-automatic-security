package com.mrhui.automatic.service;

import com.mrhui.automatic.entity.TUser;
import com.mrhui.automatic.entity.vo.UserVO;
import com.mrhui.automatic.pojo.WebsocketClient;
import org.springframework.stereotype.Service;

import javax.websocket.Session;
import java.util.Hashtable;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Gjing
 **/
@Service
public interface WebSocketService {
    public static Hashtable<String,WebsocketClient> certifiedHashTable = new Hashtable<>();
    public static Hashtable<String,WebsocketClient> notCertifiedHashTable = new Hashtable<>();
    public static CopyOnWriteArrayList<Session> websocketClients = new CopyOnWriteArrayList<>();
    public static CopyOnWriteArrayList<TUser> onlineUsers = new CopyOnWriteArrayList<>();

    void onOpen(Session session, UserVO userVO, String sessionId);
    void onClose(Session session, UserVO userVO, String sessionId);
    void onError(Session session,Throwable throwable);
    void onMessage(Session session, String message, UserVO userVO, String sessionId);
    void broadWithUserType(String message, int userType);
    void broadcast(Object message);
    void sendWithUserId(String userId,Object message);
    void sendWithHttpSessionId(String sessionId,Object message);
    void sendWithSessionId(Object message, String sid);
    void sendWithSession(Object message,Session session);
    void decodeBase64ToImage(String base64, String imgName);
}
