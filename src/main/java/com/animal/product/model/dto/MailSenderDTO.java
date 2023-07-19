package com.animal.product.model.dto;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.stereotype.Component;

import java.io.Serializable;

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
    String text;

    @Value("${email.email-from}")
    String emailFrom;

    @Value("${email.subject}")
    String subject;

}
