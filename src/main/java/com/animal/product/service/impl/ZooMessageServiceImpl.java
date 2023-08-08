package com.animal.product.service.impl;

import com.animal.product.common.ErrorCode;
import com.animal.product.exception.BusinessException;
import com.animal.product.mapper.ZooUsersMapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.animal.product.model.domain.ZooMessage;
import com.animal.product.mapper.ZooMessageMapper;
import com.animal.product.service.ZooMessageService;
import org.springframework.stereotype.Service;

/**
* @author 咏鹅
* @description 针对表【zoo_message】的数据库操作Service实现
* @createDate 2023-08-06 21:36:00
*/
@Service
public class ZooMessageServiceImpl extends ServiceImpl<ZooMessageMapper, ZooMessage>
    implements ZooMessageService{

    @Override
    public void saveUserMessage(String question, String msg) {
        LambdaUpdateWrapper<ZooMessage> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(ZooMessage::getQuestion,question)
                .eq(ZooMessage::getMessage,msg);

        try{
            this.update(updateWrapper);
        }catch (Exception e){
            throw new BusinessException(ErrorCode.NO_SAVE);
        }
    }
}




