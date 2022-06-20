package com.hhj.gctrace.util;

import com.microsoft.gctoolkit.GCToolKit;
import com.microsoft.gctoolkit.io.SingleGCLogFile;
import com.microsoft.gctoolkit.jvm.JavaVirtualMachine;
import com.microsoft.gctoolkit.sample.aggregation.HeapOccupancyAfterCollectionSummary;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class GCToolKitExample {

    public static void test() throws IOException {
        // 获取gcFile
        SingleGCLogFile logFile = new SingleGCLogFile(Paths.get("./logs/gclog2.log"));
        List<String> collect = logFile.stream().collect(Collectors.toList());
        for (String s:collect){
            System.out.println(s);
        }


        System.out.println(logFile.getMetaData().logFiles().toString());

        GCToolKit gcToolKit = new GCToolKit();

        gcToolKit.loadAggregationsFromServiceLoader();

        JavaVirtualMachine machine = gcToolKit.analyze(logFile);

        Optional<HeapOccupancyAfterCollectionSummary> result = machine.getAggregation(HeapOccupancyAfterCollectionSummary.class);

        System.out.println(result);


        System.out.println(machine.toString());


    }

    public static void main(String[] args) throws IOException {
        test();
    }
}
