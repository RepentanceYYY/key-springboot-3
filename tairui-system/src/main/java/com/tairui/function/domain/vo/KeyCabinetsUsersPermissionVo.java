package com.tairui.function.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * 钥匙柜以及有无权限的用户列表
 */
@Data
public class KeyCabinetsUsersPermissionVo {
    /**
     * 钥匙柜Id
     */
    private Integer keyCabinetId;
    /**
     * 钥匙柜编号
     */
    private Long keyCabinetNumber;
    /**
     * 钥匙柜名称
     */
    private String keyCabinetName;
    /**
     * 钥匙柜实际位置
     */
    private String keyCabinetAddressLocation;
    /**
     * 用户总数
     */
    private Integer userCount;
    /**
     * 拥有操作权限的用户总数
     */
    private Integer ownPermissionUserCount;
    /**
     * 用户和是否有权限
     */
    private List<UserPermissionVo> userPermissionList;
}
