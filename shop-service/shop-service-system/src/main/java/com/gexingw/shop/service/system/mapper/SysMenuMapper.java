package com.gexingw.shop.service.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gexingw.shop.service.system.entity.SysMenu;
import com.gexingw.shop.service.system.vo.SysMenuInfoVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/7/31 13:22
 */
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    List<Long> queryMenuIdsByUserId(Long userId);

    List<SysMenuInfoVO> queryMenuInfoByPidAndIds(long pid, List<Long> ids);

}
