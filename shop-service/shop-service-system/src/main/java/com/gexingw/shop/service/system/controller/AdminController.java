package com.gexingw.shop.service.system.controller;

import com.gexingw.shop.common.core.component.AuthInfo;
import com.gexingw.shop.common.core.util.R;
import com.gexingw.shop.service.system.service.AdminService;
import com.gexingw.shop.service.system.vo.AdminAuthInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/7/30 15:52
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("admin")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("info")
    public R<AdminAuthInfo> info(@AuthenticationPrincipal AuthInfo authInfo) {
        AdminAuthInfo adminAuthInfo = AdminAuthInfo.builder().build();
        adminAuthInfo.setInfo(adminService.findByAuthUserId(authInfo.getId()));
        adminAuthInfo.setRoles(Collections.singletonList("admin"));
        adminAuthInfo.setDataScopes(Collections.singletonList(0L));

        return R.ok(adminAuthInfo);
    }

}
