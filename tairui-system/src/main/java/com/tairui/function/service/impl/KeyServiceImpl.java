package com.tairui.function.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.tairui.common.constant.UserConstants;
import com.tairui.common.core.domain.entity.User;
import com.tairui.common.utils.StringUtils;
import com.tairui.function.domain.Key;
import com.tairui.function.domain.SystemSettings;
import com.tairui.function.domain.vo.KeyAppVo;
import com.tairui.function.mapper.KeyMapper;
import com.tairui.function.mapper.SystemSettingsMapper;
import com.tairui.function.mapper.UserMapper;
import com.tairui.function.service.IKeyService;
import com.tairui.system.mapper.SysOperLogMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 钥匙Service业务层处理
 *
 * @author yhs
 * @date 2025-07-30
 */
@Service
public class KeyServiceImpl implements IKeyService
{
    @Autowired
    private KeyMapper keyMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SystemSettingsMapper systemSettingsMapper;
    @Autowired
    private SysOperLogMapper operLogMapper;
    /**
     * 查询钥匙
     *
     * @param id 钥匙主键
     * @return 钥匙
     */
    @Override
    public Key selectKeyById(Long id)
    {
        return keyMapper.selectKeyById(id);
    }

    /**
     * 查询钥匙列表
     *
     * @param key 钥匙
     * @return 钥匙
     */
    @Override
    public List<Key> selectKeyList(Key key)
    {
        return keyMapper.selectKeyList(key);
    }

    /**
     * 新增钥匙
     *
     * @param key 钥匙
     * @return 结果
     */
    @Override
    public int insertKey(Key key)
    {
        return keyMapper.insertKey(key);
    }

    /**
     * 修改钥匙
     *
     * @param key 钥匙
     * @return 结果
     */
    @Override
    public int updateKey(Key key)
    {
        keyMapper.deleteKeyUser(key.getId());
        if (key.getUserId() != null && !key.getUserId().isEmpty()) {
            // 按逗号分割字符串，得到字符串数组
            String[] userIds = key.getUserId().split(",");
            for (int i = 0; i < userIds.length; i++) {
                long userId = Long.parseLong(userIds[i].trim());
                keyMapper.insertKeyUser(key.getId(),userId);
            }
        }
        key.setStatus("available");
        return keyMapper.updateKey(key);
    }

    /**
     * 批量删除钥匙
     *
     * @param ids 需要删除的钥匙主键
     * @return 结果
     */
    @Override
    public int deleteKeyByIds(Long[] ids)
    {
        return keyMapper.deleteKeyByIds(ids);
    }

    /**
     * 删除钥匙信息
     *
     * @param id 钥匙主键
     * @return 结果
     */
    @Override
    public int deleteKeyById(Long id)
    {
        return keyMapper.deleteKeyById(id);
    }

    /**
     *  钥匙初始化
     *
     * @param keyId  钥匙ID
     * @return
     */
    @Override
    public int initKeyApi(Long keyId) {
        keyMapper.deleteKeyUser(keyId);
        return keyMapper.initKeyApi(keyId);
    }

    /**
     * 检查同一个柜子中钥匙名称和标号是否唯一
     *
     * @param key
     * @return
     */
    @Override
    public boolean checkKeyUnique(Key key) {
        Long id = StringUtils.isNull(key.getId()) ? -1L : key.getId();
        SystemSettings info = keyMapper.checkKeyUnique(key);
        if (StringUtils.isNotNull(info) && info.getId().longValue() != id.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 一键绑定所有钥匙
     *
     * @param srttings
     * @return
     */
    @Override
    public int batchBindKeys(String srttings) {
        keyMapper.batchUnBindKeys(srttings);
        List<Integer> ids = Arrays.stream(srttings.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        return keyMapper.batchBindKeys(ids);
    }



    /**
     * 一键解绑所有钥匙
     * @param srttings
     * @return
     */
    @Override
    public int batchUnBindKeys(String srttings) {
        return keyMapper.batchUnBindKeys(srttings);
    }

    @Override
    public List<KeyAppVo> listKeyByUser(Key key) {
        Map<String, Object> mapParam = new HashMap<>();
        mapParam.put("userId", key.getUserId());
        mapParam.put("status", key.getStatus());
        List<Key> listKeyByUser = keyMapper.listKeyByUser(mapParam);

        List<KeyAppVo> listKeyAppVo = new ArrayList<>();
        listKeyByUser.stream().forEach(keyEntry -> {
            KeyAppVo keyAppVo = new KeyAppVo();
            BeanUtils.copyProperties(keyEntry, keyAppVo);
            listKeyAppVo.add(keyAppVo);
        });

        return listKeyAppVo;
    }

    @Override
    public List<JSONObject> getKeyCabinetPermission(String userName) {

        List<JSONObject> listKeyCabinet = new ArrayList();
        User user = userMapper.selectUserByUsername(userName);

        String srttings = user.getSrttings();
        String[] split = srttings.split(",");
        for (String settingId : split) {
            SystemSettings settings = systemSettingsMapper.selectSystemSettingsById(Long.valueOf(settingId));

            Key key = new Key();
            key.setSrttings(settingId);
            List<Key> selectTotalKey = keyMapper.selectKeyList(key);

            Map<String, Object> mapParam = new HashMap<>();
            mapParam.put("userName", userName);
            mapParam.put("settingId", settingId);
            List<Key> keys = keyMapper.listKeyByUser(mapParam);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("cabinetName", settings.getCabinetName());
            jsonObject.put("totalKeyNum", selectTotalKey.size());
            jsonObject.put("useKeyNum", keys.size());
            listKeyCabinet.add(jsonObject);
        }
        return listKeyCabinet;
    }
}
