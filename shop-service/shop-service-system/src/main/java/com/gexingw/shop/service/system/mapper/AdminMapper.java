package com.gexingw.shop.service.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gexingw.shop.service.system.entity.SysAdmin;
import com.gexingw.shop.service.system.vo.AdminAuthInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/7/30 15:49
 */
@Mapper
public interface AdminMapper extends BaseMapper<SysAdmin> {

    AdminAuthInfo.Info findByAuthUserId(Long id);

}
