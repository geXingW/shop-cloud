package com.gexingw.shop.service.system.controller;

import com.gexingw.shop.common.core.util.R;
import com.gexingw.shop.common.security.util.AuthUtil;
import com.gexingw.shop.service.system.service.SysMenuService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/7/30 23:01
 */
@RestController
@AllArgsConstructor
@RequestMapping("menu")
public class MenuController {

    SysMenuService sysMenuService;

    @GetMapping
    public R<Object> index() {
        return R.ok();
    }

    @GetMapping("tree")
    public R<Object> tree() {
        Long userId = AuthUtil.getUserId();
        return R.ok(sysMenuService.queryTreeByUserId(userId));
    }

}
