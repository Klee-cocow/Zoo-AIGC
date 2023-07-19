package com.animal.product.service;

import com.animal.product.common.BaseResponse;
import com.animal.product.model.dto.UserDTO;
import com.animal.product.model.request.UserRegisterRequest;
import com.baomidou.mybatisplus.extension.service.IService;
import com.animal.product.model.domain.ZooUsers;
import jakarta.servlet.http.HttpServletRequest;

/**
 * @author 咏鹅
 * @description 针对表【zoo_users】的数据库操作Service
 * @createDate 2023-07-19 17:58:01
 */
public interface ZooUsersService extends IService<ZooUsers> {
    Integer userRegister(UserRegisterRequest userRegisterRequest, String registerIdentity, HttpServletRequest request);

    UserDTO userLogin(String userAccount, String password, HttpServletRequest request);

    UserDTO getLoginUser(HttpServletRequest request);

    String generateCodeToEmail(String email);


}
