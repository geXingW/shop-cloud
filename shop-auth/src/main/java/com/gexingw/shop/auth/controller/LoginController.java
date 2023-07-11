package com.gexingw.shop.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/7/9 15:17
 */
@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

}
