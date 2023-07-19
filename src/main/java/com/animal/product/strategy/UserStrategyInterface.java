package com.animal.product.strategy;

import com.animal.product.model.domain.ZooUsers;
import com.animal.product.model.dto.UserDTO;
import com.animal.product.model.request.UserRegisterRequest;

/**
 * @author 咏鹅、AllianceTing
 * @version 1.0
 * @description TODO
 * @date 2023/7/19 22:52
 */
public interface UserStrategyInterface {

    ZooUsers doEmailOrPhone(UserRegisterRequest users, String type);
}
