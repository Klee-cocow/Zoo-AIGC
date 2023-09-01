package com.animal.product.service;

import com.animal.product.model.request.ChatRequest;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

/**
 * @author 咏鹅
 * @version 1.0
 * @description TODO
 * @date 2023/8/1 18:31
 */
public interface SseService {

    SseEmitter createSseConnect(String uuid) throws IOException;


    void sseChatDialogue(String id, ChatRequest chatRequest);

    void closeSseConnect(String uid);
}
