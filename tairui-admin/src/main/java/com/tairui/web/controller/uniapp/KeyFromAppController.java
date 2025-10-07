package com.tairui.web.controller.uniapp;

import com.tairui.common.core.domain.AjaxResult;
import com.tairui.function.domain.dto.KeyApplyDto;
import com.tairui.function.domain.dto.KeyApprovalDto;
import com.tairui.function.domain.vo.KeyAndLastApplyVo;
import com.tairui.function.domain.vo.KeyWorkflowDetailVo;
import com.tairui.function.service.IKeyFromAppService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app/keys")
@Tag(name = "钥匙操作")
public class KeyFromAppController {
    @Autowired
    private IKeyFromAppService keyFromAppService;

    @GetMapping("/getKeyList")
    @Operation(summary = "获取钥匙列表", description = "因为每个用户和钥匙的绑定和借用情况不一样，所以这个接口针对单个用户定制查询")
    public AjaxResult keyList(@RequestParam(name = "userId") Integer userId) {
        List<KeyAndLastApplyVo> list = keyFromAppService.queryKeyAndLastBorrowApplyMyLastBindingApplyList(userId);
        return AjaxResult.success("查询成功", list);
    }

    @PostMapping("/keyApply")
    @Operation(summary = "钥匙绑定/使用申请")
    public AjaxResult keyApply(@Validated @RequestBody KeyApplyDto keyApply) {
        keyFromAppService.keyApply(keyApply);
        return AjaxResult.success("钥匙" + (keyApply.getApplyType() == 0 ? "绑定" : "借用") + "申请成功");
    }

    @PutMapping("/keyApproval")
    @Operation(summary = "钥匙绑定/使用审批")
    public AjaxResult keyApproval(@Validated @RequestBody KeyApprovalDto keyApproval) {
        keyFromAppService.keyApproval(keyApproval,true);
        return AjaxResult.success("审批成功");
    }

    @GetMapping("/myApplyList")
    @Operation(summary = "获取我的所有申请")
    public AjaxResult getMyApplyList(@Parameter(description = "申请人用户ID", required = true) @RequestParam(name = "currentUserId") Integer userId) {
        List<KeyWorkflowDetailVo> keyWorkflowDetailVoList = keyFromAppService.getApplyListByApplyUserId(userId);
        return AjaxResult.success("获取成功", keyWorkflowDetailVoList);
    }

    @GetMapping("myApprovalList")
    @Operation(summary = "获取我的所有审批")
    public AjaxResult getMyApproval(@Parameter(description = "审批人用户ID", required = true) @RequestParam(name = "currentUserId") Integer userId) {
        List<KeyWorkflowDetailVo> keyWorkflowDetailVoList = keyFromAppService.getApprovalByApprovalUserId(userId);
        return AjaxResult.success("获取成功", keyWorkflowDetailVoList);
    }

}
