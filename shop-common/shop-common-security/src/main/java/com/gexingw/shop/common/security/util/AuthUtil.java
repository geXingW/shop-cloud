package com.gexingw.shop.common.security.util;

import com.gexingw.shop.common.security.component.AuthInfo;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/7/16 17:22
 */
@SuppressWarnings("unused")
public class AuthUtil {

    public static Long getUserId() {
        AuthInfo userInfo = getUserInfo();
        return userInfo != null ? userInfo.getId() : -1L;
    }

    public static String getPhone() {
        AuthInfo userInfo = getUserInfo();
        return userInfo != null ? userInfo.getPhone() : null;
    }

    public static String getUsername() {
        AuthInfo userInfo = getUserInfo();
        return userInfo != null ? userInfo.getUsername() : null;
    }

    private static AuthInfo getUserInfo() {
        return (AuthInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
