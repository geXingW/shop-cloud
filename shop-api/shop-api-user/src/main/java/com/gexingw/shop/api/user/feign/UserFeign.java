package com.gexingw.shop.api.user.feign;

import com.gexingw.shop.api.user.fallback.UserFeignFallback;
import com.gexingw.shop.api.user.vo.UserVO;
import com.gexingw.shop.common.core.constant.ServiceConstant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/9/7 18:26
 */
@FeignClient(contextId = "userFeign", value = ServiceConstant.SERVICE_USER, path = "/user", fallback = UserFeignFallback.class)
public interface UserFeign {

    @GetMapping("getById/{id}")
    UserVO getById(@PathVariable Long id);

}
