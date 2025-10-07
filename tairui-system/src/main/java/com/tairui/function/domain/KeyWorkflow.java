package com.tairui.function.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tairui.common.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * 钥匙审批流对象 key_workflow
 * 
 * @author tairui
 * @date 2025-09-15
 */
@Data
public class KeyWorkflow
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 流程号 */
    @Excel(name = "流程号")
    private String workflowNo;

    /** 申请人id */
    @Excel(name = "申请人id")
    private Long applyUserId;

    /** 申请时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "申请时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date applyTime;

    /** 申请事由 */
    @Excel(name = "申请事由")
    private String applyReason;

    /** 预计使用时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "预计使用时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date expectedTime;

    /** 审批人id */
    @Excel(name = "审批人id")
    private Long approvalUserId;

    /** 审批时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "审批时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date approvalTime;

    /** 审批意见 */
    @Excel(name = "审批意见")
    private String approvalComment;

    /** 申请审批流当前状态:0=待审批,1=已通过,2=未通过,3=已超时 */
    @Excel(name = "申请审批流当前状态")
    private Long currentStatus;
    /**
     * 离线数据
     */
    private String offlineData;
}
