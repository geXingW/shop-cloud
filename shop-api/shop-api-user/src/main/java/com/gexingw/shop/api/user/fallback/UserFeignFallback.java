package com.gexingw.shop.api.user.fallback;

import com.gexingw.shop.api.user.feign.UserFeign;
import com.gexingw.shop.api.user.vo.UserVO;
import org.springframework.stereotype.Component;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/9/9 23:34
 */
@Component
public class UserFeignFallback implements UserFeign {

    @Override
    public UserVO getById(Long id) {
        return new UserVO(id).setName("fallback-" + id);
    }

}
