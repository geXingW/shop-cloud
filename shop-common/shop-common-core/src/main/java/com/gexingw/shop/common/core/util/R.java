package com.gexingw.shop.common.core.util;

import lombok.Getter;

import java.io.Serializable;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/7/8 16:57
 */
@Getter
@SuppressWarnings("unused")
public class R<T> implements Serializable {

    private static final long serialVersionUID = -554069322802646072L;

    private final int code;

    private final boolean success;

    private final T data;

    private final String message;

    public R(int code, boolean success, String message, T data) {
        this.code = code;
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public static <T> R<T> ok() {
        return new R<>(RespCode.SUCCESS.getCode(), true, RespCode.SUCCESS.getMessage(), null);
    }

    public static <T> R<T> ok(String message) {
        return new R<>(RespCode.SUCCESS.getCode(), true, message, null);
    }

    public static <T> R<T> ok(T data) {
        return new R<>(RespCode.SUCCESS.getCode(), true, RespCode.SUCCESS.getMessage(), data);
    }

    public static <T> R<T> ok(T data, String message) {
        return new R<>(RespCode.SUCCESS.getCode(), true, message, data);
    }

    public static <T> R<T> fail(String message) {
        return new R<>(RespCode.ERROR.getCode(), true, message, null);
    }

    public static <T> R<T> fail(RespCode respCode) {
        return new R<>(respCode.getCode(), false, respCode.getMessage(), null);
    }

    public static <T> R<T> fail(RespCode respCode, String message) {
        return new R<>(respCode.getCode(), false, message, null);
    }

    public static <T> R<T> fail(int code, String message) {
        return new R<>(code, true, message, null);
    }

    public static <T> R<T> fail(T data, int code, String message) {
        return new R<>(code, true, message, data);
    }

}
