package com.tairui.function.domain;

import com.tairui.common.annotation.Excel;
import com.tairui.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 高级配置对象 auth_settings
 *
 * @author tairui
 * @date 2025-07-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AuthSettings extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**  */
    private Long srttings;

    /** 人脸认证 */
    @Excel(name = "人脸认证")
    private String faceAuth;

    /** 活体认证 */
    @Excel(name = "活体认证")
    private String livenessAuth;

    /** 指纹认证 */
    @Excel(name = "指纹认证")
    private String fingerprintAuth;

    /** 刷卡认证 */
    @Excel(name = "刷卡认证")
    private String cardAuth;

    /** 密码认证 */
    @Excel(name = "密码认证")
    private String passwordAuth;

    /** 酒精检测 */
    @Excel(name = "酒精检测")
    private String alcoholDetection;

    /** 酒精检测阈值 */
    @Excel(name = "酒精检测阈值")
    private String alcoholThreshold;

    /** 错位模式 */
    @Excel(name = "错位模式")
    private String misalignmentMode;

    /** 小屏幕 */
    @Excel(name = "小屏幕")
    private String smallScreen;
}
