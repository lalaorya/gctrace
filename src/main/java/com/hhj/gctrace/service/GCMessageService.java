package com.hhj.gctrace.service;

import com.hhj.gctrace.pojo.dto.GCMessageDTO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author virtual
 * @Date 2022/5/11 11:30
 * @description：
 */
public interface GCMessageService {

    void write(GCMessageDTO messageDTO);

    List<GCMessageDTO> read(int time);


}
