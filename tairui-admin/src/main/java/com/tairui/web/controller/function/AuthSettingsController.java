package com.tairui.web.controller.function;

import com.tairui.common.annotation.Log;
import com.tairui.common.core.controller.BaseController;
import com.tairui.common.core.domain.AjaxResult;
import com.tairui.common.core.page.TableDataInfo;
import com.tairui.common.enums.BusinessType;
import com.tairui.common.utils.poi.ExcelUtil;
import com.tairui.function.domain.AuthSettings;
import com.tairui.function.service.IAuthSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.*;
import java.util.List;

/**
 * 高级配置Controller
 *
 * @author tairui
 * @date 2025-07-29
 */
@RestController
@RequestMapping("/function/auth")
public class AuthSettingsController extends BaseController
{
    @Autowired
    private IAuthSettingsService authSettingsService;

    /**
     * 查询高级配置列表
     */
    @PreAuthorize("@ss.hasPermi('function:auth:list')")
    @GetMapping("/list")
    public TableDataInfo list(AuthSettings authSettings)
    {
        startPage();
        List<AuthSettings> list = authSettingsService.selectAuthSettingsList(authSettings);
        return getDataTable(list);
    }

    /**
     * 导出高级配置列表
     */
    @PreAuthorize("@ss.hasPermi('function:auth:export')")
    @Log(title = "高级配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AuthSettings authSettings)
    {
        List<AuthSettings> list = authSettingsService.selectAuthSettingsList(authSettings);
        ExcelUtil<AuthSettings> util = new ExcelUtil<AuthSettings>(AuthSettings.class);
        util.exportExcel(response, list, "高级配置数据");
    }

    /**
     * 获取高级配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('function:auth:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(authSettingsService.selectAuthSettingsById(id));
    }

    /**
     * 新增高级配置
     */
    @PreAuthorize("@ss.hasPermi('function:auth:add')")
    @Log(title = "高级配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AuthSettings authSettings)
    {
        return toAjax(authSettingsService.insertAuthSettings(authSettings));
    }

    /**
     * 修改高级配置
     */
    @PreAuthorize("@ss.hasPermi('function:auth:edit')")
    @Log(title = "高级配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AuthSettings authSettings)
    {
        return toAjax(authSettingsService.updateAuthSettings(authSettings));
    }

    /**
     * 删除高级配置
     */
    @PreAuthorize("@ss.hasPermi('function:auth:remove')")
    @Log(title = "高级配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(authSettingsService.deleteAuthSettingsByIds(ids));
    }
}
