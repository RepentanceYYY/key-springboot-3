package com.tairui.function.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tairui.common.annotation.Excel;

import java.util.Date;

/**
 * @author Yhs
 * @date 2025/7/291559
 */
public class AuthSystemSettings {

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

    /** 最近更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "最近更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date lastUpdateTime;

    @Excel(name = "人脸认证", readConverterExp = "enable=开,disable=关")
    private String faceAuth;

    /** 活体认证 */
    @Excel(name = "活体认证", readConverterExp = "enable=开,disable=关")
    private String livenessAuth;

    /** 指纹认证 */
    @Excel(name = "指纹认证", readConverterExp = "enable=开,disable=关")
    private String fingerprintAuth;

    /** 刷卡认证 */
    @Excel(name = "刷卡认证", readConverterExp = "enable=开,disable=关")
    private String cardAuth;

    /** 密码认证 */
    @Excel(name = "密码认证", readConverterExp = "enable=开,disable=关")
    private String passwordAuth;

    /** 酒精检测 */
    @Excel(name = "酒精检测", readConverterExp = "enable=开,disable=关")
    private String alcoholDetection;

    /** 酒精检测阈值 */
    @Excel(name = "酒精检测阈值")
    private String alcoholThreshold;

    /** 酒精检测阈值 */
    @Excel(name = "酒精检测", readConverterExp = "enable=开,disable=关")
    private String misalignmentMode;

    /** 小屏幕 */
    @Excel(name = "小屏幕", readConverterExp = "enable=开,disable=关")
    private String smallScreen;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKeyNumber() {
        return keyNumber;
    }

    public void setKeyNumber(String keyNumber) {
        this.keyNumber = keyNumber;
    }

    public String getCabinetName() {
        return cabinetName;
    }

    public void setCabinetName(String cabinetName) {
        this.cabinetName = cabinetName;
    }

    public String getCabinetCapacity() {
        return cabinetCapacity;
    }

    public void setCabinetCapacity(String cabinetCapacity) {
        this.cabinetCapacity = cabinetCapacity;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    public String getScheduledReboot() {
        return scheduledReboot;
    }

    public void setScheduledReboot(String scheduledReboot) {
        this.scheduledReboot = scheduledReboot;
    }

    public String getRebootTime() {
        return rebootTime;
    }

    public void setRebootTime(String rebootTime) {
        this.rebootTime = rebootTime;
    }

    public String getNetworkMode() {
        return networkMode;
    }

    public void setNetworkMode(String networkMode) {
        this.networkMode = networkMode;
    }

    public String getKeyRequest() {
        return keyRequest;
    }

    public void setKeyRequest(String keyRequest) {
        this.keyRequest = keyRequest;
    }

    public String getBorrowReview() {
        return borrowReview;
    }

    public void setBorrowReview(String borrowReview) {
        this.borrowReview = borrowReview;
    }

    public String getReturnCycle() {
        return returnCycle;
    }

    public void setReturnCycle(String returnCycle) {
        this.returnCycle = returnCycle;
    }

    public String getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(String returnTime) {
        this.returnTime = returnTime;
    }

    public String getTimeoutReminder() {
        return timeoutReminder;
    }

    public void setTimeoutReminder(String timeoutReminder) {
        this.timeoutReminder = timeoutReminder;
    }

    public String getReturnToHome() {
        return returnToHome;
    }

    public void setReturnToHome(String returnToHome) {
        this.returnToHome = returnToHome;
    }

    public String getVoiceAnnouncement() {
        return voiceAnnouncement;
    }

    public void setVoiceAnnouncement(String voiceAnnouncement) {
        this.voiceAnnouncement = voiceAnnouncement;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getFaceAuth() {
        return faceAuth;
    }

    public void setFaceAuth(String faceAuth) {
        this.faceAuth = faceAuth;
    }

    public String getLivenessAuth() {
        return livenessAuth;
    }

    public void setLivenessAuth(String livenessAuth) {
        this.livenessAuth = livenessAuth;
    }

    public String getFingerprintAuth() {
        return fingerprintAuth;
    }

    public void setFingerprintAuth(String fingerprintAuth) {
        this.fingerprintAuth = fingerprintAuth;
    }

    public String getCardAuth() {
        return cardAuth;
    }

    public void setCardAuth(String cardAuth) {
        this.cardAuth = cardAuth;
    }

    public String getPasswordAuth() {
        return passwordAuth;
    }

    public void setPasswordAuth(String passwordAuth) {
        this.passwordAuth = passwordAuth;
    }

    public String getAlcoholDetection() {
        return alcoholDetection;
    }

    public void setAlcoholDetection(String alcoholDetection) {
        this.alcoholDetection = alcoholDetection;
    }

    public String getAlcoholThreshold() {
        return alcoholThreshold;
    }

    public void setAlcoholThreshold(String alcoholThreshold) {
        this.alcoholThreshold = alcoholThreshold;
    }

    public String getMisalignmentMode() {
        return misalignmentMode;
    }

    public void setMisalignmentMode(String misalignmentMode) {
        this.misalignmentMode = misalignmentMode;
    }

    public String getSmallScreen() {
        return smallScreen;
    }

    public void setSmallScreen(String smallScreen) {
        this.smallScreen = smallScreen;
    }

    public int getBindStatus() {
        return bindStatus;
    }

    public void setBindStatus(int bindStatus) {
        this.bindStatus = bindStatus;
    }

    @Override
    public String toString() {
        return "AuthSystemSettings{" +
                "id=" + id +
                ", bindStatus=" + bindStatus +
                ", keyNumber='" + keyNumber + '\'' +
                ", cabinetName='" + cabinetName + '\'' +
                ", cabinetCapacity='" + cabinetCapacity + '\'' +
                ", adminPassword='" + adminPassword + '\'' +
                ", scheduledReboot='" + scheduledReboot + '\'' +
                ", rebootTime='" + rebootTime + '\'' +
                ", networkMode='" + networkMode + '\'' +
                ", keyRequest='" + keyRequest + '\'' +
                ", borrowReview='" + borrowReview + '\'' +
                ", returnCycle='" + returnCycle + '\'' +
                ", returnTime='" + returnTime + '\'' +
                ", timeoutReminder='" + timeoutReminder + '\'' +
                ", returnToHome='" + returnToHome + '\'' +
                ", voiceAnnouncement='" + voiceAnnouncement + '\'' +
                ", lastUpdateTime=" + lastUpdateTime +
                ", faceAuth='" + faceAuth + '\'' +
                ", livenessAuth='" + livenessAuth + '\'' +
                ", fingerprintAuth='" + fingerprintAuth + '\'' +
                ", cardAuth='" + cardAuth + '\'' +
                ", passwordAuth='" + passwordAuth + '\'' +
                ", alcoholDetection='" + alcoholDetection + '\'' +
                ", alcoholThreshold='" + alcoholThreshold + '\'' +
                ", misalignmentMode='" + misalignmentMode + '\'' +
                ", smallScreen='" + smallScreen + '\'' +
                '}';
    }
}
