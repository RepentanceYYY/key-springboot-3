package com.tairui.function.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tairui.common.annotation.Excel;
import com.tairui.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 钥匙柜配置对象 system_settings
 *
 * @author yhs
 * @date 2025-07-26
 */
public class SystemSettings extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键id */

    private Long id;


    @Excel(name = "绑定柜子", readConverterExp = "0=未绑定,1=已绑定")
    private int bindStatus;
    /** 序列号 */
    @Excel(name = "序列号")
    private String keyNumber;

    /** 柜子名称 */
    @Excel(name = "柜子名称")
    private String cabinetName;

    /** 柜子容量 */
    @Excel(name = "柜子容量",suffix="位")
    private String cabinetCapacity;

    /** 管理密码 */
    private String adminPassword;

    /** 定时重启（开/关） */
    @Excel(name = "定时重启", readConverterExp = "enable=开,disable=关")
    private String scheduledReboot;

    /** 重启时间(HH:MM) */
    @Excel(name = "重启时间")
    private String rebootTime;

    /** 网络模式(单机/联网) */
    private String networkMode;

    /** 钥匙申请(开/关) */
    @Excel(name = "钥匙申请", readConverterExp = "enable=开,disable=关")
    private String keyRequest;

    /** 借复审(开/关) */
    @Excel(name = "借复审", readConverterExp = "enable=开,disable=关")
    private String borrowReview;

    /** 归还周期(开/关) */
    @Excel(name = "归还周期", readConverterExp = "enable=开,disable=关")
    private String returnCycle;

    /** 归还时间 */
    @Excel(name = "归还时间",suffix="天")
    private String returnTime;

    /** 超时提醒(开/关) */
    @Excel(name = "超时提醒", readConverterExp = "enable=开,disable=关")
    private String timeoutReminder;

    /** 返回主页时长 */
    @Excel(name = "返回主页时长",suffix="分钟")
    private String returnToHome;

    /** 语音播报状态 */
    @Excel(name = "语音播报状态", readConverterExp = "enable=开,disable=关")
    private String voiceAnnouncement;

    @Excel(name = "所绑定用户")
    private String relatedUserNames;

    private String relatedUserIds;
    /** 最近更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "最近更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date lastUpdateTime;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }

    public void setKeyNumber(String keyNumber)
    {
        this.keyNumber = keyNumber;
    }

    public String getKeyNumber()
    {
        return keyNumber;
    }
    public void setCabinetName(String cabinetName)
    {
        this.cabinetName = cabinetName;
    }

    public String getCabinetName()
    {
        return cabinetName;
    }
    public void setCabinetCapacity(String cabinetCapacity)
    {
        this.cabinetCapacity = cabinetCapacity;
    }

    public String getCabinetCapacity()
    {
        return cabinetCapacity;
    }
    public void setAdminPassword(String adminPassword)
    {
        this.adminPassword = adminPassword;
    }

    public String getAdminPassword()
    {
        return adminPassword;
    }
    public void setScheduledReboot(String scheduledReboot)
    {
        this.scheduledReboot = scheduledReboot;
    }

    public String getScheduledReboot()
    {
        return scheduledReboot;
    }
    public void setRebootTime(String rebootTime)
    {
        this.rebootTime = rebootTime;
    }

    public String getRebootTime()
    {
        return rebootTime;
    }
    public void setNetworkMode(String networkMode)
    {
        this.networkMode = networkMode;
    }

    public String getNetworkMode()
    {
        return networkMode;
    }
    public void setKeyRequest(String keyRequest)
    {
        this.keyRequest = keyRequest;
    }

    public String getKeyRequest()
    {
        return keyRequest;
    }
    public void setBorrowReview(String borrowReview)
    {
        this.borrowReview = borrowReview;
    }

    public String getBorrowReview()
    {
        return borrowReview;
    }
    public void setReturnCycle(String returnCycle)
    {
        this.returnCycle = returnCycle;
    }

    public String getReturnCycle()
    {
        return returnCycle;
    }
    public void setReturnTime(String returnTime)
    {
        this.returnTime = returnTime;
    }

    public String getReturnTime()
    {
        return returnTime;
    }
    public void setTimeoutReminder(String timeoutReminder)
    {
        this.timeoutReminder = timeoutReminder;
    }

    public String getTimeoutReminder()
    {
        return timeoutReminder;
    }
    public void setReturnToHome(String returnToHome)
    {
        this.returnToHome = returnToHome;
    }

    public String getReturnToHome()
    {
        return returnToHome;
    }
    public void setVoiceAnnouncement(String voiceAnnouncement)
    {
        this.voiceAnnouncement = voiceAnnouncement;
    }

    public String getVoiceAnnouncement()
    {
        return voiceAnnouncement;
    }
    public void setLastUpdateTime(Date lastUpdateTime)
    {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Date getLastUpdateTime()
    {
        return lastUpdateTime;
    }

    public String getRelatedUserNames() {
        return relatedUserNames;
    }

    public void setRelatedUserNames(String relatedUserNames) {
        this.relatedUserNames = relatedUserNames;
    }

    public String getRelatedUserIds() {
        return relatedUserIds;
    }

    public void setRelatedUserIds(String relatedUserIds) {
        this.relatedUserIds = relatedUserIds;
    }


    public int getBindStatus() {
        return bindStatus;
    }

    public void setBindStatus(int bindStatus) {
        this.bindStatus = bindStatus;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("keyNumber", getKeyNumber())
            .append("cabinetName", getCabinetName())
            .append("cabinetCapacity", getCabinetCapacity())
            .append("adminPassword", getAdminPassword())
            .append("scheduledReboot", getScheduledReboot())
            .append("rebootTime", getRebootTime())
            .append("networkMode", getNetworkMode())
            .append("keyRequest", getKeyRequest())
            .append("borrowReview", getBorrowReview())
            .append("returnCycle", getReturnCycle())
            .append("returnTime", getReturnTime())
            .append("timeoutReminder", getTimeoutReminder())
            .append("returnToHome", getReturnToHome())
            .append("voiceAnnouncement", getVoiceAnnouncement())
            .append("lastUpdateTime", getLastUpdateTime())
                .append("relatedUserNames", getRelatedUserNames())
                .append("relatedUserIds", getRelatedUserIds())
                .append("bindStatus", getBindStatus())
            .toString();
    }
}
