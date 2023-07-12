package com.gexingw.shop.common.security.component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/7/12 23:03
 */
@Data
@Builder
@AllArgsConstructor
public class AuthInfo {

    private Long id;

    private String username;

    private String phone;

}
