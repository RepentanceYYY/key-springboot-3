package com.tairui.enums;

/**
 * 申请 | 审批状态枚举
 */
public enum WorkflowStatus {
    /**
     * 待审批
     */
    PENDING_APPROVAL(0L),
    /**
     * 通过
     */
    APPROVED(1L),
    /**
     * 拒绝
     */
    REJECTED(2L),
    /**
     * 超时
     */
    TIMEOUT(3L);

    private final Long code;

    WorkflowStatus(Long code) {
        this.code = code;
    }

    public Long getCode() {
        return code;
    }

    public static WorkflowStatus of(Long code) {
        for (WorkflowStatus status : values()) {
            if (status.code.equals(code)) return status;
        }
        throw new IllegalArgumentException("未知流程状态: " + code);
    }
}

