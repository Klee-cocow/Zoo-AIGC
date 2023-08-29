package com.animal.product.controller;

import com.animal.product.common.BaseResponse;
import com.animal.product.model.request.ChatRequest;
import com.animal.product.model.request.SessionRequest;
import com.animal.product.model.vo.MessageVO;
import com.animal.product.model.vo.SessionVO;
import com.animal.product.service.ZooMessageService;
import com.animal.product.service.ZooSessionService;
import com.animal.product.utils.ResultUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import javax.xml.transform.Result;
import java.util.List;

/**
 * @author 咏鹅
 * @version 1.0
 * @description TODO
 * @date 2023/8/6 22:28
 */
@RestController
@RequestMapping("/api/msg")
public class MessageController {

    @Resource
    private ZooMessageService zooMessageService;
    @Resource
    private ZooSessionService zooSessionService;


    @PostMapping("/save")
    public void saveUserMessage(@RequestBody ChatRequest chatRequest,HttpServletRequest request){
        zooMessageService.saveUserMessage(chatRequest.getQuestion(),chatRequest.getMessage(),chatRequest.getMid(),chatRequest.getSid());
    }

    @PostMapping("/updateSes")
    public BaseResponse<Boolean> updateSession(@RequestBody SessionRequest session,HttpServletRequest request){
        Boolean flag = zooSessionService.updateSession(session);
        return ResultUtil.success(flag);
    }

    @PostMapping("/selSes")
    public BaseResponse<List<SessionVO>> selectSession(@RequestParam("uid") Integer uid, HttpServletRequest request){
        List<SessionVO> sessionVOS = zooSessionService.selectSession(uid);
        return ResultUtil.success(sessionVOS);
    }

    @PostMapping("/selMsg")
    public BaseResponse<List<MessageVO>> selectMessage(@RequestParam("sid") String sid,HttpServletRequest request){
        List<MessageVO> messageVOS = zooMessageService.selectMessage(sid);
        return ResultUtil.success(messageVOS);
    }

}
