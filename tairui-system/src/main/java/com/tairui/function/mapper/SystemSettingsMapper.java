package com.tairui.function.mapper;

import com.tairui.function.domain.AuthSystemSettings;
import com.tairui.function.domain.SystemSettings;

import java.util.List;

/**
 * 钥匙柜配置Mapper接口
 *
 * @author yhs
 * @date 2025-07-26
 */
public interface SystemSettingsMapper
{
    /**
     * 查询钥匙柜配置
     *
     * @param id 钥匙柜配置主键
     * @return 钥匙柜配置
     */
    public SystemSettings selectSystemSettingsById(Long id);

    /**
     * 查询钥匙柜配置列表
     *
     * @param systemSettings 钥匙柜配置
     * @return 钥匙柜配置集合
     */
    public List<SystemSettings> selectSystemSettingsList(SystemSettings systemSettings);
    public List<SystemSettings> selectAllSystemSettings();

    /**
     * 新增钥匙柜配置
     *
     * @param systemSettings 钥匙柜配置
     * @return 结果
     */
    public int insertSystemSettings(SystemSettings systemSettings);

    /**
     * 获取刚插入的 ID
     *
     */
    public int lastInsertId();

    /**
     * 修改钥匙柜配置
     *
     * @param systemSettings 钥匙柜配置
     * @return 结果
     */
    public int updateSystemSettings(SystemSettings systemSettings);

    /**
     * 删除钥匙柜配置
     *
     * @param id 钥匙柜配置主键
     * @return 结果
     */
    public int deleteSystemSettingsById(Long id);

    /**
     * 批量删除钥匙柜配置
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSystemSettingsByIds(Long[] ids);

    /**
     * 检查序列号是否唯一
     *
     * @param keyNumber 序列号
     * @return 结果
     */
    public  SystemSettings checkKeyNumberUnique(String keyNumber);

    /**
     * 查询钥匙柜配置+高级配置
     *
     * @param authSystemSettings 钥匙柜配置+高级配置
     * @return 钥匙柜配置+高级配置
     */
    public List<AuthSystemSettings> selectAuthSystemSettingsList(AuthSystemSettings authSystemSettings);
}
