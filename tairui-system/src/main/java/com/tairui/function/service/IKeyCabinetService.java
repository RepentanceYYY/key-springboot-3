package com.tairui.function.service;

import com.tairui.function.domain.dto.KeyCabinetPermissionDTO;
import com.tairui.function.domain.vo.KeyCabinetIncludeKeyListVo;
import com.tairui.function.domain.vo.KeyCabinetsUsersPermissionVo;

import java.util.List;

public interface IKeyCabinetService {
    /**
     * 通过用户id查询绑定的钥匙柜列表
     *
     * @param userId
     * @return
     */
    public List<KeyCabinetIncludeKeyListVo> selectKeyCabinetIncludeKeyList(Integer userId);

    /**
     * 管理用户的钥匙柜操作权限
     *
     * @param keyCabinetPermissionDTO
     */
    public void manageUserOwnKeyCabinetPermissions(KeyCabinetPermissionDTO keyCabinetPermissionDTO);

    /**
     * 查询钥匙柜以及用户(非管理员)列表(无论有没有权限)
     *
     * @return
     */
    List<KeyCabinetsUsersPermissionVo> queryKeyCabinetAndUserList();

    KeyCabinetsUsersPermissionVo queryKeyCabinetAndUserListById(Integer keyCabinetId);
}
