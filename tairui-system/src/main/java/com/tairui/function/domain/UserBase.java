package com.tairui.function.domain;

import lombok.Data;

import java.util.Date;

@Data
public class UserBase {
    /**
     * 主键id
     */
    private Integer id;
    /**
     * 名称
     */
    private String name;
    /**
     * 账号/用户名
     */
    private String userName;
    /**
     * 性别
     */
    private String gender;
    /**
     * 创建时间
     */
    private Date createdAt;
    /**
     * 状态(正常/停用)
     */
    private String active;
    /**
     * 角色
     */
    private String role;
    /**
     * 卡号信息
     */
    private String cardInfo;
    /**
     * 指纹信息
     */
    private String fingerPrintInfo;
    /**
     * 人脸信息
     */
    private String faceInfo;
    /**
     * 密码
     */
    private String passWord;
    /**
     * 配置ID system_settings
     */
    private String srttings;
    /**
     * 微信用户在此小程序Id
     */
    private String openId;
}
