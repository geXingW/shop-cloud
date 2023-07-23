package com.gexingw.shop.common.security.component;

import com.gexingw.shop.common.core.component.AuthInfo;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/7/12 23:07
 */
@Data
@Builder
public class Authentication implements org.springframework.security.core.Authentication {

    private static final long serialVersionUID = -952647711494693618L;

    private AuthInfo authInfo;

    private boolean authenticated;

    private List<GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.authInfo;
    }

    @Override
    public boolean isAuthenticated() {
        return this.authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return this.authInfo.getUsername();
    }


}
