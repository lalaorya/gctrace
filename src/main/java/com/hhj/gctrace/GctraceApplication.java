package com.hhj.gctrace;

import com.hhj.gctrace.util.GCLogAnalysis;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.*;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@SpringBootApplication
public class GctraceApplication {

    public static void main(String[] args) throws Exception {

        SpringApplication.run(GctraceApplication.class, args);
        new Thread(()->{
            GCLogAnalysis.gen();
        }).run();

        // 生成垃圾对象
        Thread.sleep(20000);
        new Thread(()->{
            File file;
            try {
                BufferedReader bufferedReader;
                while(true){
                    file =new File("./logs/gc-test.log");
                    bufferedReader = new BufferedReader(new FileReader(file));
                    List<String> collect = bufferedReader.lines().collect(Collectors.toList());

                    System.out.println(collect.size());
                    System.out.println("-----------------p----------------");
                    Thread.sleep(10);
                }


            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).run();


//        GCLogAnalysis.gen();

    }

}
