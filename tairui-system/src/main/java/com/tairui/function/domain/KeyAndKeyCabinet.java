package com.tairui.function.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 钥匙&&所在钥匙柜
 */
@Data
public class KeyAndKeyCabinet {
    /**
     * 钥匙id
     */
    private Integer keyId;

    /**
     * 钥匙名称
     */
    private String keyName;

    /**
     * 钥匙编号
     */
    private String keyCode;

    /**
     * 钥匙在柜子中的位置
     */
    private Integer keyPosition;

    /**
     * 钥匙使用位置
     */
    private String keyUsageLocation;

    /**
     * 钥匙状态
     * 在位 available，离位 borrowed，异常 error，未绑定 unbound
     */
    private String keyStatus;

    /**
     * 绑定标签
     */
    private String keyTag;

    /**
     * 实时标签
     */
    private String keyCurrentTag;

    /**
     * 控制板地址
     */
    private String keyControlPanelAddress;

    /**
     * 单元板地址
     */
    private String keyCircuitPanelAddress;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date keyCreateAt;

    /**
     * 最后操作人id
     */
    private Integer keyLastControlUserId;

    /**
     * 最后操作时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date keyLastControlTime;

    /**
     * 所在钥匙柜id
     */
    private int keyCabinetId;
    /**
     * 所在钥匙柜编号
     */
    private Long keyCabinetNumber;

    /**
     * 所在钥匙柜名称
     */
    private String keyCabinetName;

    /**
     * 所在钥匙柜的存放位置
     */
    private String keyCabinetAddressLocation;
}
