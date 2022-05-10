package com.hhj.gctrace.controller;

import com.hhj.gctrace.service.WriteTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author virtual
 * @Date 2022/5/10 11:08
 * @description：
 */
@RestController
public class TestController {

    @Autowired
    WriteTestService service;

    @GetMapping("/write")
    public void write(){
        service.write();
        System.out.println(111);

    }

}
