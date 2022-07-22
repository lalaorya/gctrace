FROM openjdk:8-jdk-alpine

RUN mkdir /app
ADD gctrace-0.0.1-SNAPSHOT.jar /app

WORKDIR /app
EXPOSE 19001
ENV JAVA_OPTS = "-Xloggc:./gclog-fat.log\
                 -XX:+PrintGCDetails\
                 -XX:+UnlockExperimentalVMOptions\
                 -XX:+UseCGroupMemoryLimitForHeap\
                 -XX:+PrintGCApplicationStoppedTime\
                 -XX:+PrintGCDateStamps\
                 -XX:+PrintHeapAtGC\
                 -Dfile.encoding=UTF-8\
                 -Dsun.jnu.encoding=UTF-8\
                 -Dlog4j2.formatMsgNoLookups=tru\
                 -server"


#
#ENTRYPOINT java ${JAVA_OPTS} -jar gctrace-0.0.1-SNAPSHOT.jar com.hhj.gctrace.GctraceApplication

#/app # java -jar gctrace-0.0.1-SNAPSHOT.jar -Xloggc:/app/log/gc.log -XX:+PrintGCDetails -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -XX:+PrintGCApplicationStoppedTime -XX:+PrintGCDat
#eStamps -XX:+PrintHeapAtGC -Dfile.encoding=UTF-8 -Dsun.jnu.encoding=UTF-8 -Dlog4j2.formatMsgNoLookups=true -server


# 启动命令：
#java -Xloggc:/app/log/gc.log -XX:+PrintGCDetails -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -XX:+PrintGCApplicationStoppedTime -XX:+PrintGCDateStamps -XX:+PrintHeapAtGC -Dfil
#e.encoding=UTF-8 -Dsun.jnu.encoding=UTF-8 -Dlog4j2.formatMsgNoLookups=true -server -jar ./gctrace-0.0.1-SNAPSHOT.jar