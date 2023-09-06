package com.gexingw.shop.common.db.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/7/30 16:17
 */
@SuppressWarnings("unused")
public class BaseService<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> {

    /**
     * 根据Id查询记录是否存在
     *
     * @param id ID
     * @return 是否存在
     */
    public boolean exist(Long id) {
        return this.baseMapper.exists(new QueryWrapper<T>().eq("id", id));
    }

    /**
     * 按条件查询
     *
     * @param queryWrapper 查询条件
     * @return 结果
     */
    public List<T> query(QueryWrapper<T> queryWrapper) {
        return this.baseMapper.selectList(queryWrapper);
    }

}
