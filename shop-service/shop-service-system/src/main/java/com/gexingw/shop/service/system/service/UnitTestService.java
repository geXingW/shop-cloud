package com.gexingw.shop.service.system.service;

import com.gexingw.shop.service.system.mapper.AdminMapper;
import com.gexingw.shop.service.system.vo.AdminAuthInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/9/5 9:21
 */
@SuppressWarnings("unused")
@Service
public class UnitTestService {

    @Resource
    AdminMapper adminMapper;

    private boolean privateMethod(Integer num) {
        return num % 2 == 0;
    }

    public boolean publicMethod(Integer num) {
        return num % 2 == 0;
    }

    public static boolean staticMethod(Integer num) {
        return num % 2 == 0;
    }

    public Boolean isEvenNumberPublic(Integer num) {
        return this.publicMethod(num);
    }

    public Boolean isEvenNumberPrivate(Integer num) {
        return this.privateMethod(num);
    }

    public static Boolean isEvenNumberStatic(Integer num) {
        return staticMethod(num);
    }

    public AdminAuthInfo.Info queryDB() {
        return adminMapper.findByAuthUserId(1L);
    }

}
