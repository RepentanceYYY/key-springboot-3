package com.tairui.function.domain.vo;

import com.tairui.common.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class UserLeftJoinKeyVo implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键id
     */
    private Long id;

    /**
     * 名称
     */
    @Excel(name = "用户名称")
    private String userNikeName;

    /**
     * 账号/用户名
     */
    @Excel(name = "账号/用户名")
    private String userName;

    /**
     * 性别
     */
    @Excel(name = "性别")
    private String userGender;

    /**
     * 创建时间
     */
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    //@Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date userCreatedAt;

    /**
     * 状态
     */
    @Excel(name = "状态", readConverterExp = "true=正常,false=停用")
    private String userActive;

    /**
     * 角色
     */
    @Excel(name = "角色")
    private String role;

    /**
     * 卡号信息
     */
    @Excel(name = "卡号信息")
    private String cardInfo;
    @Excel(name = "指纹信息")
    private String fingerPrintInfo;
    @Excel(name = "人脸信息")
    private String faceInfo;

    /**
     * 密码
     */
    // @Excel(name = "密码")
    private String passWord;
    /**
     * 配置ID
     */
    private String srttings;
    @Excel(name = "绑定的钥匙位")
    private String keyPosition;
    @Excel(name = "绑定的钥匙名称")
    private String keyName;
    @Excel(name = "绑定的钥匙编号")
    private String keyCode;
}
