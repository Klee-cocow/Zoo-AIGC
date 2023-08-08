package com.animal.product.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 咏鹅
 * @version 1.0
 * @description TODO
 * @date 2023/8/1 18:44
 */
@Data
public class ChatRequest implements Serializable {

    private String message;

    private String uuid;

    private String question;
}
