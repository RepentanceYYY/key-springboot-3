package com.tairui.function.service;

import com.tairui.function.domain.AuthSettings;

import java.util.List;

/**
 * 高级配置Service接口
 *
 * @author tairui
 * @date 2025-07-29
 */
public interface IAuthSettingsService
{
    /**
     * 查询高级配置
     *
     * @param id 高级配置主键
     * @return 高级配置
     */
    public AuthSettings selectAuthSettingsById(Long id);

    /**
     * 查询高级配置列表
     *
     * @param authSettings 高级配置
     * @return 高级配置集合
     */
    public List<AuthSettings> selectAuthSettingsList(AuthSettings authSettings);

    /**
     * 新增高级配置
     *
     * @param authSettings 高级配置
     * @return 结果
     */
    public int insertAuthSettings(AuthSettings authSettings);

    /**
     * 修改高级配置
     *
     * @param authSettings 高级配置
     * @return 结果
     */
    public int updateAuthSettings(AuthSettings authSettings);

    /**
     * 批量删除高级配置
     *
     * @param ids 需要删除的高级配置主键集合
     * @return 结果
     */
    public int deleteAuthSettingsByIds(Long[] ids);

    /**
     * 删除高级配置信息
     *
     * @param id 高级配置主键
     * @return 结果
     */
    public int deleteAuthSettingsById(Long id);
}
