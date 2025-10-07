package com.tairui.function.mapper;

import com.tairui.common.core.domain.entity.User;
import com.tairui.function.domain.UserBase;
import com.tairui.function.domain.vo.UserLeftJoinKeyVo;
import com.tairui.function.domain.vo.UserSimpleVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 用户Mapper接口
 *
 * @author YHS
 * @date 2025-07-29
 */
public interface UserMapper {
    /**
     * 查询用户
     *
     * @param id 用户主键
     * @return 用户
     */
    public User selectUserById(Long id);

    /**
     * 查询用户列表
     *
     * @param ids 用户ids
     * @return 用户集合
     */
    public List<UserBase> selectUserBaseListByIds(@Param("ids") Integer[] ids);
    public UserBase selectUserBaseById(@Param("userId")Integer userId);

    /**
     * 查询用户列表
     *
     * @param user 用户
     * @return 用户集合
     */
    public List<User> selectUserList(User user);

    /**
     * 查询用户列表
     *
     * @param user 用户
     * @return 用户集合
     */
    public List<User> getUsersByKeyPosition(User user);

    /**
     * 导出用户列表
     *
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
     * 删除用户
     *
     * @param id 用户主键
     * @return 结果
     */
    public int deleteUserById(Long id);

    /**
     * 批量删除用户
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserByIds(Long[] ids);

    /**
     * 检查用户名称是否唯一
     *
     * @param name 用户名称
     * @return 结果
     */
    public User checkNameUnique(String name);


    /**
     * 检查用户名是否唯一
     *
     * @param userName 用户名
     * @return 结果
     */
    public User checkUserNameUnique(String userName);

    /**
     * 移除与用户绑定柜子相关的钥匙
     *
     * @param dbIdSet
     * @param id
     */
    public void removeBoundKeysByUserCabinet(@Param("dbIdSet") Set<Long> dbIdSet, @Param("id") Long id);

    /**
     * 好像原来做导出用的
     *
     * @param vo
     * @return
     */

    public List<UserLeftJoinKeyVo> selectUserLeftJoinKeyVoList(UserLeftJoinKeyVo vo);

    /**
     * 根据账号查询用户信息
     *
     * @param username 账号
     * @return 返回用户信息
     */
    User selectUserByUsername(String username);

    /**
     * 查询所有用户的id和是否激活和绑定的钥匙柜ids
     *
     * @return
     */
    public List<UserSimpleVo> selectUserSimpleVo();

    /**
     * 修改用户绑定的钥匙柜idStr
     * @param userId
     * @param keyCabinetsStr
     */
    public void updateUserOwnCabinetPermissions(@Param("userId")Integer userId,@Param("keyCabinetsStr")String keyCabinetsStr);

}
