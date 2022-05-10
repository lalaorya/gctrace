package com.hhj.gctrace.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hhj.gctrace.pojo.dto.GCMessageDTO;
import com.sun.media.jfxmedia.logging.Logger;
import org.springframework.stereotype.Service;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * @Author virtual
 * @Date 2022/5/10 16:43
 * @description：
 */
@Service
@ServerEndpoint("/ws")
public class WebSocketService {

    /**
     *  concurrent包的线程安全Set，用来存放每个客户端对应的WebSocketServer对象
     */
    private static ConcurrentSkipListSet<Session> sessionPools = new ConcurrentSkipListSet<>();

    // 建立连接成功调用
    @OnOpen
    public void onOpen(Session session) {
        // 链接建立，存储链接对象
        sessionPools.add(session);
    }

    // 关闭连接时调用
    @OnClose
    public void onClose(Session session) {
        sessionPools.remove(session);
    }

    // 收到客户端信息
    @OnMessage
    public void onMessage(String message) throws IOException {
        // 客户端不会发送消息
    }

    // 错误时调用
    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }


    // 群发消息
    public void broadcast(GCMessageDTO message) {
        for (Session session : sessionPools) {
            try {
                sendMessage(session, message);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }
    }

    // 发送消息
    public void sendMessage(Session session, GCMessageDTO message) throws IOException {
        if (session != null) {
            synchronized (session) {
                session.getBasicRemote().sendText(JSONObject.toJSONString(message));
            }
        }
    }

    public static ConcurrentSkipListSet<Session> getSessionPools() {
        return sessionPools;
    }


}
