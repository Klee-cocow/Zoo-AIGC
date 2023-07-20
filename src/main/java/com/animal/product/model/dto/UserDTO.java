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
     * 密码
     */
    private String password;

    /**
     * 手机验证码
     */
    private String phone_code;

    private String phone;

    /**
     * 邮箱确认码
     */
    private String invite_code;
}
