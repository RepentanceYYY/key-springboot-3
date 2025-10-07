package com.tairui.function.domain.base;

import com.tairui.function.domain.KeyAndKeyCabinet;
import lombok.Data;

import java.util.Date;

/**
 * 钥匙&&所在钥匙柜&&对应最后一条工作流详情
 */
@Data
public class KeyAndKeyWorkflowUserCabinet extends KeyAndKeyCabinet {

    /**
     * 工作流详情单中钥匙状态
     */
    private Integer keyWorkflowDetailStatus;
    /**
     * 钥匙在工作流中的申请类型:0.绑定；1.借用
     */
    private Integer keyWorkflowDetailApplyType;
    /**
     * 最后一行工单详情的时间
     */

    private Date keyWorkflowDetailLastTimer;
}
