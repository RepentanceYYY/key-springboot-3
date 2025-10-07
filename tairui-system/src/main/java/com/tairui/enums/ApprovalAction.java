package com.tairui.enums;

/**
 * 审批动作
 */
public enum ApprovalAction {
    APPROVE(1),  // 同意
    REJECT(2),   // 拒绝
    TIMEOUT(3);  // 超时

    private final int code;

    ApprovalAction(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static ApprovalAction of(Integer code) {
        for (ApprovalAction a : values()) {
            if (a.code == code) return a;
        }
        throw new IllegalArgumentException("未知审批动作: " + code);
    }
}
