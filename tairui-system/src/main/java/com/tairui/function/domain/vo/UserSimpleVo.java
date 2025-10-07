package com.tairui.function.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * mapper用
 */
@Data
public class UserSimpleVo {
    /**
     *用户id
     */
    private Integer userId;
    /**
     * 用户昵称
     */
    private String userNikeName;
    /**
     * 用户名
     */
    private String userName;
    /**
     *是否激活
     */
    private String userActive;
    /**
     * 用户角色
     */
    private String userRole;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date userCreatedAt;
    /**
     *钥匙柜ids(英文逗号分割)
     */
    private String keyCabinetsStr;
}
