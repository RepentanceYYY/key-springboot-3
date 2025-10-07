package com.tairui.function.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import jakarta.validation.constraints.*;

import java.util.Date;

/**
 * 钥匙申请dto
 */
@Data
public class KeyApplyDto {
    /**
     * 申请人id
     */
    @NotNull(message = "申请人id不能为空")
    private Integer applyUserId;
    /**
     * 钥匙柜id
     */
    @NotNull(message = "钥匙柜id不能为空")
    private Integer keyCabinetId;
    /**
     * 钥匙ids
     */
    @NotNull(message = "申请钥匙列表不能为空")
    private Integer[] keyIds;
    /**
     * 申请类型：0.绑定钥匙 1.借用钥匙
     */
    @NotNull(message = "申请类型不能为空")
    @Min(value = 0, message = "申请类型只能是0.绑定或1.借用")
    @Max(value = 1, message = "申请类型只能是0.绑定或1.借用")
    private Integer applyType;
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
     * 审批人id
     */
    @NotNull(message = "审批人id不能为空")
    private Integer approvalUserId;
}
