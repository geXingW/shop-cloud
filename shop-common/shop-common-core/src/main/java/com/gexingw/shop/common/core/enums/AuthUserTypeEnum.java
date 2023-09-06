package com.gexingw.shop.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/7/30 23:23
 */
@Getter
@AllArgsConstructor
public enum AuthUserTypeEnum {

    ADMIN(1, "管理员"),

    MEMBER(2, "用户"),
    ;

    private final Integer code;

    private final String text;

}
