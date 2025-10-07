package com.tairui.function.domain;

import com.tairui.common.annotation.Excel;
import com.tairui.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 高级配置对象 auth_settings
 *
 * @author tairui
 * @date 2025-07-29
 */
public class AuthSettings extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**  */
    private Long id;

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

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setFaceAuth(String faceAuth)
    {
        this.faceAuth = faceAuth;
    }

    public String getFaceAuth()
    {
        return faceAuth;
    }
    public void setLivenessAuth(String livenessAuth)
    {
        this.livenessAuth = livenessAuth;
    }

    public String getLivenessAuth()
    {
        return livenessAuth;
    }
    public void setFingerprintAuth(String fingerprintAuth)
    {
        this.fingerprintAuth = fingerprintAuth;
    }

    public String getFingerprintAuth()
    {
        return fingerprintAuth;
    }
    public void setCardAuth(String cardAuth)
    {
        this.cardAuth = cardAuth;
    }

    public String getCardAuth()
    {
        return cardAuth;
    }
    public void setPasswordAuth(String passwordAuth)
    {
        this.passwordAuth = passwordAuth;
    }

    public String getPasswordAuth()
    {
        return passwordAuth;
    }
    public void setAlcoholDetection(String alcoholDetection)
    {
        this.alcoholDetection = alcoholDetection;
    }

    public String getAlcoholDetection()
    {
        return alcoholDetection;
    }
    public void setAlcoholThreshold(String alcoholThreshold)
    {
        this.alcoholThreshold = alcoholThreshold;
    }

    public String getAlcoholThreshold()
    {
        return alcoholThreshold;
    }
    public void setMisalignmentMode(String misalignmentMode)
    {
        this.misalignmentMode = misalignmentMode;
    }

    public String getMisalignmentMode()
    {
        return misalignmentMode;
    }
    public void setSmallScreen(String smallScreen)
    {
        this.smallScreen = smallScreen;
    }

    public String getSmallScreen()
    {
        return smallScreen;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("faceAuth", getFaceAuth())
                .append("livenessAuth", getLivenessAuth())
                .append("fingerprintAuth", getFingerprintAuth())
                .append("cardAuth", getCardAuth())
                .append("passwordAuth", getPasswordAuth())
                .append("alcoholDetection", getAlcoholDetection())
                .append("alcoholThreshold", getAlcoholThreshold())
                .append("misalignmentMode", getMisalignmentMode())
                .append("smallScreen", getSmallScreen())
                .toString();
    }
}
