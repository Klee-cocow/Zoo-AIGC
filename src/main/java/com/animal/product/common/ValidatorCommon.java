package com.animal.product.common;

import com.animal.product.constant.IdentityEnum;
import com.animal.product.exception.BusinessException;
import com.animal.product.model.domain.ZooUsers;
import com.animal.product.model.dto.UserDTO;
import com.animal.product.model.request.UserRegisterRequest;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * @author AllianceTing、咏鹅
 * @version 1.0
 * @description TODO
 * @date 2023/7/19 23:27
 */
public class ValidatorCommon {

    public static void userInfoIsValid(UserRegisterRequest user, String type) {



        //手机号判断
        if(type.equals(IdentityEnum.phone)){
            if (!Pattern.compile("^[1-9]{6,16}$").matcher(user.getPhone()).matches()) {
                throw new BusinessException(ErrorCode.PARAMETER_ERROR, "userRegistryRequest.User Phone Parms error");
            }

        }
        //匹配邮箱
        if(type.equals(IdentityEnum.email)){
            String email = user.getEmail();
            String password = user.getPassword();

            if (StringUtils.isAnyBlank(password,email)) {
                throw new BusinessException(ErrorCode.PARAMETER_ERROR, "邮箱或密码不能为空");
            }
            if (!Pattern.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$").matcher(email).matches()) {
                throw new BusinessException(ErrorCode.PARAMETER_ERROR, "userRegistryRequest.User MailID Parms error");
            }
            if (password.length() < 6) {
                throw new BusinessException(ErrorCode.PARAMETER_ERROR, "密码长度必须大于等于6位");
            }
            if(user.getInvite_code().isEmpty()){
                throw new BusinessException(ErrorCode.PARAMETER_ERROR,"请输入验证码");
            }

        }
    }
}
