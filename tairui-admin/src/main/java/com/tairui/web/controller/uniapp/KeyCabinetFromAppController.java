package com.tairui.web.controller.uniapp;

import com.tairui.common.core.domain.AjaxResult;
import com.tairui.function.domain.dto.KeyCabinetPermissionDTO;
import com.tairui.function.domain.vo.KeyCabinetIncludeKeyListVo;
import com.tairui.function.domain.vo.KeyCabinetsUsersPermissionVo;
import com.tairui.function.service.IKeyCabinetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app/keyCabinets")
@Tag(name = "钥匙柜操作")
public class KeyCabinetFromAppController {
    @Autowired
    private IKeyCabinetService keyCabinetService;

    @GetMapping("/getMyBindingKeyCabinet")
    @Operation(summary = "获取我绑定的钥匙柜", description = "返回钥匙柜信息列表，以及每个钥匙柜中已初始化的钥匙列表")
    public AjaxResult getMyBindingKeyCabinetIncludeInitializedKeyList(@RequestParam(name = "userId") Integer userId) {
        List<KeyCabinetIncludeKeyListVo> list = keyCabinetService.selectKeyCabinetIncludeKeyList(userId);
        return AjaxResult.success("查询成功", list);
    }

    @PostMapping("/updateKeyCabinetPermissions")
    @Operation(summary = "更新钥匙柜操作权限（分配/解除）", description = "如果是解除钥匙柜权限，那么会同时解除与该钥匙柜下钥匙的绑定，以及自动拒绝该钥匙柜下所有钥匙的申请(绑定和借用)")
    public AjaxResult updateKeyCabinetPermissions(@Validated @RequestBody KeyCabinetPermissionDTO dto) {
        keyCabinetService.manageUserOwnKeyCabinetPermissions(dto);
        return AjaxResult.success("更新成功", keyCabinetService.queryKeyCabinetAndUserListById(dto.getKeyCabinetId()));
    }

    @GetMapping("/keyCabinets/usersPermissions")
    @Operation(summary = "获取钥匙柜列表及用户(非管理员)权限信息")
    public AjaxResult queryKeyCabinetAndUserList() {
        List<KeyCabinetsUsersPermissionVo> keyCabinetsUsersPermissionList = keyCabinetService.queryKeyCabinetAndUserList();
        return AjaxResult.success("获取成功", keyCabinetsUsersPermissionList);
    }

    @GetMapping("/keyCabinetById/usersPermissions")
    @Operation(summary = "通过id获取钥匙柜以及用户(非管理员)权限信息")
    public AjaxResult queryKeyCabinetAndUserListById(@RequestParam("keyCabinetId") Integer keyCabinetId) {
        return AjaxResult.success("查询成功", keyCabinetService.queryKeyCabinetAndUserListById(keyCabinetId));
    }
}
