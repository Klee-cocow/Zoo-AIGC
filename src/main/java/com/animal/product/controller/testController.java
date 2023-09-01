package com.animal.product.controller;

import cn.hutool.Hutool;
import cn.hutool.core.convert.impl.CharacterConverter;
import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import com.animal.product.common.BaseResponse;
import com.animal.product.common.ErrorCode;
import com.animal.product.common.LocalCache;
import com.animal.product.exception.BusinessException;
import com.animal.product.model.vo.WxInfoVO;
import com.animal.product.service.WxUserService;
import com.animal.product.utils.CommonToolUtils;
import com.animal.product.utils.HttpClientUtils;
import com.animal.product.utils.ResultUtil;
import com.zoo.friend.OpenAIClient;
import com.zoo.friend.constant.Role;
import com.zoo.friend.entity.AI.chat.ChatGPTCompletion;
import com.zoo.friend.entity.AI.chat.ChatGPTMessage;
import com.zoo.friend.listener.ZooEventSourceListener;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.xml.stream.events.Characters;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

/**
 * @author 咏鹅
 * @version 1.0
 * @description TODO
 * @date 2023/7/22 1:08
 */
@RestController
@RequestMapping("/wx")
@Slf4j
public class testController {




    @Resource
    private WxUserService wxService;

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


    /**
     * @description 微信验证
     * @param request
     * @return java.lang.String
     * @author 咏鹅
     * @date 2023/8/30 22:17
    */
    @GetMapping("/callback")
    @ResponseBody
    public String checkSign (HttpServletRequest request) throws Exception {
        log.info("===========>checkSign");
        // 获取微信请求参数
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        log.info("开始校验此次消息是否来自微信服务器，param->signature:{},\ntimestamp:{},\nnonce:{},\nechostr:{}",
                signature, timestamp, nonce, echostr);
        if (CommonToolUtils.checkSignature(signature, timestamp, nonce)) {
            return echostr;
        }
        return "";
    }

    @GetMapping("/login")
    @ResponseBody
    public BaseResponse<WxInfoVO> getWxQRCodeParam(){
        WxInfoVO wxInfoVO = wxService.getWxQRCodeParam();
        return ResultUtil.success(wxInfoVO);
    }

    @GetMapping("/redirect")
    public BaseResponse<String> redirectUrl(HttpServletResponse response){
        String str = wxService.redirectUrl(response);
        return ResultUtil.success(str);
    }

    @GetMapping("/redirect/user")
    public BaseResponse<String> getWxUserInfo(@RequestParam("code") String code,
                                              @RequestParam(value = "state",required = false) String state,
                                              HttpServletResponse response){
        String userName = wxService.getWxUserInfo(code, state, response);
        return ResultUtil.success(userName);

    }

    @PostMapping("/callback")
    public String responseMsg(HttpServletRequest request,HttpServletResponse response){
        String str = wxService.responseMsg(request, response);
        return str;
    }

    @GetMapping("/create_sse")
    public SseEmitter createSseConnect(HttpServletRequest request){
        String ticket = request.getHeader("ticket");
        try {
            return wxService.createSseConnect(ticket);
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
    }

    @PostMapping("/QRcode")
    public void wxQRCodeScan(@RequestParam("ticket") String ticket,HttpServletRequest request){
        SseEmitter sseEmitter = (SseEmitter) LocalCache.CACHE.get(ticket);
        if(Objects.isNull(sseEmitter)) return;
        try {
            String jwt = wxService.wxQRCodeScan(ticket);
            if(StringUtils.isNotEmpty(jwt)) {
                sseEmitter.send("true/n"+jwt);
            }
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"微信推送失败");
        }
        sseEmitter.complete();
        LocalCache.CACHE.remove(ticket);
    }


}
