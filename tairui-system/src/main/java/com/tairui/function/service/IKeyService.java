package com.tairui.function.service;

import com.alibaba.fastjson2.JSONObject;
import com.tairui.function.domain.Key;
import com.tairui.function.domain.vo.KeyAppVo;

import java.util.List;

/**
 * 钥匙Service接口
 *
 * @author yhs
 * @date 2025-07-30
 */
public interface IKeyService
{
    /**
     * 查询钥匙
     *
     * @param id 钥匙主键
     * @return 钥匙
     */
    public Key selectKeyById(Long id);

    /**
     * 查询钥匙列表
     *
     * @param key 钥匙
     * @return 钥匙集合
     */
    public List<Key> selectKeyList(Key key);

    /**
     * 新增钥匙
     *
     * @param key 钥匙
     * @return 结果
     */
    public int insertKey(Key key);

    /**
     * 修改钥匙
     *
     * @param key 钥匙
     * @return 结果
     */
    public int updateKey(Key key);

    /**
     * 批量删除钥匙
     *
     * @param ids 需要删除的钥匙主键集合
     * @return 结果
     */
    public int deleteKeyByIds(Long[] ids);

    /**
     * 删除钥匙信息
     *
     * @param id 钥匙主键
     * @return 结果
     */
    public int deleteKeyById(Long id);

    /**
     *  钥匙初始化
     *
     * @param keyId  钥匙ID
     * @return
     */
    public int initKeyApi(Long keyId);

    /**
     * 检查同一个柜子中钥匙名称和标号是否唯一
     *
     * @param key
     * @return
     */
    public boolean checkKeyUnique(Key key);

    /**
     * 一键绑定所有钥匙
     *
     * @param srttings
     * @return
     */
    public int batchBindKeys(String srttings);

    /**
     * 一键解绑所有钥匙
     * @param srttings
     * @return
     */
    public int batchUnBindKeys(String srttings);

    List<KeyAppVo> listKeyByUser(Key key);

    List<JSONObject> getKeyCabinetPermission(String userName);
}
