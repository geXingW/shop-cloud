package com.gexingw.shop.service.auth.config;

import com.gexingw.shop.service.auth.handler.AccessDeniedHandler;
import com.gexingw.shop.service.auth.handler.AuthenticationEntryPointHandler;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.HashMap;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/7/9 13:29
 */
@Configuration
public class WebSecurityConfiguration {

    @Bean
    @Order(2)
    @SneakyThrows
    public SecurityFilterChain webSecurityFilterChain(HttpSecurity httpSecurity) {
        httpSecurity.authorizeHttpRequests(
                authorize -> authorize
                        .antMatchers("/css/**", "/img/**", "/login", "/favicon.ico").permitAll()
                        .mvcMatchers("/oauth2/captcha").permitAll()
                        .anyRequest().authenticated()
        );

        httpSecurity.csrf().disable();

        // 自定义登录页面
        httpSecurity.formLogin(formLogin -> formLogin.loginPage("/login"));

        // 启用授权码模式的表单登录
//        httpSecurity.formLogin(Customizer.withDefaults());

        // 禁用密码模式的表单登录
//        httpSecurity.formLogin().disable();

        // 异常处理
        httpSecurity.exceptionHandling().accessDeniedHandler(new AccessDeniedHandler()).authenticationEntryPoint(new AuthenticationEntryPointHandler());

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        HashMap<String, PasswordEncoder> passwordEncoderMap = new HashMap<>(1);
        passwordEncoderMap.put("bcrypt", new BCryptPasswordEncoder());

        return new DelegatingPasswordEncoder("bcrypt", passwordEncoderMap);
    }

}
