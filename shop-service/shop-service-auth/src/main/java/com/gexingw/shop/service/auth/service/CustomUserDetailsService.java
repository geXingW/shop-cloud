package com.gexingw.shop.service.auth.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gexingw.shop.service.auth.entity.AuthUser;
import com.gexingw.shop.service.auth.mapper.UserMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * smart-logistics-saas.
 *
 * @author W.sf
 * @date 2023/7/18 10:38
 */
@Service
@Primary
public class CustomUserDetailsService implements UserDetailsService {

    @Resource
    UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userMapper.selectOne(new QueryWrapper<AuthUser>().eq("username", username));
    }

}
