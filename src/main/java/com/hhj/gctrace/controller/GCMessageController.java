package com.hhj.gctrace.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.hhj.gctrace.pojo.dto.GCMessageDTO;
import com.hhj.gctrace.service.GCMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author virtual
 * @Date 2022/5/11 17:50
 * @description：
 */
@RestController
public class GCMessageController {


    @Autowired
    GCMessageService gcMessageService;


    @GetMapping("getHistoryLog")
    public List<GCMessageDTO> getHistoryLog(){
        return gcMessageService.read(10);

    }

}
