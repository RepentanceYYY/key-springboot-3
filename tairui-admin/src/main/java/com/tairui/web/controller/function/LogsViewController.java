package com.tairui.web.controller.function;

import com.tairui.common.annotation.Log;
import com.tairui.common.core.controller.BaseController;
import com.tairui.common.core.domain.AjaxResult;
import com.tairui.common.core.page.TableDataInfo;
import com.tairui.common.enums.BusinessType;
import com.tairui.common.utils.poi.ExcelUtil;
import com.tairui.function.domain.LogsView;
import com.tairui.function.service.ILogsViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.*;
import java.util.List;

/**
 * 操作记录Controller
 *
 * @author tairui
 * @date 2025-07-31
 */
@RestController
@RequestMapping("/function/logsView")
public class LogsViewController extends BaseController
{
    @Autowired
    private ILogsViewService logsViewService;

    /**
     * 查询操作记录列表
     */
    @PreAuthorize("@ss.hasPermi('function:logsView:list')")
    @GetMapping("/list")
    public TableDataInfo list(LogsView logsView)
    {
        startPage();
        List<LogsView> list = logsViewService.selectLogsViewList(logsView);
        return getDataTable(list);
    }

    /**
     * 导出操作记录列表
     */
    //@PreAuthorize("@ss.hasPermi('function:logsView:export')")
    //@Log(title = "操作记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, LogsView logsView)
    {
        List<LogsView> list = logsViewService.selectLogsViewList(logsView);
        ExcelUtil<LogsView> util = new ExcelUtil<LogsView>(LogsView.class);
        util.exportExcel(response, list, "操作记录","操作记录表");
    }

    /**
     * 获取操作记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('function:logsView:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(logsViewService.selectLogsViewById(id));
    }

    /**
     * 新增操作记录
     */
    @PreAuthorize("@ss.hasPermi('function:logsView:add')")
    @Log(title = "操作记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody LogsView logsView)
    {
        return toAjax(logsViewService.insertLogsView(logsView));
    }

    /**
     * 修改操作记录
     */
    @PreAuthorize("@ss.hasPermi('function:logsView:edit')")
    @Log(title = "操作记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody LogsView logsView)
    {
        return toAjax(logsViewService.updateLogsView(logsView));
    }

    /**
     * 删除操作记录
     */
    @PreAuthorize("@ss.hasPermi('function:logsView:remove')")
    @Log(title = "操作记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(logsViewService.deleteLogsViewByIds(ids));
    }
}
