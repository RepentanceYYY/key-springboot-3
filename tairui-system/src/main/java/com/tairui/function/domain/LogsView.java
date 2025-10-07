package com.tairui.function.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tairui.common.annotation.Excel;
import com.tairui.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 操作记录对象 logs_view
 *
 * @author tairui
 * @date 2025-07-31
 */
public class LogsView extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Long id;

    /** 操作时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "操作时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createAt;

    /** 操作类型 */
    @Excel(name = "操作类型")
    private String type;

    /** 操作内容 */
    @Excel(name = "操作内容")
    private String action;

    /** 操作用户 */
    @Excel(name = "操作用户")
    private String user;

    /** 相关钥匙 */
    @Excel(name = "相关钥匙")
    private String code;

    /** 状态 */
    @Excel(name = "状态",readConverterExp = "True=成功,False=失败")
    private String status;

    /** 操作柜子 */
    @Excel(name = "操作柜子")
    private String srttings;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setCreateAt(Date createAt)
    {
        this.createAt = createAt;
    }

    public Date getCreateAt()
    {
        return createAt;
    }
    public void setType(String type)
    {
        this.type = type;
    }

    public String getType()
    {
        return type;
    }
    public void setAction(String action)
    {
        this.action = action;
    }

    public String getAction()
    {
        return action;
    }
    public void setUser(String user)
    {
        this.user = user;
    }

    public String getUser()
    {
        return user;
    }
    public void setCode(String code)
    {
        this.code = code;
    }

    public String getCode()
    {
        return code;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }
    public void setSrttings(String srttings)
    {
        this.srttings = srttings;
    }

    public String getSrttings()
    {
        return srttings;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("createAt", getCreateAt())
                .append("type", getType())
                .append("action", getAction())
                .append("user", getUser())
                .append("code", getCode())
                .append("status", getStatus())
                .append("srttings", getSrttings())
                .toString();
    }
}
