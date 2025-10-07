package com.tairui.function.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tairui.function.domain.KeyAndKeyCabinet;
import lombok.Data;

import java.util.Date;

/**
 * 钥匙&&所在钥匙柜&&最后的绑定和借用申请
 */
@Data
public class KeyAndLastApplyVo extends KeyAndKeyCabinet {
    /**
     * 我的最后绑定工作流详情状态
     */
    private Integer lastBindingWorkflowDetailsStatus;
    /**
     * 我的最后绑定申请时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date lastBindingApplyTime;
    /**
     * 该钥匙最后借用工作流详情状态
     */
    private Integer lastBorrowingWorkflowDetailsStatus;
    /**
     * 该钥匙最后借用时间:借用申请->借用审批->同意后的待领取->同意后的领取时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date lastBorrowingTime;
}
