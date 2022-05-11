package com.hhj.gctrace.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hhj.gctrace.pojo.dto.GCMessageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

import java.util.concurrent.CopyOnWriteArraySet;

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
    private static CopyOnWriteArraySet<WebSocketService> sessionPools = new CopyOnWriteArraySet<>();

    private Session session;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    // 建立连接成功调用
    @OnOpen
    public void onOpen(Session session) {
        // 链接建立，存储链接对象
        this.session = session;
        sessionPools.add(this);
    }

    // 关闭连接时调用
    @OnClose
    public void onClose(Session session) {
        sessionPools.remove(this);
        logger.info("客户端{}关闭",session);
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
        for (WebSocketService service : sessionPools) {
            try {
                service.sendMessage(service.session, message);
//                sendMessage(session, message);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }
    }

    // 发送消息
    public void sendMessage(Session session, GCMessageDTO message) throws IOException {
        if (session != null && session.isOpen()) {
            synchronized (session) {
                logger.info("发送给{}：{}",session,message);
                session.getBasicRemote().sendText(JSONObject.toJSONString(message));
            }
        }
    }



}
