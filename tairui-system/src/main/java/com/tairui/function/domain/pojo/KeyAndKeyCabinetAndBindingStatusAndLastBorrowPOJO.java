package com.tairui.function.domain.pojo;

import com.tairui.function.domain.KeyAndKeyCabinet;
import lombok.Data;

import java.util.List;

/**
 * 查询mapper用的，不想在mapper写死参数所以定义了pojo
 */
@Data
public class KeyAndKeyCabinetAndBindingStatusAndLastBorrowPOJO extends KeyAndKeyCabinet {
    /**
     * 要查询的用户id
     */
    private Integer userId;
    /**
     * 是否绑定
     */
    private Integer isBinding;
    /**
     * 最后借用的工作流中申请类型
     */
    private Integer lastKeyWorkflowDetailApplyType;
    /**
     * 最后借用工作流详情状态
     */
    private Integer lastBorrowingWorkflowDetailsStatus;
    /**
     *  限制查询的钥匙柜ids
     */
    private List<Integer> keyCabinetIds;
}
