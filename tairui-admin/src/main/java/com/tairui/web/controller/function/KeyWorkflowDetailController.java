package com.tairui.web.controller.function;

import com.tairui.common.annotation.Log;
import com.tairui.common.core.controller.BaseController;
import com.tairui.common.core.domain.AjaxResult;
import com.tairui.common.core.page.TableDataInfo;
import com.tairui.common.enums.BusinessType;
import com.tairui.common.utils.poi.ExcelUtil;
import com.tairui.function.domain.KeyWorkflowDetail;
import com.tairui.function.service.IKeyWorkflowDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.*;
import java.util.List;

/**
 * 钥匙申请审批流程详情
 * Controller
 *
 * @author tairui
 * @date 2025-09-18
 */
@RestController
@RequestMapping("/function/keyWorkflowDetail")
public class KeyWorkflowDetailController extends BaseController {
    @Autowired
    private IKeyWorkflowDetailService keyWorkflowDetailService;

    /**
     * 查询钥匙申请审批流程详情
     * 列表
     */
    @PreAuthorize("@ss.hasPermi('function:keyWorkflowDetail:list')")
    @GetMapping("/list")
    public TableDataInfo list(KeyWorkflowDetail keyWorkflowDetail) {
        startPage();
        List<KeyWorkflowDetail> list = keyWorkflowDetailService.selectKeyWorkflowDetailList(keyWorkflowDetail);
        return getDataTable(list);
    }

    /**
     * 导出钥匙申请审批流程详情列表
     */
    @PreAuthorize("@ss.hasPermi('function:keyWorkflowDetail:export')")
    @Log(title = "钥匙申请审批流程详情 ", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, KeyWorkflowDetail keyWorkflowDetail) {
        List<KeyWorkflowDetail> list = keyWorkflowDetailService.selectKeyWorkflowDetailList(keyWorkflowDetail);
        ExcelUtil<KeyWorkflowDetail> util = new ExcelUtil<KeyWorkflowDetail>(KeyWorkflowDetail.class);
        util.exportExcel(response, list, "钥匙申请审批流程详情 数据");
    }

    /**
     * 获取钥匙申请审批流程详情详细信息
     */
    @PreAuthorize("@ss.hasPermi('function:keyWorkflowDetail:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(keyWorkflowDetailService.selectKeyWorkflowDetailById(id));
    }

    /**
     * 新增钥匙申请审批流程详情
     */
    @PreAuthorize("@ss.hasPermi('function:keyWorkflowDetail:add')")
    @Log(title = "钥匙申请审批流程详情 ", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody KeyWorkflowDetail keyWorkflowDetail) {
        return toAjax(keyWorkflowDetailService.insertKeyWorkflowDetail(keyWorkflowDetail));
    }

    /**
     * 修改钥匙申请审批流程详情
     */
    @PreAuthorize("@ss.hasPermi('function:keyWorkflowDetail:edit')")
    @Log(title = "钥匙申请审批流程详情 ", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody KeyWorkflowDetail keyWorkflowDetail) {
        return toAjax(keyWorkflowDetailService.updateKeyWorkflowDetail(keyWorkflowDetail));
    }

    /**
     * 删除钥匙申请审批流程详情
     */
    @PreAuthorize("@ss.hasPermi('function:keyWorkflowDetail:remove')")
    @Log(title = "钥匙申请审批流程详情 ", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(keyWorkflowDetailService.deleteKeyWorkflowDetailByIds(ids));
    }
}
