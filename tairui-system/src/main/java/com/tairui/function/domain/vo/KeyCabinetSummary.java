package com.tairui.function.domain.vo;

import lombok.Data;

/**
 * 钥匙柜概要
 */
@Data
public class KeyCabinetSummary {
    /**
     * 钥匙柜Id
     */
    private Integer keyCabinetId;
    /**
     * 钥匙柜编号
     */
    private Long keyCabinetNumber;
    /**
     * 钥匙柜名称
     */
    private String keyCabinetName;
    /**
     * 钥匙柜所在地理位置
     */
    private String keyCabinetAddressLocation;
    /**
     * 已初始化钥匙总数
     */
    private Integer initializedKeySum;
    /**
     * 可借用钥匙总数
     */
    private Integer canBorrowCount;
}
