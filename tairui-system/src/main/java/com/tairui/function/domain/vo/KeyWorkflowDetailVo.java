package com.tairui.function.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 申请审批流中钥匙详情
 */
@Data
public class KeyWorkflowDetailVo {
    /**
     * 申请单id
     */
    private Integer id;
    /**
     * 申请单号
     */
    private String workflowNo;
    /**
     * 申请用户id
     */
    private Integer applyUserId;
    /**
     * 申请用户名称
     */
    private String applyUserName;
    /**
     * 申请时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date applyTime;
    /**
     * 申请事由
     */
    private String applyReason;
    /**
     * 预计使用时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date expectedTime;
    /**
     * 审批用户id
     */
    private Integer approvalUserId;
    /**
     * 审批用户名称
     */
    private String approvalUserName;
    /**
     * 审批时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date approvalTime;
    /**
     * 审批意见
     */
    private String approvalComment;
    /**
     * 工单状态
     */
    private Integer currentStatus;
    /**
     * 离线数据
     */
    private String offlineData;
    /**
     * 包含的详情列表
     */
    private List<WorkflowDetailsAndKeyAndKeyCabinet> workflowDetailsAndKeyList;
}
