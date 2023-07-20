package com.animal.product.service.strategyimpl;

import com.animal.product.common.ValidatorCommon;
import com.animal.product.model.domain.ZooUsers;
import com.animal.product.model.dto.UserDTO;
import com.animal.product.strategy.UserStrategyInterface;

/**
 * @author 咏鹅、AllianceTing
 * @version 1.0
 * @description TODO
 * @date 2023/7/19 23:20
 */
public class PhoneStrategyImpl implements UserStrategyInterface {
    @Override
    public ZooUsers doEmailOrPhone(UserDTO userDTO, String type) {
        ValidatorCommon.userInfoIsValid(userDTO,type);
        ZooUsers user = new ZooUsers();

        if(!(userDTO.getPhone() !=null)){
            String phone = userDTO.getPhone();
            user.setPhone(phone);
        }

        if(!(userDTO.getPhone_code() !=null)){
            String phone_code = userDTO.getPhone_code();
            user.setPhone_code(phone_code);
        }

        return user;
    }
}
