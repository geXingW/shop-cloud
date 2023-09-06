package com.gexingw.shop.common.db.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.gexingw.shop.common.core.constant.DateTimeConstant;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/7/30 16:56
 */
@Data
@Accessors(chain = true)
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = -2804259149113981621L;

    @TableId
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @TableField(fill = FieldFill.INSERT)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long createUser;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = DateTimeConstant.STANDARD_DATE_TIME_FORMAT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.UPDATE)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long updateUser;

    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = DateTimeConstant.STANDARD_DATE_TIME_FORMAT)
    private LocalDateTime updateTime;

    @TableLogic
    @JsonIgnore
    private Boolean deleted;

}
