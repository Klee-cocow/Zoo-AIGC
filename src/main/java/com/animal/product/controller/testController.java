package com.animal.product.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 咏鹅
 * @version 1.0
 * @description TODO
 * @date 2023/7/22 1:08
 */
@RestController
@RequestMapping("/dit")
public class testController {

    @PostMapping("/abc")
    public String abc(){
        return "测试";
    }
}
