package com.gexingw.shop.service.auth.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/7/22 23:46
 */
@Data
@TableName("auth_user")
@Accessors(chain = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
public class AuthUser implements UserDetails, Serializable, OAuth2AuthenticatedPrincipal {

    private static final long serialVersionUID = -5697201612176275037L;

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
     * 用户类型：1、管理员；2、用户
     */
    private Integer type;

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
