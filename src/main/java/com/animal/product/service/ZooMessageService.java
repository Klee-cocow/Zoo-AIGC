package com.animal.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.animal.product.model.domain.ZooMessage;

/**
* @author 咏鹅
* @description 针对表【zoo_message】的数据库操作Service
* @createDate 2023-08-06 21:36:00
*/
public interface ZooMessageService extends IService<ZooMessage> {
    void saveUserMessage(String question,String msg);
}
