package com.tairui.function.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 钥匙柜表映射实体
 */
@Data
public class KeyCabinet {
    /**
     * id
     */
    private Integer id;
    /**
     * 柜子编号
     */
    private Long keyNumber;
    /**
     * 柜子名称
     */
    private String cabinetName;
    /**
     * 柜子容量
     */
    private String cabinetCapacity;
    /**
     * 柜子存放位置
     */
    private String addressLocation;
    /**
     * 管理密码
     */
    private String adminPassword;
    /**
     * 定时重启
     */
    private String scheduledReboot;
    /**
     * 重启时间
     */
    private String rebootTime;
    /**
     * 网络模式
     */
    private String networkMode;
    /**
     * 钥匙申请(开/关)
     */
    private String keyRequest;
    /**
     * 借复审(开/关)
     */
    private String borrowReview;
    /**
     * 归还周期(开/关)
     */
    private String returnCycle;
    /**
     * 归还时间
     */
    private String returnTime;
    /**
     * 超时提醒
     */
    private String timeoutReminder;
    /**
     * 返回主页
     */
    private String returnToHome;
    /**
     * 语音播报
     */
    private String voiceAnnouncement;
    /**
     * 最近更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastUpdateTime;
    /**
     * 判断是发和现场柜子做了绑定  0未绑定 1已绑定
     */
    private Integer bindStatus;
}
