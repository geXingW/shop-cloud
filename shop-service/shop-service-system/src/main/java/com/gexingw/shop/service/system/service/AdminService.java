package com.gexingw.shop.service.system.service;

import com.gexingw.shop.common.db.service.BaseService;
import com.gexingw.shop.service.system.entity.SysAdmin;
import com.gexingw.shop.service.system.mapper.AdminMapper;
import com.gexingw.shop.service.system.vo.AdminAuthInfo;
import org.springframework.stereotype.Service;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/7/30 16:15
 */
@Service
public class AdminService extends BaseService<AdminMapper, SysAdmin> {

    public AdminAuthInfo.Info findByAuthUserId(Long id) {
        return baseMapper.findByAuthUserId(id);
    }

}
