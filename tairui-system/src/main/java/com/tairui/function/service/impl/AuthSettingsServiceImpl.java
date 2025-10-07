package com.tairui.function.service.impl;

import com.tairui.common.constant.Constants;
import com.tairui.common.utils.StringUtils;
import com.tairui.function.domain.AuthSettings;
import com.tairui.function.domain.Key;
import com.tairui.function.mapper.AuthSettingsMapper;
import com.tairui.function.mapper.KeyMapper;
import com.tairui.function.service.IAuthSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 高级配置Service业务层处理
 *
 * @author tairui
 * @date 2025-07-29
 */
@Service
public class AuthSettingsServiceImpl implements IAuthSettingsService
{
    @Autowired
    private AuthSettingsMapper authSettingsMapper;
    @Autowired
    private KeyMapper keyMapper;

    /**
     * 查询高级配置
     *
     * @param id 高级配置主键
     * @return 高级配置
     */
    @Override
    public AuthSettings selectAuthSettingsById(Long id)
    {
        return authSettingsMapper.selectAuthSettingsById(id);
    }

    /**
     * 查询高级配置列表
     *
     * @param authSettings 高级配置
     * @return 高级配置
     */
    @Override
    public List<AuthSettings> selectAuthSettingsList(AuthSettings authSettings)
    {
        return authSettingsMapper.selectAuthSettingsList(authSettings);
    }

    /**
     * 新增高级配置
     *
     * @param authSettings 高级配置
     * @return 结果
     */
    @Override
    public int insertAuthSettings(AuthSettings authSettings)
    {
        authSettings.setFaceAuth("enable");
        authSettings.setLivenessAuth("disable");
        authSettings.setFingerprintAuth("enable");
        authSettings.setCardAuth("enable");
        authSettings.setPasswordAuth("enable");
        authSettings.setAlcoholDetection("disable");
        authSettings.setAlcoholThreshold("20");
        authSettings.setMisalignmentMode("enable");
        authSettings.setSmallScreen("disable");
        return authSettingsMapper.insertAuthSettings(authSettings);
    }

    /**
     * 修改高级配置
     *
     * @param authSettings 高级配置
     * @return 结果
     */
    @Override
    public int updateAuthSettings(AuthSettings authSettings) {
        // 关闭错位模式
        if (authSettings.getMisalignmentMode().equals(Constants.DISABLE)) {
            // 将key表中，status的error状态改为，available unbound
            // name 为 ”未命名钥匙“ ， 表示 unbound， 否则为  available
            Key keyParam = new Key();
            keyParam.setStatus(Constants.ERROR);
            List<Key> keys = keyMapper.selectKeyList(keyParam);
            for (Key key : keys) {
                if (Constants.NULL_NAME.equals(key.getName()) || StringUtils.isEmpty(key.getName())) {
                    key.setStatus(Constants.UNBOUND);
                } else {
                    key.setStatus(Constants.AVAILABLE);
                }
                // 进行修改操作
                keyMapper.updateKey(key);
            }
        }

        // 开启错位模式
        if (authSettings.getMisalignmentMode().equals(Constants.ENABLE)) {
            // 将key表中，将available unbound的状态，改为error
            // 当key表中，currentTag != tag 时，为 error
            Key keyParam = new Key();
            keyParam.setStatus(Constants.AVAILABLE);
            List<Key> availableKeys = keyMapper.selectKeyList(keyParam);
            for (Key availableKey : availableKeys) {
                if (StringUtils.isEmpty(availableKey.getCurrentTag()) || !availableKey.getCurrentTag().equals(availableKey.getTag())) {
                    availableKey.setStatus(Constants.ERROR);
                    keyMapper.updateKey(availableKey);
                }
            }
            keyParam.setStatus(Constants.UNBOUND);
            List<Key> unboundKeys = keyMapper.selectKeyList(keyParam);
            for (Key unboundKey : unboundKeys) {
                if (StringUtils.isEmpty(unboundKey.getCurrentTag()) || !unboundKey.getCurrentTag().equals(unboundKey.getTag())) {
                    unboundKey.setStatus(Constants.ERROR);
                    keyMapper.updateKey(unboundKey);
                }
            }
        }
        return authSettingsMapper.updateAuthSettings(authSettings);
    }

    /**
     * 批量删除高级配置
     *
     * @param ids 需要删除的高级配置主键
     * @return 结果
     */
    @Override
    public int deleteAuthSettingsByIds(Long[] ids)
    {
        return authSettingsMapper.deleteAuthSettingsByIds(ids);
    }

    /**
     * 删除高级配置信息
     *
     * @param id 高级配置主键
     * @return 结果
     */
    @Override
    public int deleteAuthSettingsById(Long id)
    {
        return authSettingsMapper.deleteAuthSettingsById(id);
    }
}
