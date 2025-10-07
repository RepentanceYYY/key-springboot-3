package com.tairui.common.core.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tairui.common.annotation.Excel;
import com.tairui.common.core.domain.BaseEntity;

import java.util.Arrays;
import java.util.Date;

/**
 * 用户对象 user
 *
 * @author YHS
 * @date 2025-07-29
 */
public class User extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 配置ID  system_settings */
    @Excel(name = "钥匙柜绑定")
    private String srttings;
    /** 主键id */
    private Long id;

    /** 名称 */
    @Excel(name = "名称")
    private String name;

    /** 账号/用户名 */
    @Excel(name = "账号/用户名")
    private String userName;

    /** 性别 */
    @Excel(name = "性别")
    private String gender;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    /** 状态 */
    @Excel(name = "状态", readConverterExp = "true=正常,false=停用")
    private String active;

    /** 角色 */
    @Excel(name = "角色")
    private String role;

    /** 卡号信息 */
    @Excel(name = "卡号信息")
    private String cardInfo;

    /** 指纹信息 */
    private String fingerPrintInfo;

    /** 人脸信息 */
    private String faceInfo;

    /** 密码 */
    @Excel(name = "密码")
    private String passWord;

    @Excel(name = "钥匙码")
    private String keyCode;

    @Excel(name = "钥匙名称")
    private String keyName;

    @Excel(name = "钥匙位置")
    private String keyPosition;

    private Long[] srttingIds;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }

    public String getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(String keyCode) {
        this.keyCode = keyCode;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getKeyPosition() {
        return keyPosition;
    }

    public void setKeyPosition(String keyPosition) {
        this.keyPosition = keyPosition;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getUserName()
    {
        return userName;
    }
    public void setGender(String gender)
    {
        this.gender = gender;
    }

    public String getGender()
    {
        return gender;
    }
    public void setCreatedAt(Date createdAt)
    {
        this.createdAt = createdAt;
    }

    public Date getCreatedAt()
    {
        return createdAt;
    }
    public void setActive(String active)
    {
        this.active = active;
    }

    public String getActive()
    {
        return active;
    }
    public void setRole(String role)
    {
        this.role = role;
    }

    public String getRole()
    {
        return role;
    }
    public void setCardInfo(String cardInfo)
    {
        this.cardInfo = cardInfo;
    }

    public String getCardInfo()
    {
        return cardInfo;
    }
    public void setFingerPrintInfo(String fingerPrintInfo)
    {
        this.fingerPrintInfo = fingerPrintInfo;
    }

    public String getFingerPrintInfo()
    {
        return fingerPrintInfo;
    }
    public void setFaceInfo(String faceInfo)
    {
        this.faceInfo = faceInfo;
    }

    public String getFaceInfo()
    {
        return faceInfo;
    }
    public void setPassWord(String passWord)
    {
        this.passWord = passWord;
    }

    public String getPassWord()
    {
        return passWord;
    }
    public void setSrttings(String srttings)
    {
        this.srttings = srttings;
    }

    public String getSrttings()
    {
        return srttings;
    }


    public Long[] getSrttingIds() {
        return srttingIds;
    }

    public void setSrttingIds(Long[] srttingIds) {
        this.srttingIds = srttingIds;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", userName='" + userName + '\'' +
                ", gender='" + gender + '\'' +
                ", createdAt=" + createdAt +
                ", active='" + active + '\'' +
                ", role='" + role + '\'' +
                ", cardInfo='" + cardInfo + '\'' +
                ", fingerPrintInfo='" + fingerPrintInfo + '\'' +
                ", faceInfo='" + faceInfo + '\'' +
                ", passWord='" + passWord + '\'' +
                ", srttings='" + srttings + '\'' +
                ", srttingIds=" + Arrays.toString(srttingIds) +
                '}';
    }
}
