package com.gexingw.shop.common.core.component;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/7/22 16:55
 */
@Data
@Builder
@Accessors(chain = true)
@SuppressWarnings("UnusedAssignment")
public class AuthInfo implements Serializable {

    private static final long serialVersionUID = -3297097606364381474L;

    private Long id = -1L;

    private String clientId = "";

    private String username = "";

    private Long tenantId = -1L;

    private String phone = "";

    private Set<String> scopes = new HashSet<>();

}
