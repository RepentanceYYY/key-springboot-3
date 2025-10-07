package com.tairui.function.domain;

import lombok.Data;

import java.util.Date;

/**
 * 用户微信推送消息模板绑定表
 */
@Data
public class UserSubscribe {
    /**
     * 系统用户ID
     */
    private Integer userId;

    /**
     * 微信用户openid
     */
    private String openId;

    /**
     * 消息模板ID
     */
    private String templateId;

    /**
     * 创建时间
     */
    private Date createTime;
}
