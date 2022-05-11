package com.hhj.gctrace.pojo.dto;

import lombok.*;


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
public class GCMessageDTO {

   private Long startTime;


   private Long endTime;


   private Long pauseMillis;


   private Long maxPauseMillis;


   private String gcCause;


   private String gcAction;


   private String gcName;


   private String type;

    /**
     * 格式：ip:port
     */
//    @Column
//    String serverAddress;

}
