package com.gexingw.shop.common.security.config;

import com.gexingw.shop.common.security.filter.AuthenticationFilter;
import com.gexingw.shop.common.security.handler.AccessDeniedHandler;
import com.gexingw.shop.common.security.handler.AuthenticationEntryPointHandler;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/7/12 23:26
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfiguration {

    @Resource
    AuthenticationFilter authenticationFilter;

    @Bean
    @SneakyThrows
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
        // 禁用CSRF校验
        httpSecurity.csrf().disable();
        // 禁用跨区配置
        httpSecurity.cors().disable();
        // 无状态Session配置
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // 认证信息配置
        httpSecurity.addFilterAt(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // 异常处理
        httpSecurity.exceptionHandling().accessDeniedHandler(new AccessDeniedHandler()).authenticationEntryPoint(new AuthenticationEntryPointHandler());

        return httpSecurity.build();
    }

    /**
     * 设置Security默认的用户信息获取方式
     * 指定内存用户信息，避免生成默认用户信息。该账户无法用于登录
     *
     * @return UserDetailsService
     */
    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager userDetailsManager = new InMemoryUserDetailsManager();
        UserDetails muyuan = User.withUsername("muyuan").password("muyuan_password").authorities("muyuan_authority").build();
        userDetailsManager.createUser(muyuan);

        return userDetailsManager;
    }

    /**
     * 密码加密套件，默认使用bcrypt加密
     *
     * @return PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        HashMap<String, PasswordEncoder> passwordEncoderMap = new HashMap<>(1);
        passwordEncoderMap.put("bcrypt", new BCryptPasswordEncoder());

        return new DelegatingPasswordEncoder("bcrypt", passwordEncoderMap);
    }

}
