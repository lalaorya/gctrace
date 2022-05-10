package com.hhj.gctrace;

import com.hhj.gctrace.service.WriteTestService;
import com.hhj.gctrace.util.GCLogAnalysis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.List;

@SpringBootApplication
public class GctraceApplication {

    public static void main(String[] args) {

        SpringApplication.run(GctraceApplication.class, args);

        // 生成垃圾对象
        GCLogAnalysis.gen();

    }

}
