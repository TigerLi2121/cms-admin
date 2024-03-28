package com.mm.common.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 返回数据
 *
 * @author lwl
 */
@Data
public class R<T> {

    /**
     * 响应码
     */
    private Integer code;

    /**
     * 响应消息
     */
    private String msg;

    /**
     * 响应数据
     */
    private T data;

    public R(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static <T> R<T> error() {
        return code(RCode.SYSTEM_ERR);
    }

    public static <T> R<T> error(String msg) {
        return code(RCode.SYSTEM_ERR, msg);
    }

    public static <T> R<T> code(int code, String msg) {
        return new R<>(code, msg);
    }

    public static <T> R<T> code(ICode iCode) {
        return new R<>(iCode.getCode(), iCode.getMsg());
    }

    public static <T> R<T> code(ICode iCode, String msg) {
        return new R<>(iCode.getCode(), msg);
    }

    public static <T> R<T> ok(T data) {
        R<T> r = code(RCode.SUCCESS);
        r.setData(data);
        return r;
    }

    public static <T> R.Page<T> ok(List<T> list, Integer total) {
        return new Page<>(RCode.SUCCESS.getCode(), total, list);
    }

    public static <T> R.Page<T> ok(IPage<T> iPage) {
        return new Page<>(RCode.SUCCESS.getCode(), iPage.getTotal(), iPage.getRecords());
    }

    public static <T> R<T> ok(T data, ICode iCode) {
        R<T> r = code(iCode);
        r.setData(data);
        return r;
    }

    public static <T> R<T> ok() {
        return code(RCode.SUCCESS);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Page<T> {

        /**
         * 响应码
         */
        private Integer code;

        /**
         * 总数
         */
        private long total;

        /**
         * 数据
         */
        private List<T> data;
    }
}
