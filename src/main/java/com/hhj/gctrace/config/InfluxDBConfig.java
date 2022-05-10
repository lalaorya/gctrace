package com.hhj.gctrace.config;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author virtual
 * @Date 2022/5/10 10:36
 * @description：
 */
@Configuration
public class InfluxDBConfig {

    @Value("${INFLUXDB_TOKEN}")
    String token;

    @Value("${INFLUXDB_BUCKET}")
    String bucket;

    @Value("${INFLUXDB_ORG}")
    String org;

    @Value(("${INFLUXDB_URL}"))
    String url;

    /**
     * 初始化数据
     * @return
     */
    @Bean
    public InfluxDBClient initClient(){
        System.out.println(System.getenv(token));
        return InfluxDBClientFactory.create(url,token.toCharArray(),org,bucket);
    }
}
