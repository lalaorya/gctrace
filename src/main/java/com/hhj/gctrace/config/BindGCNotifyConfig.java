package com.hhj.gctrace.config;

/**
 * @Author virtual
 * @Date 2022/5/9 17:56
 * @description：
 */

import com.alibaba.fastjson.JSON;
import com.hhj.gctrace.pojo.dto.GCMessageDTO;
import com.hhj.gctrace.service.GCMessageService;
import com.hhj.gctrace.service.WebSocketService;
import com.sun.management.GarbageCollectionNotificationInfo;
import com.sun.management.GcInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.management.Notification;
import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;
import java.lang.management.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 使用Notification监听GC日志 并持久化到 influxDB
 * JVM 启动参数:
 *   -Xmx500m
 *   -Xms500m
 *   -XX:+UseConcMarkSweepGC
 *   -XX:+UseParNewGC
 *   -Xloggc:./logs/gc.log
 *   -XX:+PrintGCDetails
 *   -XX:+PrintGCDateStamps
 *   -XX:+PrintGCApplicationConcurrentTime
 *   -XX:+PrintGCApplicationStoppedTime
 */
@Configuration
public class  BindGCNotifyConfig {

    @Autowired
    WebSocketService webSocketService;

    @Autowired
    GCMessageService messageService;

    public BindGCNotifyConfig() {
    }

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final AtomicBoolean inited = new AtomicBoolean(Boolean.FALSE);
    private final AtomicLong maxPauseMillis = new AtomicLong(0L);
    // 记录服务器启动时间
    private final Long serverStartTime = System.currentTimeMillis();

    @PostConstruct
    public void init() {
        try {
            doInit();
        } catch (Throwable e) {
            logger.warn("[GC 日志监听-初始化]失败! ", e);
        }
    }


    private void doInit() {
        if (!inited.compareAndSet(Boolean.FALSE, Boolean.TRUE)) {
            return;
        }
        int count = 0;
        // 每个 mbean 都注册监听
        for (GarbageCollectorMXBean mbean : ManagementFactory.getGarbageCollectorMXBeans()) {
            if (!(mbean instanceof NotificationEmitter)) {
                continue;
            }

            final NotificationEmitter notificationEmitter = (NotificationEmitter) mbean;
            // 添加监听
            final NotificationListener notificationListener = getNewListener(mbean);
            notificationEmitter.addNotificationListener(notificationListener, null, null);

            logger.info("[GC 日志监听-初始化]MemoryPoolNames=" + JSON.toJSONString(mbean.getMemoryPoolNames()));

        }
    }

    private NotificationListener getNewListener(final GarbageCollectorMXBean mbean) {

        final NotificationListener listener = new NotificationListener() {
            // 如果Mbean发出事件通知会回调该函数
            @Override
            public void handleNotification(Notification notification, Object ref) {
                // 只处理 GC 事件
                if (!notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
                    return;
                }

                GarbageCollectionNotificationInfo notificationInfo = GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());

                // 通过notificationInfo获取GC相关信息
                String gcName = notificationInfo.getGcName();
                // 事件触发原因
                String gcAction = notificationInfo.getGcAction();
                // GC发生原因
                String gcCause = notificationInfo.getGcCause();
                GcInfo gcInfo = notificationInfo.getGcInfo();
                // duration 是指 Pause 阶段的总停顿时间
                long duration = gcInfo.getDuration();
                Long startTime = gcInfo.getStartTime() + serverStartTime;
                Long endTime = gcInfo.getEndTime() + serverStartTime;

                if (maxPauseMillis.longValue() < duration) {
                    // 保证线程安全
                    maxPauseMillis.set(duration);
                }
                long gcId = gcInfo.getId();

                String type = "jvm.gc.pause";
                // 只处理pause事件
                if ("No GC".equals(gcCause)) {
                    return;
                }

                // 构造信息
                GCMessageDTO message = GCMessageDTO.builder()
                        .gcName(gcName)
                        .gcAction(gcAction)
                        .gcCause(gcCause)
                        .startTime(startTime)
                        .endTime(endTime)
                        .pauseMillis(duration)
                        .maxPauseMillis(maxPauseMillis.longValue())
                        .type(type)
                        .build();


                logger.info("[GC 日志监听-GC 事件]gcId={}; gcDetail: {}", gcId, message);
                // 通过webSocket群发给连接的用户
                webSocketService.broadcast(message);
                // 持久化
                messageService.write(message);
//                messageService.read()


            }
        };

        return listener;
    }


}
