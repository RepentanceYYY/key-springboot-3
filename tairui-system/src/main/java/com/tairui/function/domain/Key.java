package com.tairui.function.domain;

import com.tairui.common.annotation.Excel;
import com.tairui.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 钥匙对象 key
 *
 * @author yhs
 * @date 2025-07-30
 */
@Data
public class Key extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Long id;

    @Excel(name = "所属柜子")
    private String srttingsName;

    /** 所属位置 */
    @Excel(name = "所属位置",suffix="位")
    private String position;

    /**
     * 地理位置
     */
    @Excel(name = "使用位置")
    private String usageLocation;

    /** 钥匙名称 */
    @Excel(name = "钥匙名称")
    private String name;

    /** 钥匙编号 */
    @Excel(name = "钥匙编号")
    private String code;

    @Excel(name = "所绑定用户")
    private String userNames;

    /** 绑定标签 */
    @Excel(name = "绑定标签")
    private String tag;

    /** 实时标签 */
    @Excel(name = "实时标签")
    private String currentTag;

    /** 状态  */
    @Excel(name = "状态", readConverterExp = "available=在位,borrowed=离位,error=异常,unbound=未绑定")
    private String status;

    /** 控制板地址 */
    private String controlPanelAddress;

    /** 单元板地址 */
    private String circuitPanelAddress;

    /** 创建时间 */
    private Date createAt;

    /** 借用人 */

    private Long lastControlUserId;

    @Excel(name = "借用人")
    private String lastControlUser;



    private String srttings;


    private String userId;
}
