package com.animal.product.common;

import com.animal.product.model.dto.MailSenderDTO;
import com.animal.product.model.vo.UserVO;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.HashMap;

/**
 * @author 咏鹅
 * @version 1.0
 * @description TODO
 * @date 2023/7/19 19:23
 */
@Component
public class CommonToolUtils {

    @Resource
    JavaMailSender javaMailSender;

    @Resource
    MailSenderDTO mailSenderDTO;

    // 获取用户当前的真实ip
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("PRoxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }


    //发送邮箱
    public void sendMail(String mailDes,String mailText) throws MessagingException {
        mailSenderDTO.setToEmail(mailDes);
        mailSenderDTO.setText(mailText);

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage,true);
        messageHelper.setFrom(mailSenderDTO.getEmailFrom());
        messageHelper.setSubject(mailSenderDTO.getSubject());
        messageHelper.setTo(mailSenderDTO.getToEmail());
        messageHelper.setText(mailSenderDTO.getText());
        javaMailSender.send(mimeMessage);
    }


}
