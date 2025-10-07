package com.tairui.framework.service;

import com.tairui.common.core.domain.entity.User;
import com.tairui.function.domain.vo.UserLeftJoinKeyVo;

import java.util.List;
import java.util.Set;

/**
 * 用户Service接口
 *
 * @author YHS
 * @date 2025-07-29
 */
public interface IUserService
{
    /**
     * 查询用户
     *
     * @param id 用户主键
     * @return 用户
     */
    public User selectUserById(Long id);
    public List<User> selectAllActiveUser();

    /**
     * 通过账号查询用户信息
     *
     * @param username 账号
     * @return 返回结果
     */
    public User selectUserByUsername(String username);

    /**
     * 查询用户列表
     *
     * @param user 用户
     * @return 用户集合
     */
    public List<User> selectUserList(User user);

    /**
     * 根据钥匙位查询用户
     *
     * @param user 用户
     * @return 用户集合
     */
    public List<User> getUsersByKeyPosition(User user);

    /**
     * 导出用户列表
     * @param user 用户
     * @return 结果
     */
    public List<User> selectExportUserList(User user);

    /**
     * 导出用户钥匙列表
     *
     * @param user 用户
     * @return 结果
     */
    public List<User> selectExportUserKeyList(User user);

    /**
     * 新增用户
     *
     * @param user 用户
     * @return 结果
     */
    public int insertUser(User user);

    /**
     * 修改用户
     *
     * @param user 用户
     * @return 结果
     */
    public int updateUser(User user);

    /**
     * 批量删除用户
     *
     * @param ids 需要删除的用户主键集合
     * @return 结果
     */
    public int deleteUserByIds(Long[] ids);

    /**
     * 删除用户信息
     *
     * @param id 用户主键
     * @return 结果
     */
    public int deleteUserById(Long id);

    /**
     * 检查用户名称是否唯一
     * @param user 用户
     * @return 结果
     */
    public boolean checkNameUnique(User user);

    /**
     * 检查用户名是否唯一
     * @param user 用户
     * @return 结果
     */
    public boolean checkUserNameUnique(User user);


    /**
     * 移除与用户绑定柜子相关的钥匙
     *
     * @param dbIdSet
     * @param id
     */
    public void removeBoundKeysByUserCabinet(Set<Long> dbIdSet, Long id);

    /**
     * 查询用户钥匙信息，以用户为主
     * @param keyword
     * @return
     */
    public List<UserLeftJoinKeyVo> selectUserLeftJoinKeyVoList(String keyword);

    /**
     * 登录
     *
     * @param username
     * @param password
     * @param code
     * @param uuid
     * @param noCaptcha
     * @return
     */
    String login(String username, String password, String code, String uuid, Boolean noCaptcha);


}
