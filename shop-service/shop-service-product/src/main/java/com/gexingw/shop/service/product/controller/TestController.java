package com.gexingw.shop.service.product.controller;

import com.gexingw.shop.common.core.util.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/7/8 16:54
 */
@RestController
public class TestController {

    @GetMapping
    public R<String> index() {
        return R.ok("Product test controller index...");
    }

}
