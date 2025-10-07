package com.tairui.framework.service.impl;

import com.tairui.common.constant.Constants;
import com.tairui.common.constant.UserConstants;
import com.tairui.common.core.domain.entity.SysUser;
import com.tairui.common.core.domain.entity.User;
import com.tairui.common.core.domain.model.LoginUser;
import com.tairui.common.core.redis.RedisCache;
import com.tairui.common.exception.ServiceException;
import com.tairui.common.exception.user.UserNotExistsException;
import com.tairui.common.exception.user.UserPasswordNotMatchException;
import com.tairui.common.utils.DateUtils;
import com.tairui.common.utils.MessageUtils;
import com.tairui.common.utils.StringUtils;
import com.tairui.common.utils.ip.IpUtils;
import com.tairui.framework.manager.AsyncManager;
import com.tairui.framework.manager.factory.AsyncFactory;
import com.tairui.framework.security.context.AuthenticationContextHolder;
import com.tairui.framework.service.IUserService;
import com.tairui.framework.web.service.SysLoginService;
import com.tairui.framework.web.service.TokenService;
import com.tairui.function.domain.vo.UserLeftJoinKeyVo;
import com.tairui.function.mapper.UserMapper;
import com.tairui.system.service.ISysConfigService;
import com.tairui.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import jakarta.annotation.*;
import java.util.List;
import java.util.Set;

/**
 * 用户Service业务层处理
 *
 * @author YHS
 * @date 2025-07-29
 */
@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SysLoginService sysLoginService;

    @Autowired
    private TokenService tokenService;

    @Resource
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysConfigService configService;

    /**
     * 查询用户
     *
     * @param id 用户主键
     * @return 用户
     */
    @Override
    public User selectUserById(Long id) {
        return userMapper.selectUserById(id);
    }

    @Override
    public List<User> selectAllActiveUser() {
        User user = new User();
        user.setActive("true");
        user.setRole("管理员");
        return userMapper.selectUserList(user);
    }

    @Override
    public User selectUserByUsername(String username) {
        return userMapper.selectUserByUsername(username);
    }

    /**
     * 查询用户列表
     *
     * @param user 用户
     * @return 用户
     */
    @Override
    public List<User> selectUserList(User user) {
        return userMapper.selectUserList(user);
    }

    /**
     * 查询用户列表
     *
     * @param user 用户
     * @return 用户集合
     */
    @Override
    public List<User> getUsersByKeyPosition(User user) {
        return userMapper.getUsersByKeyPosition(user);
    }

    /**
     * 导出用户列表
     *
     * @param user 用户
     * @return 结果
     */
    @Override
    public List<User> selectExportUserList(User user) {
        return userMapper.selectExportUserList(user);
    }

    /**
     * 导出用户钥匙列表
     *
     * @param user 用户
     * @return 结果
     */
    @Override
    public List<User> selectExportUserKeyList(User user) {
        return userMapper.selectExportUserKeyList(user);
    }

    /**
     * 新增用户
     *
     * @param user 用户
     * @return 结果
     */
    @Override
    public int insertUser(User user) {
        return userMapper.insertUser(user);
    }

    /**
     * 修改用户
     *
     * @param user 用户
     * @return 结果
     */
    @Override
    public int updateUser(User user) {
        return userMapper.updateUser(user);
    }

    /**
     * 批量删除用户
     *
     * @param ids 需要删除的用户主键
     * @return 结果
     */
    @Override
    public int deleteUserByIds(Long[] ids) {
        return userMapper.deleteUserByIds(ids);
    }

    /**
     * 删除用户信息
     *
     * @param id 用户主键
     * @return 结果
     */
    @Override
    public int deleteUserById(Long id) {
        return userMapper.deleteUserById(id);
    }

    /**
     * 检查用户名称是否唯一
     *
     * @param user 用户
     * @return 结果
     */
    @Override
    public boolean checkNameUnique(User user) {
        Long id = StringUtils.isNull(user.getId()) ? -1L : user.getId();
        User info = userMapper.checkNameUnique(user.getName());
        if (StringUtils.isNotNull(info) && info.getId().longValue() != id.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 检查用户名是否唯一
     *
     * @param user 用户
     * @return 结果
     */
    @Override
    public boolean checkUserNameUnique(User user) {
        Long id = StringUtils.isNull(user.getId()) ? -1L : user.getId();
        User info = userMapper.checkUserNameUnique(user.getUserName());
        if (StringUtils.isNotNull(info) && info.getId().longValue() != id.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 移除与用户绑定柜子相关的钥匙
     *
     * @param dbIdSet
     * @param id
     */
    @Override
    public void removeBoundKeysByUserCabinet(Set<Long> dbIdSet, Long id) {
        userMapper.removeBoundKeysByUserCabinet(dbIdSet, id);
    }

    @Override
    public List<UserLeftJoinKeyVo> selectUserLeftJoinKeyVoList(String keyword) {
        UserLeftJoinKeyVo vo = new UserLeftJoinKeyVo();
        if (keyword != null && !keyword.trim().isEmpty()) {
            vo.setUserName(keyword);
            vo.setUserNikeName(keyword);
            vo.setRole(keyword);
        }
        return userMapper.selectUserLeftJoinKeyVoList(vo);
    }

    @Override
    public String login(String username, String password, String code, String uuid, Boolean noCaptcha) {
        // 验证码校验
//        sysLoginService.validateCaptcha(username,code,uuid,noCaptcha);
        // 登录前置校验
        // 用户名或密码为空 错误
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("not.null")));
            throw new UserNotExistsException();
        }

        // 比对用户密码
        User user = userMapper.selectUserByUsername(username);
        if (!password.equals(user.getPassWord())) {
            throw new UserPasswordNotMatchException();
        }

        // 生成token
        return fackeLogin();
    }

    public String fackeLogin() {
        String username = "admin";
        String password = "admin123";
        // 用户验证
        Authentication authentication = null;
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        AuthenticationContextHolder.setContext(authenticationToken);
        try {
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager.authenticate(authenticationToken);
        } catch (Exception e) {
            if (e instanceof BadCredentialsException) {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
                throw new UserPasswordNotMatchException();
            } else {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, e.getMessage()));
                throw new ServiceException(e.getMessage());
            }
        } finally {
            AuthenticationContextHolder.clearContext();
        }
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        recordLoginInfo(loginUser.getUserId());
        // 生成token
        return tokenService.createToken(loginUser);
    }

    public void recordLoginInfo(Long userId) {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        sysUser.setLoginIp(IpUtils.getIpAddr());
        sysUser.setLoginDate(DateUtils.getNowDate());
        userService.updateUserProfile(sysUser);
    }

}
