package com.tairui.function.domain.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 流程单详情&&所在流程单&&对应钥匙&&钥匙柜
 * 用在连表查出来的数据
 * mapper数据映射和入参用
 */
@Data
public class KeyWorkflowDetailAndKeyPOJO {
    /**
     * 流程主键
     */
    private Long workflowId;

    /**
     * 流程号
     */
    private String workflowNo;

    /**
     * 申请人id
     */
    private Long applyUserId;
    /**
     * 申请人名称
     */
    private String applyUserName;

    /**
     * 申请时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date applyTime;
    /**
     * 申请类型
     */
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
    private Long approvalUserId;
    /**
     * 审批人名称
     */
    private String approvalUserName;

    /**
     * 审批时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date approvalTime;

    /**
     * 审批意见
     */
    private String approvalComment;

    /**
     * 申请审批流当前状态:
     * 0=待审批,
     * 1=已通过
     * ,2=未通过,
     * 3=已超时
     */
    private Long workflowCurrentStatus;
    /**
     * 申请审批流离线数据
     */
    private String offlineData;
    /**
     * 钥匙在申请审批流中使用状态
     * 0=未开始
     * 1=进行中
     * 2=已结束
     */
    private Integer detailStatus;
    /**
     * 审批审批流详情离线数据
     */
    private String detailOfflineData;
    /**
     * 钥匙id
     */
    private Integer keyId;
    /**
     * 钥匙编号
     */
    private String keyCode;
    /**
     * 钥匙名称
     */
    private String keyName;
    /**
     * 钥匙在柜子中的位置
     */
    private Integer keyPosition;
    /**
     * 钥匙使用位置
     */
    private String keyUsageLocation;
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
    private Date keyCreateAt;
    /**
     * 最后操作人id
     */
    private Integer keyLastControlUserId;
    /**
     * 钥匙状态
     * 在位 availabe 离位 borrowed 异常error 未绑定 unbound
     */
    private String keyStatus;

    /**
     * 所在钥匙柜Id
     */
    private Integer keyCabinetId;
    /**
     * 所在钥匙柜编号
     */
    private Long keyCabinetNumber;
    /**
     * 所在钥匙柜名称
     */
    private String keyCabinetName;
    /**
     * 所在钥匙柜的地址
     */
    private String keyCabinetAddressLocation;
    /**
     * 目标用户id
     */
    private Integer targetUserId;
}
