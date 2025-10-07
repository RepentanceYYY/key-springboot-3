package com.tairui.function.domain;

import lombok.Data;

import java.util.Date;

/**
 * 钥匙申请审批流程详情对象 key_workflow_detail
 *
 * @author tairui
 * @date 2025-09-18
 */
@Data
public class KeyWorkflowDetail {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 流程单id
     */
    private Long keyWorkflowId;

    /**
     * 钥匙柜id
     */
    private Long keyCabinetId;

    /**
     * 钥匙id
     */
    private Long keyId;

    /**
     * 申请类型：0.绑定钥匙1.借用钥匙
     */
    private Long applyType;

    /**
     * 进行状态：0.未开始1.进行中2.结束
     */
    private Long status;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 离线数据
     */
    private String offlineData;
}
