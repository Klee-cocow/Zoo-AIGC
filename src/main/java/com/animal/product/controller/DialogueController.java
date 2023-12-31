package com.animal.product.controller;

import com.animal.product.common.BaseResponse;
import com.animal.product.common.ErrorCode;
import com.animal.product.exception.BusinessException;
import com.animal.product.model.request.ChatRequest;
import com.animal.product.service.SseService;
import com.animal.product.utils.ResultUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.util.Strings;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author 咏鹅
 * @version 1.0
 * @description TODO
 * @date 2023/8/1 18:25
 */
@RestController
@RequestMapping("/api/test")
public class DialogueController {

    @Resource
    private SseService sseService;

    @GetMapping("/create_sse")
    public SseEmitter createSseConnect(HttpServletRequest request){
        String uuid = getUid(request);
        try {
            return sseService.createSseConnect(uuid);
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
    }

    @GetMapping("/closeSse")
    public void closeConnect(HttpServletRequest request) {
        String uid = getUid(request);
        sseService.closeSseConnect(uid);
    }

    @PostMapping("/chat")
    public BaseResponse<String> sseChatDialogue(@RequestBody ChatRequest chatRequest, HttpServletRequest request){
        String sid = chatRequest.getSid();
        sseService.sseChatDialogue(sid,chatRequest);
        return ResultUtil.success("OK");
    }

    private String getUid(HttpServletRequest request){
        String uuid = request.getHeader("uuid");
        if(Strings.isBlank(uuid)){
            throw new BusinessException(ErrorCode.PARAMETER_ERROR,"uuid为空");
        }
        return uuid;
    }
}
