package com.hhj.gctrace.service.impl;

import com.hhj.gctrace.pojo.dto.GCMessageDTO;
import com.hhj.gctrace.service.GCMessageService;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.Query;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import com.influxdb.query.dsl.Flux;
import com.influxdb.query.dsl.functions.LimitFlux;
import com.influxdb.query.dsl.functions.restriction.Restrictions;
import com.influxdb.query.internal.FluxResultMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @Author virtual
 * @Date 2022/5/11 11:33
 * @description：
 */
@Service
public class GCMessageServiceImpl implements GCMessageService {


    @Value("${INFLUXDB_BUCKET}")
    String bucket;

    @Value("${server.port}")
    String port;



    @Autowired
    InfluxDBClient client;



    /**
     * 将message存储进influxDB
     * 存储规范：每个实例的GC表是独立的，表名：log_ip_port
     * @param messageDTO
     */
    @Override
    public void write(GCMessageDTO messageDTO) {
        // 创建Point
        Point point = Point.measurement("log_127.0.0.1_" + port)
                .addField("startTime", messageDTO.getStartTime())
                .addField("endTime", messageDTO.getEndTime())
                .addField("pauseMillis", messageDTO.getPauseMillis())
                .addField("maxPauseMillis", messageDTO.getMaxPauseMillis())
                .addField("gcCause", messageDTO.getGcCause())
                .addField("gcAction", messageDTO.getGcAction())
                .addField("gcName", messageDTO.getGcName())
                .addField("type", messageDTO.getType());

        WriteApiBlocking writeApiBlocking = client.getWriteApiBlocking();
        // 写入精度为毫秒
        writeApiBlocking.writePoint(point);
    }

    @Override
    public List<GCMessageDTO> read(int time) {

        // 读取的数据库
        // todo：IP
        String measurement = "log_127.0.0.1_" + port;

//        String flux = "from(bucket: \"" + bucket  + "\")  |> filter(fn: (r) => r._measurement == \"" + measurement + "\")";
        // 使用flux来构造influxSQL
        Flux limit = Flux.from(bucket)
                // 只读取过去5min的数据
                .range(-5L, ChronoUnit.MINUTES)
                .filter(Restrictions.and(Restrictions.measurement().equal(measurement)))
                .limit(30);
        QueryApi queryApi = client.getQueryApi();


        List<FluxTable> tables = queryApi.query(limit.toString());
        if(!(tables.size() > 0 && tables.get(0).getRecords().size() > 0)){
            return new ArrayList<GCMessageDTO>();
        }

        List<GCMessageDTO> list = new ArrayList<>();

        // 列数
        for(FluxTable fluxTable : tables){
            List<FluxRecord> records = fluxTable.getRecords();
            // 行数
            for(int i = 0;i < records.size();i++){
                FluxRecord fluxRecord = records.get(i);
                String field = fluxRecord.getField();
                Object value =  fluxRecord.getValueByKey("_value");
                if(field.equals("pauseMillis")){
                    if(list.size() > i && list.get(i) != null){
                        list.get(i).setPauseMillis((Long) value);
                    }else{
                        list.add(GCMessageDTO.builder().pauseMillis((Long) value).build());
                    }
                }else if(field.equals("startTime")){
                    if(list.size() > i && list.get(i) != null){
                        list.get(i).setStartTime((Long) value);
                    }else{
                        list.add(GCMessageDTO.builder().startTime((Long) value).build());
                    }
                }else if(field.equals("endTime")){
                    if(list.size() > i && list.get(i) != null){
                        list.get(i).setEndTime((Long) value);
                    }else{
                        list.add(GCMessageDTO.builder().endTime((Long) value).build());
                    }
                }else {
                    break;
                }
            }


        }



        return list;
    }


}
