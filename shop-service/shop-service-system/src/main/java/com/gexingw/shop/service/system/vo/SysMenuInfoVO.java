package com.gexingw.shop.service.system.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/7/31 13:38
 */
@Data
public class SysMenuInfoVO implements Serializable {

    private static final long serialVersionUID = 5564463790807494340L;

    private Long id;

    private Long pid;

    private String name;

    private String title;

    private List<SysMenuInfoVO> children;

}
