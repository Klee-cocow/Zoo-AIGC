package com.animal.product.controller;

import com.animal.product.common.BaseResponse;
import com.animal.product.common.ErrorCode;
import com.animal.product.common.ResultUtils;
import com.animal.product.exception.BusinessException;
import com.animal.product.model.dto.UserDTO;
import com.animal.product.model.request.UserLoginRequest;
import com.animal.product.model.request.UserRegisterRequest;
import com.animal.product.model.vo.UserVO;
import com.animal.product.service.ZooUsersService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

/**
 * @author 咏鹅、AllianceTing
 * @version 1.0
 * @description 用户方法
 * @date 2023/7/19 18:01
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private ZooUsersService userService;


    @PostMapping("/register")
    public BaseResponse<Integer> userRegister(@RequestBody UserRegisterRequest userRegisterRequest, String registerIdentity, HttpServletRequest request) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMETER_ERROR);
        }
        Integer result = userService.userRegister(userRegisterRequest, registerIdentity, request);

        return ResultUtils.success(result);
    }


    @PostMapping("/login")
    public BaseResponse<UserVO> userLogin(@RequestBody UserLoginRequest userLoginRequest, String loginIdentity, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMETER_ERROR);
        }
        UserVO user = userService.userLogin(userLoginRequest, loginIdentity, request);


        return ResultUtils.success(user);
    }

    @PostMapping("/sendcode")
    public void sendCode(@RequestParam String email) {
        userService.generateCodeToEmail(email);
    }


    @GetMapping("/getLoginUser")
    public BaseResponse<UserVO> getLoginUser(HttpServletRequest request){
        String token = request.getHeader("Authorization");

        UserVO loginUser = userService.getLoginUser(request);
        //验证token有效性
        return ResultUtils.success(loginUser);
    }




}
