package com.animal.product.service.impl;

import cn.hutool.json.JSONUtil;
import com.animal.product.common.ErrorCode;
import com.animal.product.common.LocalCache;
import com.animal.product.exception.BusinessException;
import com.animal.product.model.domain.ZooMessage;
import com.animal.product.model.domain.ZooSession;
import com.animal.product.model.request.ChatRequest;
import com.animal.product.service.SseService;
import com.animal.product.service.ZooMessageService;
import com.animal.product.service.ZooSessionService;
import com.animal.product.utils.CommonToolUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zoo.friend.OpenAIClient;
import com.zoo.friend.constant.Role;
import com.zoo.friend.entity.AI.chat.ChatGPTCompletion;
import com.zoo.friend.entity.AI.chat.ChatGPTMessage;
import com.zoo.friend.listener.ZooEventSourceListener;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 咏鹅
 * @version 1.0
 * @description TODO
 * @date 2023/8/1 18:32
 */
@Service
@Slf4j
public class SseServiceImpl implements SseService {


    private final OpenAIClient openAIClient;

    public SseServiceImpl(OpenAIClient openAIClient){
        this.openAIClient = openAIClient;
    }

    @Resource
    private ZooMessageService messageService;

    @Resource
    private ZooSessionService sessionService;

    @Override
    public SseEmitter createSseConnect(String uuid) {
//        Object sse = LocalCache.CACHE.get(uuid);
//        if(!Objects.isNull(sse)){
//            return (SseEmitter) sse;
//        }
        //默认30秒超时,设置为0L则永不超时
        SseEmitter sseEmitter = new SseEmitter(0l);
        //完成后回调
        sseEmitter.onCompletion(() -> {
            log.info("[{}]结束连接...................", uuid);
            LocalCache.CACHE.remove(uuid);
        });
        //超时回调
        sseEmitter.onTimeout(() -> {
            log.info("[{}]连接超时...................", uuid);
        });
        //异常回调
        sseEmitter.onError(
                throwable -> {
                    try {
                        log.info("[{}]连接异常,{}", uuid, throwable.toString());
                        sseEmitter.send(SseEmitter.event()
                                .id(uuid)
                                .name("发生异常！")
                                .data(ChatGPTMessage.Party().setContent("发生异常请重试！").partyRun())
                                .reconnectTime(3000));
                        LocalCache.CACHE.put(uuid, sseEmitter);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );
        try {
            sseEmitter.send(SseEmitter.event().reconnectTime(5000));
        } catch (IOException e) {
            e.printStackTrace();
        }
        LocalCache.CACHE.put(uuid, sseEmitter);
        log.info("[{}]创建sse连接成功！", uuid);
        return sseEmitter;
    }

    @Override
    public void sseChatDialogue(String id, ChatRequest chatRequest) {
        if (StringUtils.isBlank(chatRequest.getMessage())) {
            log.info("参数异常，msg为null", id);
            throw new BusinessException(ErrorCode.PARAMETER_ERROR,"参数异常，msg不能为空~");
        }
        String msg = chatRequest.getMessage();

        String messageContext = (String) LocalCache.CACHE.get("msg" + id);
        List<ChatGPTMessage> messages = new ArrayList<>();
        if (StringUtils.isNotBlank(messageContext)) {
            messages = JSONUtil.toList(messageContext, ChatGPTMessage.class);
            if (messages.size() >= 10) {
                messages = messages.subList(1, 10);
            }
            ChatGPTMessage currentMessage = ChatGPTMessage.Party().setContent(msg).setRole(Role.USER).partyRun();
            messages.add(currentMessage);
        } else {
            ChatGPTMessage currentMessage = ChatGPTMessage.Party().setContent(msg).setRole(Role.USER).partyRun();
            messages.add(currentMessage);
        }

        SseEmitter sseEmitter = (SseEmitter) LocalCache.CACHE.get(id);

        if (sseEmitter == null) {
            log.info("聊天消息推送失败uid:[{}],没有创建连接，请重试。", id);
            throw new BusinessException(ErrorCode.NO_AUTH,"聊天消息推送失败uid:[{}],没有创建连接，请重试。~");
        }
        ZooEventSourceListener zooEventSourceListener = new ZooEventSourceListener(sseEmitter);
        ChatGPTCompletion completion = ChatGPTCompletion
                .builder()
                .messages(messages)
                .build();

        //进行用户信息保存操作
        ZooSession userSession = new ZooSession();
        ZooMessage userMsg = new ZooMessage();
        userMsg.setMessage(chatRequest.getMessage());
        userMsg.setId(chatRequest.getMid());
        userMsg.setIcon("null"); //暂无头像
        if(msg.length()<=6)
            userSession.setTitle(msg);
        else
            userSession.setTitle(msg.substring(0,6));

        Timestamp currentDate = CommonToolUtils.getCurrentDate();
        userMsg.setFrom_Key_id(1);
        userMsg.setCreateTime(currentDate);
        userMsg.setQuestion(msg);
        userMsg.setType(Role.USER.toString());
        userMsg.setSession_id(id);

        QueryWrapper<ZooSession> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        try {
            List<ZooSession> list = sessionService.list(queryWrapper);
            if(list.isEmpty()){
                userSession.setCreateTime(currentDate);
                userSession.setUser_id(1);
                userSession.setId(id);
                sessionService.saveSession(userSession);
            }
            messageService.save(userMsg);
        } catch (Exception e){
            throw new BusinessException(ErrorCode.NO_SAVE);
        }
        openAIClient.streamCompletions(completion, zooEventSourceListener);

        LocalCache.CACHE.put("msg" + id, JSONUtil.toJsonStr(messages), LocalCache.TIMEOUT);  //将当前消息put进缓存中
    }

    @Override
    public void closeSseConnect(String uid) {
        SseEmitter sseEmitter = (SseEmitter)LocalCache.CACHE.get(uid);
        if (sseEmitter != null) {
            sseEmitter.complete();
            //移除
            LocalCache.CACHE.remove(uid);
        }
    }
}
