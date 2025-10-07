package com.tairui.utils;

/**
 * 常量字典
 */
public class BizConstants {
    /**
     * 申请有效期(分钟)
     */
    public static final int APPLY_TIMEOUT_MINUTES = 120;
    /**
     * 绑定申请类型值
     */
    public static final int APPLY_TYPE_BIND = 0;
    /**
     * 借用申请类型值
     */
    public static final int APPLY_TYPE_BORROW = 1;
    /**
     * 绑定申请结束详情状态值
     */
    public static final int BIND_END_DETAIL_STATUS = 2;
    /**
     * 借用已审批但未领取状态值
     */
    public static final int BORROW_APPROVED_NOT_PICKED = 1;
    /**
     * 借用同意并已领走钥匙，或借用被拒绝状态值
     */
    public static final Integer BORROW_APPROVED_PICKED_OR_REJECTED = 2;
    /**
     * 未申请过
     */
    public static final Integer NOT_APPLIED_DETAIL_STATUS = null;
    /**
     * 待审批
     */
    public static final Integer PENDING_APPROVAL = 0;
    /**
     * 同意
     */
    public static final Integer APPROVED = 1;
    /**
     * 拒绝
     */
    public static final Integer REJECTED = 2;
    /**
     * 超时
     */
    public static final Integer TIMEOUT = 3;
    /**
     * 钥匙在位
     */
    public static final String KEY_AVAILABLE = "available";
    /**
     * 钥匙离位
     */
    public static final String KEY_BORROWED = "borrowed";
    /**
     * 钥匙异常
     */
    public static final String KEY_ERROR = "error";
    /**
     * 钥匙未绑定
     */
    public static final String KEY_UNBOUND = "unbound";
    /**
     * 管理员
     */
    public static final String ADMIN = "管理员";
    /**
     * 普通用户
     */
    public static final String USER = "普通用户";

}
