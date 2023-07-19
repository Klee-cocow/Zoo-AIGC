package com.animal.product.service.strategyimpl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.animal.product.common.CommonToolUtils;
import com.animal.product.common.ErrorCode;
import com.animal.product.common.ValidatorCommon;
import com.animal.product.constant.IdentityEnum;
import com.animal.product.constant.UserConstant;
import com.animal.product.exception.BusinessException;
import com.animal.product.model.domain.ZooUsers;
import com.animal.product.model.dto.UserDTO;
import com.animal.product.model.request.UserRegisterRequest;
import com.animal.product.strategy.UserStrategyInterface;
import jakarta.mail.MessagingException;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author 咏鹅
 * @version 1.0
 * @description TODO
 * @date 2023/7/19 23:19
 */
public class EmailStrategyImpl implements UserStrategyInterface {
    @Override
    public ZooUsers doEmailOrPhone(UserRegisterRequest userRequest, String type) {
        ValidatorCommon.userInfoIsValid(userRequest,type);
        ZooUsers user = new ZooUsers();

        String entryPassword = DigestUtils.md5DigestAsHex((UserConstant.SALT + userRequest.getPassword()).getBytes());
        String email = userRequest.getEmail();
        String invite_code = userRequest.getInvite_code();

        user.setDescription(UserConstant.USER_DESCRIPTION);  //默认设置

        user.setEmail(email);
        user.setInvite_code(invite_code);
        user.setPassword(entryPassword);
        return user;
    }



}
