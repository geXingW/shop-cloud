package com.gexingw.shop.service.product.controller;

import com.gexingw.shop.common.core.util.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/7/10 12:00
 */
@RestController
public class IndexController {

    @GetMapping
    public R<String> index(HttpServletRequest request){
        System.out.println(request.getHeader("SHOP-AUTH-USER-ID"));

        return R.ok("Production index controller index.");
    }

}
