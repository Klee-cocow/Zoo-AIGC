package com.animal.product.service;

import com.animal.product.model.request.SessionRequest;
import com.animal.product.model.vo.SessionVO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.animal.product.model.domain.ZooSession;

import java.util.List;

/**
* @author 咏鹅
* @description 针对表【zoo_session】的数据库操作Service
* @createDate 2023-08-25 20:37:01
*/
public interface ZooSessionService extends IService<ZooSession> {

    void saveSession(ZooSession session);

    List<SessionVO> selectSession(Integer uid);

    Boolean updateSession(SessionRequest session);
}
