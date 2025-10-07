package com.tairui.web.controller.function;

import com.tairui.common.annotation.Log;
import com.tairui.common.core.controller.BaseController;
import com.tairui.common.core.domain.AjaxResult;
import com.tairui.common.core.page.TableDataInfo;
import com.tairui.common.enums.BusinessType;
import com.tairui.common.utils.poi.ExcelUtil;
import com.tairui.function.domain.KeyWorkflow;
import com.tairui.function.service.IKeyWorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.*;
import java.util.List;

/**
 * 钥匙审批流Controller
 * 
 * @author tairui
 * @date 2025-09-15
 */
@RestController
@RequestMapping("/function/workflow")
public class KeyWorkflowController extends BaseController
{
    @Autowired
    private IKeyWorkflowService keyWorkflowService;

    /**
     * 查询钥匙审批流列表
     */
    @PreAuthorize("@ss.hasPermi('function:workflow:list')")
    @GetMapping("/list")
    public TableDataInfo list(KeyWorkflow keyWorkflow)
    {
        startPage();
        List<KeyWorkflow> list = keyWorkflowService.selectKeyWorkflowList(keyWorkflow);
        return getDataTable(list);
    }

    /**
     * 导出钥匙审批流列表
     */
    @PreAuthorize("@ss.hasPermi('function:workflow:export')")
    @Log(title = "钥匙审批流", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, KeyWorkflow keyWorkflow)
    {
        List<KeyWorkflow> list = keyWorkflowService.selectKeyWorkflowList(keyWorkflow);
        ExcelUtil<KeyWorkflow> util = new ExcelUtil<KeyWorkflow>(KeyWorkflow.class);
        util.exportExcel(response, list, "钥匙审批流数据");
    }

    /**
     * 获取钥匙审批流详细信息
     */
    @PreAuthorize("@ss.hasPermi('function:workflow:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(keyWorkflowService.selectKeyWorkflowById(id));
    }

    /**
     * 新增钥匙审批流
     */
    @PreAuthorize("@ss.hasPermi('function:workflow:add')")
    @Log(title = "钥匙审批流", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody KeyWorkflow keyWorkflow)
    {
        return toAjax(keyWorkflowService.insertKeyWorkflow(keyWorkflow));
    }

    /**
     * 修改钥匙审批流
     */
    @PreAuthorize("@ss.hasPermi('function:workflow:edit')")
    @Log(title = "钥匙审批流", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody KeyWorkflow keyWorkflow)
    {
        return toAjax(keyWorkflowService.updateKeyWorkflow(keyWorkflow));
    }

    /**
     * 删除钥匙审批流
     */
    @PreAuthorize("@ss.hasPermi('function:workflow:remove')")
    @Log(title = "钥匙审批流", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(keyWorkflowService.deleteKeyWorkflowByIds(ids));
    }
}
