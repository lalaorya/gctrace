package com.hhj.gctrace;

import com.hhj.gctrace.util.GCLogAnalysis;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class GctraceApplication {

    public static void main(String[] args) throws InterruptedException {

        SpringApplication.run(GctraceApplication.class, args);

        // 生成垃圾对象
        Thread.sleep(20000);
        GCLogAnalysis.gen();

    }

}
