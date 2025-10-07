package com.tairui.enums;

/**
 * 申请类型
 */
public enum ApplyType {
    BIND(0),    // 绑定
    BORROW(1);  // 借用

    private final int code;

    ApplyType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static ApplyType of(Integer code) {
        for (ApplyType t : values()) {
            if (t.code == code) return t;
        }
        throw new IllegalArgumentException("未知申请类型: " + code);
    }
}
