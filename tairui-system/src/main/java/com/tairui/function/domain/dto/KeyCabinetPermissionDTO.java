package com.tairui.function.domain.dto;

import lombok.Data;

import jakarta.validation.constraints.*;


/**
 * 分配/解除钥匙柜权限dto
 */
@Data
public class KeyCabinetPermissionDTO {
    /**
     * 钥匙柜ID
     */
    @NotNull(message = "钥匙柜ID不能为空")
    private Integer keyCabinetId;

    /**
     * 用户ID集合
     */
    @NotNull(message = "用户ID数组不能为空")
    private Integer[] userIds;

    /**
     * 操作类型：0=解除授权，1=授权
     */
    @NotNull(message = "操作类型不能为空")
    @Min(value = 0, message = "操作类型只能是0.解除授权,1.分配")
    @Max(value = 1, message = "操作类型只能是0.解除授权,1.分配")
    private Integer actionType;
}
