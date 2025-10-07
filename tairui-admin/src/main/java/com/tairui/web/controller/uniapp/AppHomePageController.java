package com.tairui.web.controller.uniapp;

import com.tairui.common.core.domain.AjaxResult;
import com.tairui.function.domain.vo.AppHomePageVo;
import com.tairui.function.domain.vo.KeyCabinetIncludeKeyListVo;
import com.tairui.function.domain.vo.KeySummaryStatisticsVO;
import com.tairui.function.domain.vo.KeyWorkflowDetailVo;
import com.tairui.function.service.IKeyCabinetService;
import com.tairui.function.service.IKeyFromAppService;
import com.tairui.utils.BizConstants;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Tag(name = "首页服务")
public class AppHomePageController {

    @Autowired
    private IKeyFromAppService keyFromAppService;
    @Autowired
    private IKeyCabinetService keyCabinetService;

    @GetMapping("/getAppHomePageAllData")
    @Operation(summary = "获取首页所有数据")
    public AjaxResult getAppHomePageAllData(@Parameter(description = "登录用户ID", required = true) @RequestParam("userId") Integer userId) {

        AppHomePageVo allData = new AppHomePageVo();
        //  1.钥匙状态概要
        KeySummaryStatisticsVO keySummaryStatisticsVO = keyFromAppService.queryKeySummaryStatistics(userId);
        allData.setKeySummaryStatistics(keySummaryStatisticsVO);
        // 2.我的钥匙柜权限
        List<KeyCabinetIncludeKeyListVo> keyCabinetIncludeKeysList = keyCabinetService.selectKeyCabinetIncludeKeyList(userId);
        allData.setKeyCabinetIncludeKeysList(keyCabinetIncludeKeysList);
        //3.查询我申请和待我审批的
        List<KeyWorkflowDetailVo> myApplyAndApproval = keyFromAppService.queryTargetUserApplyOrApproval(userId);
        //3.1我的申请
        List<KeyWorkflowDetailVo> myApplyList = myApplyAndApproval.stream().filter(x -> x.getApplyUserId() == userId)
                .sorted(Comparator.comparing(KeyWorkflowDetailVo::getApplyTime))
                .limit(2)
                .collect(Collectors.toList());
        allData.setApplyList(myApplyList);

        //3.2待我的审批
        List<KeyWorkflowDetailVo> awaitApprovalToMe = myApplyAndApproval.stream().filter(x -> x.getApprovalUserId() == userId && x.getCurrentStatus() == BizConstants.PENDING_APPROVAL)
                .sorted(Comparator.comparing(KeyWorkflowDetailVo::getApplyTime))
                .limit(2)
                .collect(Collectors.toList());
        allData.setAwaitApprovalList(awaitApprovalToMe);
        return AjaxResult.success("获取成功", allData);
    }
}
