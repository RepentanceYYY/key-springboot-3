package com.tairui.function.domain.dto;

import lombok.Data;

import jakarta.validation.constraints.*;


/**
 * 钥匙审批dto
 */
@Data
public class KeyApprovalDto {

    /**
     * 申请审批流程Id
     */
    @NotNull(message = "申请审批流程Id不能为空")
    private Integer keyWorkflowId;

    /**
     * 审批用户id
     */
    @NotNull(message = "审批用户Id不能为空")
    private Integer approvalUserId;

    /**
     * 审批意见
     */
    private String approvalComment;

    /**
     * 审批动作 1.同意 2.拒绝 2.超时
     * 请求时只能传1或者2
     */
    @NotNull(message = "审批动作不能为空")
    @Min(value = 1, message = "审批动作只能是1.同意或2.拒绝")
    @Max(value = 2, message = "审批动作只能是1.同意或2.拒绝")
    private Integer approvalAction;
}
