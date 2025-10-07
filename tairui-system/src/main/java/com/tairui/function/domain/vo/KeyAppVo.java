package com.tairui.function.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 小程序钥匙返回对象 vo
 */
@Data
public class KeyAppVo implements Serializable {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 所属位置
     */
    private String position;

    /**
     * 钥匙名称
     */
    private String name;

    /**
     * 钥匙编号
     */
    private String code;

    /**
     * 状态 available=在位,borrowed=离位,error=异常,unbound=未绑定
     */
    private String status;

    /**
     * 所属柜子id
     */
    private String srttings;

    /**
     * 所属柜子名称
     */
    private String srttingsName;
}
