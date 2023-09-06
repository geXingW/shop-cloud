package com.gexingw.shop.service.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/7/30 11:47
 */
@Data
@TableName("admin")
public class SysAdmin {

    private Long id;

    private Long authUserId;

    private String nickName;

    private Long deptId;

    private Long jobId;

    private String username;

    private Boolean admin;

}
