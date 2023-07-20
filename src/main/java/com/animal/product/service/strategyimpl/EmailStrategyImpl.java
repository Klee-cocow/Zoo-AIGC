package com.animal.product.service.strategyimpl;

import com.animal.product.common.ValidatorCommon;
import com.animal.product.constant.UserConstant;
import com.animal.product.model.domain.ZooUsers;
import com.animal.product.model.dto.UserDTO;
import com.animal.product.strategy.UserStrategyInterface;
import org.springframework.util.DigestUtils;

/**
 * @author 咏鹅、AllianceTing
 * @version 1.0
 * @description TODO
 * @date 2023/7/19 23:19
 */
public class EmailStrategyImpl implements UserStrategyInterface {
    @Override
    public ZooUsers doEmailOrPhone(UserDTO userDTO, String type) {
        ValidatorCommon.userInfoIsValid(userDTO,type);
        ZooUsers user = new ZooUsers();

        if(!userDTO.getPassword().isEmpty()) {
            String entryPassword = DigestUtils.md5DigestAsHex((UserConstant.SALT + userDTO.getPassword()).getBytes());
            user.setPassword(entryPassword);
        }

        if(!userDTO.getEmail().isEmpty()){
            String email = userDTO.getEmail();
            user.setEmail(email);
        }

        if(!userDTO.getInvite_code().isEmpty()) {
            String invite_code = userDTO.getInvite_code();
            user.setInvite_code(invite_code);
        }

        return user;
    }



}
