package com.animal.product.model.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 咏鹅
 * @version 1.0
 * @description TODO
 * @date 2023/7/19 18:08
 */
@Data
public class UserDTO implements Serializable {

    /**
     * 账户邮箱
     */
    private String email;


    /**
     * 姓名
     */
    private String name;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 邀请码
     */
    private String remember_token;

    /**
     * 手机
     */
    private String phone;

    /**
     * 个人介绍
     */
    private String description;


    /**
     * 剩余额度
     */
    private Integer money;
}
