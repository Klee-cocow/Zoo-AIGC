package com.animal.product.controller;

import com.zoo.friend.OpenAIClient;
import com.zoo.friend.constant.Role;
import com.zoo.friend.entity.AI.chat.ChatGPTCompletion;
import com.zoo.friend.entity.AI.chat.ChatGPTMessage;
import com.zoo.friend.listener.ZooEventSourceListener;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.websocket.server.PathParam;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

/**
 * @author 咏鹅
 * @version 1.0
 * @description TODO
 * @date 2023/7/22 1:08
 */
@RestController
@RequestMapping("/dialogue")
public class testController {

    @GetMapping(value = "/chat",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter chat(@RequestParam("msg") String msg, HttpServletResponse response){
        SseEmitter sseEmitter = new SseEmitter();

        ChatGPTMessage message = ChatGPTMessage.Party()
                .setRole(Role.USER).setContent(msg).partyRun();

        ChatGPTCompletion completion = ChatGPTCompletion.builder()
                .messages(Arrays.asList(message)).build();

        OpenAIClient client = OpenAIClient.Party()
                .apikey("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")
                .partyRun();

        // 这个类是一个监听器
        ZooEventSourceListener eventSourceListener = new ZooEventSourceListener();
        // 这个方法是进行sse操作
        client.streamCompletions(completion,eventSourceListener);


        CountDownLatch countDownLatch = new CountDownLatch(1);
        try {
            countDownLatch.await();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        sseEmitter.complete();

        return sseEmitter;
    }
}
