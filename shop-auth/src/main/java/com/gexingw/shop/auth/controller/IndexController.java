package com.gexingw.shop.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/7/9 15:17
 */
@RestController
public class IndexController {

    @GetMapping
    public Object index(){
        return "Index controller index method.";
    }

}
