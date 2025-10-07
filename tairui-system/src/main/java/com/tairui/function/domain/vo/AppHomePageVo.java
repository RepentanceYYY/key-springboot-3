package com.tairui.function.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * 小程序首页数据
 */
@Data
public class AppHomePageVo {
    /**
     * 钥匙钥匙统计概要
     */
    private KeySummaryStatisticsVO keySummaryStatistics;
    /**
     * 我的钥匙柜权限
     */
    private List<KeyCabinetIncludeKeyListVo> keyCabinetIncludeKeysList;
    /**
     * 我的申请
     */
    private List<KeyWorkflowDetailVo> applyList;
    /**
     * 待我审批
     */
    private List<KeyWorkflowDetailVo> awaitApprovalList;
}
