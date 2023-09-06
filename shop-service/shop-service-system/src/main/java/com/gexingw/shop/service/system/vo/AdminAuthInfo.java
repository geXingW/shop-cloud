package com.gexingw.shop.service.system.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/7/30 16:22
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminAuthInfo implements Serializable {

    private static final long serialVersionUID = 8277088834514218296L;

    private Info info;

    private List<String> roles;

    private List<Long> dataScopes;

    @Data
    public static class Info implements Serializable {

        private static final long serialVersionUID = 1201425413656413529L;

        private Long id;

        private Long authUserId;

        private Long deptId;

        private Long jobId;

        private String nickname;

        private String username;

        private Boolean isAdmin;

    }

}
