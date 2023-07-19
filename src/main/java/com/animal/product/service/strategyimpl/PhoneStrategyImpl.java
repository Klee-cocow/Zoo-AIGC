package com.animal.product.service.strategyimpl;

import com.animal.product.model.domain.ZooUsers;
import com.animal.product.model.dto.UserDTO;
import com.animal.product.model.request.UserRegisterRequest;
import com.animal.product.strategy.UserStrategyInterface;

/**
 * @author 咏鹅
 * @version 1.0
 * @description TODO
 * @date 2023/7/19 23:20
 */
public class PhoneStrategyImpl implements UserStrategyInterface {
    @Override
    public ZooUsers doEmailOrPhone(UserRegisterRequest users, String type) {

        return null;
    }
}
