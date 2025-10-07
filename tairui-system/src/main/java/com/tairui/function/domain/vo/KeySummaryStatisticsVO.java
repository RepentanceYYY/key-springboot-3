package com.tairui.function.domain.vo;

import lombok.Data;

/**
 * 钥匙统计概要 VO
 */
@Data
public class KeySummaryStatisticsVO {

    /**
     * 可用钥匙数量
     */
    private Integer canBorrowCount;

    /**
     * 待审批钥匙数量
     */
    private Integer pendingApprovalCount;

    /**
     * 今日使用钥匙数量
     */
    private Integer todayUsageCount;

    /**
     * 需归还钥匙数量
     */
    private Integer toReturnCount;
}
