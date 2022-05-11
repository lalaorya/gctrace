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

    /**
     * 访问凭证
     */
    @Value("${INFLUXDB_TOKEN}")
    String token;

    /**
     * 相当于数据库database
     */
    @Value("${INFLUXDB_BUCKET}")
    String bucket;

    @Value("${INFLUXDB_ORG}")
    String org;

    @Value(("${INFLUXDB_URL}"))
    String url;

    /**
     * 注入client对象
     * @return
     */
    @Bean
    public InfluxDBClient initClient(){
        return InfluxDBClientFactory.create(url,token.toCharArray(),org,bucket);
    }
}
