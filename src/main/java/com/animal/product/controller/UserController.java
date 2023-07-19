package com.animal.product.controller;

import cn.hutool.core.util.RandomUtil;
import com.animal.product.common.BaseResponse;
import com.animal.product.common.ErrorCode;
import com.animal.product.common.ResultUtils;
import com.animal.product.constant.IdentityEnum;
import com.animal.product.constant.UserConstant;
import com.animal.product.exception.BusinessException;
import com.animal.product.model.domain.ZooUsers;
import com.animal.product.model.dto.UserDTO;
import com.animal.product.model.request.UserLoginRequest;
import com.animal.product.model.request.UserRegisterRequest;
import com.animal.product.service.ZooUsersService;
import com.animal.product.strategy.UserStrategyContent;
import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

/**
 * @author 咏鹅
 * @version 1.0
 * @description TODO
 * @date 2023/7/19 18:01
 */
@RestController
public class UserController {

    @Resource
    private ZooUsersService userService;


    @PostMapping("/register")
    public BaseResponse<Integer> userRegister(@RequestBody UserRegisterRequest userRegisterRequest, String registerIdentity, HttpServletRequest request){
        if(userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMETER_ERROR);
        }

        Integer result = userService.userRegister(userRegisterRequest,registerIdentity,request);

        return ResultUtils.success(result);
    }


    @PostMapping("/login")
    public BaseResponse<UserDTO> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request){
        if(userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMETER_ERROR);
        }

        String userEmail = userLoginRequest.getEmail();
        String password = userLoginRequest.getPassword();
        if(StringUtils.isAnyBlank(userEmail,password)){
            throw new BusinessException(ErrorCode.PARAMETER_ERROR,"用户名或密码不能为空");
        }
        if (password.length() < 6) {
            throw new BusinessException(ErrorCode.PARAMETER_ERROR, "密码长度必须大于等于6位");
        }

        UserDTO user = userService.userLogin(userEmail, password,request);


        return ResultUtils.success(user);
    }

    @GetMapping("/sendcode")
    public void sendCode(@PathVariable String email){
        userService.generateCodeToEmail(email);
    }


}
