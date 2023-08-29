package com.animal.product.service.impl;

import com.animal.product.common.ErrorCode;
import com.animal.product.exception.BusinessException;
import com.animal.product.model.request.SessionRequest;
import com.animal.product.model.vo.SessionVO;
import com.animal.product.service.ZooSessionService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.animal.product.model.domain.ZooSession;
import com.animal.product.mapper.ZooSessionMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @author 咏鹅
* @description 针对表【zoo_session】的数据库操作Service实现
* @createDate 2023-08-25 20:37:01
*/
@Service
public class ZooSessionServiceImpl extends ServiceImpl<ZooSessionMapper, ZooSession>
    implements ZooSessionService {

    @Override
    public void saveSession(ZooSession session) {
        this.save(session);
    }

    @Override
    public List<SessionVO> selectSession(Integer uid) {
        LambdaQueryWrapper<ZooSession> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ZooSession::getUser_id, uid);
        List<ZooSession> session = this.list(queryWrapper);

        List<SessionVO> currentSession = new ArrayList<>();
        for(ZooSession item : session){
            SessionVO sessionVO = new SessionVO();
            BeanUtils.copyProperties(item,sessionVO);
            currentSession.add(sessionVO);
        }
        return currentSession;

    }

    @Override
    public Boolean updateSession(SessionRequest session) {
        LambdaUpdateWrapper<ZooSession> queryWrapper = new LambdaUpdateWrapper<>();
        queryWrapper.eq(ZooSession::getUser_id,session.getUser_id())
                .eq(ZooSession::getId,session.getId())
                .set(ZooSession::getTitle,session.getTitle());

        boolean flag = false;
        try{
            flag = this.update(queryWrapper);
        }catch (Exception e){
            throw new BusinessException(ErrorCode.NO_SAVE);
        }
        return flag;
    }
}




