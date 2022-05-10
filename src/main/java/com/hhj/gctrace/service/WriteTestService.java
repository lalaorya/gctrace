package com.hhj.gctrace.service;

import com.hhj.gctrace.pojo.dto.GCMessageDTO;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

/**
 * @Author virtual
 * @Date 2022/5/10 10:47
 * @description：InfluxDB write test
 */
@Service
public class WriteTestService {

    @Autowired
    InfluxDBClient client;

    public void write(){
        GCMessageDTO gcMessageDTO = new GCMessageDTO(Instant.now(), Instant.now(), 1000L, 2000L, "Allocation Failture", "end of majorGC", "CMS", "jvm.gc.pause");
        WriteApiBlocking writeApiBlocking = client.getWriteApiBlocking();
        writeApiBlocking.writeMeasurement(WritePrecision.NS,gcMessageDTO);


        String query = "from(bucket: \"gclog\") |> range(start: -1h)";
        List<FluxTable> tables = client.getQueryApi().query(query);

        for (FluxTable table : tables) {
            for (FluxRecord record : table.getRecords()) {
                System.out.println(record.getValues().toString());
            }
        }
    }


}
