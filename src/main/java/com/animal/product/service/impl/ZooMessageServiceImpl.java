package com.animal.product.service.impl;

import com.animal.product.common.ErrorCode;
import com.animal.product.exception.BusinessException;
import com.animal.product.mapper.ZooUsersMapper;
import com.animal.product.model.vo.MessageVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.animal.product.model.domain.ZooMessage;
import com.animal.product.mapper.ZooMessageMapper;
import com.animal.product.service.ZooMessageService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @author 咏鹅
* @description 针对表【zoo_message】的数据库操作Service实现
* @createDate 2023-08-06 21:36:00
*/
@Service
public class ZooMessageServiceImpl extends ServiceImpl<ZooMessageMapper, ZooMessage>
    implements ZooMessageService{

    @Override
    public void saveUserMessage(String question, String msg,Long id,String sid) {
        LambdaUpdateWrapper<ZooMessage> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(ZooMessage::getMessage,msg)
                .eq(ZooMessage::getId,id)
                .eq(ZooMessage::getSession_id,sid);

        try{
            this.update(updateWrapper);
        }catch (Exception e){
            throw new BusinessException(ErrorCode.NO_SAVE);
        }
    }

    @Override
    public List<MessageVO> selectMessage(String sessionId) {
        LambdaQueryWrapper<ZooMessage> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ZooMessage::getSession_id,sessionId);
        List<ZooMessage> messages = this.list(queryWrapper);
        List<MessageVO> currentMessage = new ArrayList<>();
        for(ZooMessage item : messages){
            MessageVO messageVO = new MessageVO();
            BeanUtils.copyProperties(item,messageVO);
            currentMessage.add(messageVO);
        }
        return currentMessage;
    }
}




