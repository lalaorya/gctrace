package com.hhj.gctrace.pojo.dto;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import lombok.*;

import java.time.Instant;

/**
 * @Author virtual
 * @Date 2022/5/10 10:49
 * @description：
 */
@Data @Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Measurement(name = "gcMessage")
public class GCMessageDTO {
    @Column(timestamp = true)
    Instant startTime;

    @Column(tag = true)
    Instant endTime;

    @Column
    Long pauseMillis;

    @Column
    Long maxPauseMillis;

    @Column
    String gcCause;

    @Column
    String gcAction;

    @Column
    String gcName;

    @Column
    String type;

}
