package com.gexingw.shop.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gexingw.shop.common.security.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/7/9 14:46
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}

