package com.animal.product.model.dto;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author 咏鹅
 * @version 1.0
 * @description TODO
 * @date 2023/7/19 21:41
 */
@Data
@Component
@ConfigurationProperties(prefix = "email")
public class MailSenderDTO{

    String toEmail;

    @Value("${email.email-from}")
    String emailFrom;

    @Value("${email.subject}")
    String subject;

    String code;
}
