package com.animal.product.controller;

import com.animal.product.model.request.ChatRequest;
import com.animal.product.service.ZooMessageService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * @author 咏鹅
 * @version 1.0
 * @description TODO
 * @date 2023/8/6 22:28
 */
@RestController
@RequestMapping("/msg")
public class MessageController {

    @Resource
    private ZooMessageService zooMessageService;

    @PostMapping("/save")
    public void saveUserMessage(@RequestBody ChatRequest request){
        zooMessageService.saveUserMessage(request.getQuestion(),request.getMessage());
    }

}
