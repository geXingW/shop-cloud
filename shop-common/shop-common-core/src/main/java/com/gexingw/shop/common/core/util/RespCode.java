package com.gexingw.shop.common.core.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/7/8 16:58
 */
@Getter
@AllArgsConstructor
public enum RespCode {

    /**
     * 成功的响应
     */
    SUCCESS(200000, "成功！"),

    PARAM_MISS(400001, "缺少必要的请求参数！"),
    PARAM_TYPE_ERROR(400002, "请求参数格式错误！"),
    PARAM_BIND_ERROR(400003, "参数绑定错误！"),
    PARAM_VALID_ERROR(400004, "参数校验失败!"),
    MSG_NOT_READABLE(400005, "消息不能读取！"),
    RECORD_HAS_EXISTED(400006, "数据重复！"),

    UN_AUTHORIZATION(401000, "请先登录！"),
    AUTHORIZATION_EXPIRED(401001, "登录已过期，请重新登录！"),
    INVALID_CREDENTIALS(401002, "凭证无效，请检查！"),

    NOT_FOUND(404000, "未找到资源！"),
    SOURCE_NOT_FOUND(404001, "记录不存在！"),

    METHOD_NOT_SUPPORTED(405000, "不支持的请求方式！"),

    MEDIA_TYPE_NOT_SUPPORTED(415000, "不支持的媒体类型！"),

    /**
     * 失败的响应
     */
    ERROR(500000, "已失败！"),
    QUERY_ERROR(500010, "查询失败，稍后再试试吧！"),
    CREATE_ERROR(500011, "创建失败，稍后再试试吧！"),
    UPDATE_ERROR(500012, "更新失败，稍后再试试吧！"),
    DELETE_ERROR(500014, "删除失败，稍后再试试吧！"),

    /**
     * 用户模块-错误码
     * 范围：200100~200199
     */
    USER_OR_PASSWORD_ERROR(200100, "用户名或密码错误！"),
    VERIFY_CODE_ERROR(200100, "验证码错误！"),
    USER_HAS_EXIST_ERROR(200101, "用户已存在，请更换手机号重试"),

    /**
     * 运输模块-错误码
     * 范围：200300~200399
     */
    VEHICLE_BUSY(200300, "车辆运输中，不允许操作！"),


    ;
    private final int code;

    private final String message;


}

