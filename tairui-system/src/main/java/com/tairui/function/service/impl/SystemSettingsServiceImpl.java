package com.tairui.function.service.impl;

import com.tairui.common.constant.UserConstants;
import com.tairui.common.utils.StringUtils;
import com.tairui.function.domain.AuthSettings;
import com.tairui.function.domain.AuthSystemSettings;
import com.tairui.function.domain.SystemSettings;
import com.tairui.function.mapper.SystemSettingsMapper;
import com.tairui.function.service.IAuthSettingsService;
import com.tairui.function.service.ISystemSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 钥匙柜配置Service业务层处理
 *
 * @author yhs
 * @date 2025-07-26
 */
@Service
public class SystemSettingsServiceImpl implements ISystemSettingsService
{
    @Autowired
    private SystemSettingsMapper systemSettingsMapper;

    @Autowired
    private IAuthSettingsService iAuthSettingsService;
    /**
     * 查询钥匙柜配置
     *
     * @param id 钥匙柜配置主键
     * @return 钥匙柜配置
     */
    @Override
    public SystemSettings selectSystemSettingsById(Long id)
    {
        return systemSettingsMapper.selectSystemSettingsById(id);
    }

    /**
     * 查询钥匙柜配置列表
     *
     * @param systemSettings 钥匙柜配置
     * @return 钥匙柜配置
     */
    @Override
    public List<SystemSettings> selectSystemSettingsList(SystemSettings systemSettings)
    {
        return systemSettingsMapper.selectSystemSettingsList(systemSettings);
    }

    /**
     * 新增钥匙柜配置
     *
     * @param systemSettings 钥匙柜配置
     * @return 结果
     *             `lastUpdateTime` = NOW();
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertSystemSettings(SystemSettings systemSettings)
    {
        systemSettings.setScheduledReboot("enable");
        systemSettings.setRebootTime("00:00");
        systemSettings.setNetworkMode("standalone");
        systemSettings.setKeyRequest("disable");
        systemSettings.setBorrowReview("disable");
        systemSettings.setReturnCycle("disable");
        systemSettings.setReturnTime("1");
        systemSettings.setTimeoutReminder("disable");
        systemSettings.setReturnToHome("3");
        systemSettings.setVoiceAnnouncement("enable");
        int insertCount   = systemSettingsMapper.insertSystemSettings(systemSettings);
        if (insertCount <= 0) {

            return  insertCount;
        }
        int id = systemSettingsMapper.lastInsertId();
        AuthSettings authSettings = new AuthSettings();
        authSettings.setId((long) id);
        return   iAuthSettingsService.insertAuthSettings(authSettings);
    }

    /**
     * 修改钥匙柜配置
     *
     * @param systemSettings 钥匙柜配置
     * @return 结果
     */
    @Override
    public int updateSystemSettings(SystemSettings systemSettings)
    {
        return systemSettingsMapper.updateSystemSettings(systemSettings);
    }

    /**
     * 批量删除钥匙柜配置
     *
     * @param ids 需要删除的钥匙柜配置主键
     * @return 结果
     */
    @Override
    public int deleteSystemSettingsByIds(Long[] ids)
    {
        return systemSettingsMapper.deleteSystemSettingsByIds(ids);
    }

    /**
     * 删除钥匙柜配置信息
     *
     * @param id 钥匙柜配置主键
     * @return 结果
     */
    @Override
    public int deleteSystemSettingsById(Long id)
    {
        return systemSettingsMapper.deleteSystemSettingsById(id);
    }

    /**
     * 检查序列号是否唯一
     *
     * @param systemSettings 钥匙柜配置
     * @return 结果
     */

    public boolean checkKeyNumberUnique(SystemSettings systemSettings)
    {
        Long id = StringUtils.isNull(systemSettings.getId()) ? -1L : systemSettings.getId();
        SystemSettings info = systemSettingsMapper.checkKeyNumberUnique(systemSettings.getKeyNumber());
        if (StringUtils.isNotNull(info) && info.getId().longValue() != id.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 查询钥匙柜配置+高级配置
     *
     * @param authSystemSettings 钥匙柜配置+高级配置
     * @return 钥匙柜配置+高级配置
     */
    @Override
    public List<AuthSystemSettings> selectAuthSystemSettingsList(AuthSystemSettings authSystemSettings) {
        return systemSettingsMapper.selectAuthSystemSettingsList(authSystemSettings);
    }
}
