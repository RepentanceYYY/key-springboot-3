package com.tairui.function.mapper;

import com.tairui.function.domain.Key;
import com.tairui.function.domain.KeyUser;
import com.tairui.function.domain.SystemSettings;
import com.tairui.function.domain.pojo.KeyAndKeyCabinetAndBindingStatusAndLastBorrowPOJO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 钥匙Mapper接口
 *
 * @author yhs
 * @date 2025-07-30
 */
public interface KeyMapper {
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
     * 获取用户已绑定钥匙
     */
    public List<Key> selectBoundKeysByUser(@Param("userId") Integer userId);

    /**
     * 通过用户id和钥匙状态获取钥匙列表
     *
     * @param key
     * @return 钥匙列表
     */
    public List<Key> selectKeyListByKeyIdAndUserId(Key key);

    /**
     * 查询钥匙信息&&和该用户的绑定情况&&最后借用情况
     *
     * @param keyAndKeyCabinetAndBindingStatusAndLastBorrowPOJO
     * @return
     */

    public List<KeyAndKeyCabinetAndBindingStatusAndLastBorrowPOJO> selectKeyAndKeyCabinetAndBindingStatusAndLastBorrowApply(KeyAndKeyCabinetAndBindingStatusAndLastBorrowPOJO keyAndKeyCabinetAndBindingStatusAndLastBorrowPOJO);

    /**
     * 查询所有数据
     *
     * @return
     */
    public List<Key> selectAllKey();

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
     * 删除钥匙
     *
     * @param id 钥匙主键
     * @return 结果
     */
    public int deleteKeyById(Long id);

    /**
     * 批量删除钥匙
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteKeyByIds(Long[] ids);

    /**
     * 批量删除 key_user
     *
     * @param keyId 钥匙ID
     */
    public void deleteKeyUser(Long keyId);

    /**
     * 新增key_user数据
     *
     * @param keyId  钥匙ID
     * @param userId 用户ID
     */
    public void insertKeyUser(@Param("keyId") long keyId, @Param("userId") long userId);

    /**
     * 通过获取钥匙柜下的所有钥匙id
     *
     * @param keyCabinetId
     * @return
     */

    public List<Integer> selectKeyIdsByKeyCabinetId(@Param("keyCabinetId") Integer keyCabinetId);

    /**
     * 获取用户绑定的所有钥匙id
     *
     * @param userId
     * @return
     */
    public List<Integer> selectKeyIdsByUserId(@Param("userId") Integer userId);

    /**
     * 钥匙初始化
     *
     * @param keyId 钥匙ID
     * @return
     */
    public int initKeyApi(Long keyId);

    /**
     * 检查同一个柜子中钥匙名称和标号是否唯一
     *
     * @param key
     * @return
     */
    public SystemSettings checkKeyUnique(Key key);

    /**
     * 一键绑定所有钥匙
     *
     * @param ids
     * @return
     */
    public int batchBindKeys(@Param("ids") List<Integer> ids);

    /**
     * 一键解绑所有钥匙
     *
     * @param srttings
     * @return
     */
    public int batchUnBindKeys(@Param("srttings") String srttings);

    /**
     * 批量用户钥匙绑定
     *
     * @param keyUserList
     * @return
     */

    public int insertBatchKeyUser(@Param("keyUserList") List<KeyUser> keyUserList);

    /**
     * 解绑用户绑定的钥匙
     * @param userId 需要解绑的用户id
     * @param keyIds 需要解绑的钥匙ids
     * @return
     */
    public int batchUnBindKeysByUserId(@Param("userId")Integer userId,@Param("keyIds")List<Integer> keyIds);


    List<Key> listKeyByUser(Map<String, Object> mapParam);

    /**
     * 由于通用性不强，我直接在sql里把它查出来了
     *
     * @param userId    用户id
     * @param applyType 钥匙在工作流中的申请类型
     * @param status    钥匙在工作流中的状态
     * @return
     */
    public int selectTodayUseKeyCount(@Param("userId") Integer userId, @Param("applyType") Integer applyType, @Param("status") Integer status);
}
