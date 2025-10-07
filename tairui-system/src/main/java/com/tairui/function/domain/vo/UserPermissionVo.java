package com.tairui.function.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class UserPermissionVo {
    /**
     * 用户id
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
     * 用户创建是否
     */
    @JsonFormat(pattern = "yy-MM-dd HH:mm")
    private Date userCreatedAt;
    /**
     * 是否激活
     */
    private String userActive;
    /**
     * 是否拥有该钥匙柜权限
     */
    private Boolean ownPermission;
}
