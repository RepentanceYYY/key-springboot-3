package com.tairui.web.controller.function;

import com.tairui.common.annotation.Log;
import com.tairui.common.core.controller.BaseController;
import com.tairui.common.core.domain.AjaxResult;
import com.tairui.common.core.page.TableDataInfo;
import com.tairui.common.enums.BusinessType;
import com.tairui.common.utils.StringUtils;
import com.tairui.common.utils.poi.ExcelUtil;
import com.tairui.function.domain.AuthSystemSettings;
import com.tairui.function.domain.SystemSettings;
import com.tairui.function.service.ISystemSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.*;

import java.util.List;

/**
 * 钥匙柜配置Controller
 *
 * @author yhs
 * @date 2025-07-26
 */
@RestController
@RequestMapping("/function/keyConfiguration")
public class SystemSettingsController extends BaseController {
    @Autowired
    private ISystemSettingsService systemSettingsService;

    /**
     * 查询钥匙柜配置列表
     */
    @PreAuthorize("@ss.hasPermi('function:keyConfiguration:list')")
    @GetMapping("/list")
    public TableDataInfo list(SystemSettings systemSettings) {
        startPage();
        List<SystemSettings> list = systemSettingsService.selectSystemSettingsList(systemSettings);
        return getDataTable(list);
    }

    /**
     * 查询钥匙柜配置列表（已绑定 且没有分页）
     */
    @GetMapping("/list2")
    public TableDataInfo list2(SystemSettings systemSettings) {
        systemSettings.setBindStatus(1);
        List<SystemSettings> list = systemSettingsService.selectSystemSettingsList(systemSettings);
        return getDataTable(list);
    }

    /**
     * 导出钥匙柜配置列表
     */
    @PreAuthorize("@ss.hasPermi('function:keyConfiguration:export')")
    @Log(title = "钥匙柜配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AuthSystemSettings authSystemSettings) {
        List<AuthSystemSettings> list = systemSettingsService.selectAuthSystemSettingsList(authSystemSettings);
        ExcelUtil<AuthSystemSettings> util = new ExcelUtil<AuthSystemSettings>(AuthSystemSettings.class);
        util.exportExcel(response, list, "钥匙柜配置数据");
    }

    /**
     * 获取钥匙柜配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('function:keyConfiguration:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(systemSettingsService.selectSystemSettingsById(id));
    }

    /**
     * 新增钥匙柜配置
     */
    @PreAuthorize("@ss.hasPermi('function:keyConfiguration:add')")
    @Log(title = "钥匙柜配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SystemSettings systemSettings) {
        if (StringUtils.isNotEmpty(systemSettings.getKeyNumber()) && !systemSettingsService.checkKeyNumberUnique(systemSettings)) {
            return error("新增序列号'" + systemSettings.getKeyNumber() + "'失败，序列号已存在");
        }
        return toAjax(systemSettingsService.insertSystemSettings(systemSettings));
    }

    /**
     * 修改钥匙柜配置
     */
    @PreAuthorize("@ss.hasPermi('function:keyConfiguration:edit')")
    @Log(title = "钥匙柜配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SystemSettings systemSettings) {
        if (StringUtils.isNotEmpty(systemSettings.getKeyNumber()) && !systemSettingsService.checkKeyNumberUnique(systemSettings)) {
            return error("修改序列号'" + systemSettings.getKeyNumber() + "'失败，序列号已存在");
        }
        return toAjax(systemSettingsService.updateSystemSettings(systemSettings));
    }

    /**
     * 删除钥匙柜配置
     */
    @PreAuthorize("@ss.hasPermi('function:keyConfiguration:remove')")
    @Log(title = "钥匙柜配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(systemSettingsService.deleteSystemSettingsByIds(ids));
    }

    /**
     * 获取新的序列号
     *
     * @return
     */
    @GetMapping("/getNewKeyNumber")
    public AjaxResult getNewKeyNumber() {
        return AjaxResult.success("success", systemSettingsService.generateUniqueSystemSettingsKeyNumber());
    }
}
