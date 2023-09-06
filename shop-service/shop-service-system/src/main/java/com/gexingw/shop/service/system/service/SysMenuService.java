package com.gexingw.shop.service.system.service;

import com.gexingw.shop.common.db.service.BaseService;
import com.gexingw.shop.service.system.entity.SysMenu;
import com.gexingw.shop.service.system.mapper.SysMenuMapper;
import com.gexingw.shop.service.system.vo.SysMenuInfoVO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/7/31 13:23
 */
@Service
public class SysMenuService extends BaseService<SysMenuMapper, SysMenu> {

    public List<SysMenuInfoVO> queryTreeByUserId(Long userId) {
        List<Long> menuIds = baseMapper.queryMenuIdsByUserId(userId);

        // 递归查询子菜单
        return this.getChildrenMenusByPidAndIds(0L, menuIds);
    }

    private List<SysMenuInfoVO> getChildrenMenusByPidAndIds(long pid, List<Long> ids) {
        if (ids.isEmpty()) {
            return new ArrayList<>(0);
        }

        List<SysMenuInfoVO> menuInfoVOList = baseMapper.queryMenuInfoByPidAndIds(pid, ids);
        if (menuInfoVOList.isEmpty()) {
            return menuInfoVOList;
        }

        for (SysMenuInfoVO menuInfoVO : menuInfoVOList) {
            // 递归查询子菜单数据
            List<SysMenuInfoVO> childrenMenu = this.getChildrenMenusByPidAndIds(menuInfoVO.getId(), ids);
            if (childrenMenu.isEmpty()) {
                continue;
            }

            menuInfoVO.setChildren(childrenMenu);
        }

        return menuInfoVOList;
    }


}
