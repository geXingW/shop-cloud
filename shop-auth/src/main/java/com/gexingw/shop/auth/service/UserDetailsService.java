package com.gexingw.shop.auth.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gexingw.shop.auth.mapper.UserMapper;
import com.gexingw.shop.common.security.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/7/9 14:42
 */
@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Resource
    UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userMapper.selectOne(new QueryWrapper<User>().eq("username", username));
    }

}
