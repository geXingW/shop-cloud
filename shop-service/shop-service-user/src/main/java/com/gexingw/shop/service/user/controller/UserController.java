package com.gexingw.shop.service.user.controller;

import com.gexingw.shop.api.user.feign.UserFeign;
import com.gexingw.shop.api.user.vo.UserVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/9/8 16:45
 */
@RestController
@RequestMapping("/user")
public class UserController implements UserFeign {

    @Override
    @GetMapping("getById/{id}")
    public UserVO getById(@PathVariable Long id) {
        return new UserVO(id).setName("user-" + id);
    }

}
