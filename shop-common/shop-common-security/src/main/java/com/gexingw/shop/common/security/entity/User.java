package com.gexingw.shop.common.security.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;

import java.io.Serializable;
import java.util.*;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/7/9 16:59
 */
@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
public class User implements UserDetails, Serializable, OAuth2AuthenticatedPrincipal {

    private Long id;

    /**
     * 账号（手机号）
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 姓名
     */
    private String name;

    /**
     * 手机
     */
    private String phone;

    /**
     * 组织架构id
     */
    private Long deptId;

    /**
     * 用户类型：1、公司员工 2、司机
     */
    private Integer type;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 状态:  1 正常 2 禁用
     */
    private Integer status;

    @Override
    @JsonIgnore
    public Map<String, Object> getAttributes() {
        return new HashMap<>();
    }

    @Override
    @JsonIgnore
    public <A> A getAttribute(String name) {
        return OAuth2AuthenticatedPrincipal.super.getAttribute(name);
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }

}
