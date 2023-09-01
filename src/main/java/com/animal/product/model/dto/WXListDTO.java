package com.animal.product.model.dto;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author 咏鹅
 * @version 1.0
 * @description TODO
 * @date 2023/8/30 21:45
 */
@Data
@Component
@ConfigurationProperties(prefix = "wx")
public class WXListDTO {

    @Value("${wx.app_id}")
    String APP_ID;
    @Value("${wx.app_secret}")
    String APP_SECRET;
    @Value("${wx.redirect_url}")
    String REDIRECT_URL;
}
