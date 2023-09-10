package com.gexingw.shop.service.product.controller;

import com.gexingw.shop.api.user.feign.UserFeign;
import com.gexingw.shop.common.core.util.R;
import com.gexingw.shop.common.core.util.SnowflakeUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/9/8 17:20
 */
@RestController
@RequestMapping("product")
public class ProductController {

    @Resource
    private UserFeign userFeign;

    @GetMapping
    public R<Object> info() {
        return R.ok(userFeign.getById(SnowflakeUtil.getId()));
    }

}
