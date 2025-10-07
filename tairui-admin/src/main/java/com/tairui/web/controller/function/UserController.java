package com.tairui.web.controller.function;

import com.tairui.common.annotation.Log;
import com.tairui.common.core.controller.BaseController;
import com.tairui.common.core.domain.AjaxResult;
import com.tairui.common.core.domain.entity.User;
import com.tairui.common.core.domain.model.LoginBody;
import com.tairui.common.core.page.TableDataInfo;
import com.tairui.common.enums.BusinessType;
import com.tairui.common.utils.ServletUtils;
import com.tairui.common.utils.StringUtils;
import com.tairui.common.utils.poi.ExcelUtil;
import com.tairui.framework.service.IUserService;
import com.tairui.function.domain.vo.UserLeftJoinKeyVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 用户Controller
 *
 * @author YHS
 * @date 2025-07-29
 */
@RestController
@RequestMapping("/function/user")
public class UserController extends BaseController {
    @Autowired
    private IUserService userService;

    /**
     * 查询用户列表
     */
    @PreAuthorize("@ss.hasPermi('function:user:list')")
    @GetMapping("/list")
    public TableDataInfo list(User user) {
        startPage();
        List<User> list = userService.selectUserList(user);
        return getDataTable(list);
    }

    /**
     * 登录接口
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginBody loginBody)
    {
        AjaxResult ajax = AjaxResult.success();
        // 生成令牌
        String username = userService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(),
                loginBody.getUuid(), loginBody.getNoCaptcha());
        User user = userService.selectUserByUsername(username);
//        ajax.put(Constants.TOKEN, username);
        ajax.put("userInfo",user);
        return ajax;
    }

    @GetMapping("getInfo")
    public AjaxResult getInfo()
    {
        String username = ServletUtils.getRequest().getHeader("Authorization");
        User user = userService.selectUserByUsername(username);
        // 角色集合
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", user);
        return ajax;
    }


    /**
     * 根据钥匙位查询用户
     */
    @GetMapping("/by-key-position")
    public TableDataInfo getUsersByKeyPosition(User user) {
        List<User> list = userService.getUsersByKeyPosition(user);
        return getDataTable(list);
    }


    /**
     * 导出用户列表
     */
    @PreAuthorize("@ss.hasPermi('function:user:export')")
    @Log(title = "用户", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, User user) {
//        List<User> list = userService.selectExportUserList(user);
        List<User> list = userService.selectExportUserKeyList(user);
        ExcelUtil<User> util = new ExcelUtil<User>(User.class);
        util.exportExcel(response, list, "用户数据");
    }

    @PostMapping("/export2")
    public void export2(HttpServletResponse response, @RequestParam(value = "keyword", required = false) String keyword) {
        List<UserLeftJoinKeyVo> list = userService.selectUserLeftJoinKeyVoList(keyword);
        ExcelUtil<UserLeftJoinKeyVo> util = new ExcelUtil<>(UserLeftJoinKeyVo.class);
        util.exportExcel(response, list, "用户信息数据", "用户信息表");
    }

    /**
     * 获取用户详细信息
     */
    @PreAuthorize("@ss.hasPermi('function:user:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        User user = userService.selectUserById(id);
        User user2 = new User();
        BeanUtils.copyProperties(user, user2);
        if (user.getSrttings() != null && !user.getSrttings().isEmpty()) {
            // 按逗号分割字符串，得到字符串数组
            String[] idStrs = user.getSrttings().split(",");
            // 创建对应的 Long 数组
            Long[] ids = new Long[idStrs.length];

            for (int i = 0; i < idStrs.length; i++) {
                try {
                    // 去除空格后转换为 Long
                    ids[i] = Long.parseLong(idStrs[i].trim());
                } catch (NumberFormatException e) {
                    // 单个元素转换失败时，可设为 null 或跳过
                    ids[i] = null;
                }
            }
            user2.setSrttingIds(ids);
        } else {
            // 空值时设为空数组（避免后续使用 NPE）
            user2.setSrttingIds(new Long[0]);
        }
        return success(user2);
    }

    /**
     * 新增用户
     */
    @PreAuthorize("@ss.hasPermi('function:user:add')")
    @Log(title = "用户", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody User user)
    {
        if (user.getSrttingIds() != null && user.getSrttingIds().length > 0) {
            StringBuilder sb = new StringBuilder();
            for (Long id : user.getSrttingIds()) {
                if (id != null) {
                    if (sb.length() > 0) {
                        sb.append(",");
                    }
                    sb.append(id);
                }
            }
            // 假设 User 类有 setSrttings 方法用于设置字符串
            user.setSrttings(sb.toString());
        } else {
            // 数组为空时设置空字符串
            user.setSrttings("");
        }
        if (StringUtils.isNotEmpty(user.getName()) && !userService.checkNameUnique(user)) {
            return error("新增用户名称'" + user.getName() + "'失败，用户名称已存在");
        }
        if (StringUtils.isNotEmpty(user.getUserName()) && !userService.checkUserNameUnique(user)) {
            return error("新增用户名'" + user.getUserName() + "'失败，用户名已存在");
        }
        return toAjax(userService.insertUser(user));
    }

    /**
     * 修改用户
     */
    @PreAuthorize("@ss.hasPermi('function:user:edit')")
    @Log(title = "用户", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody User user) {
        User user1 = userService.selectUserById(user.getId());
        if (user.getSrttings() != null && !user.getSrttings().isEmpty()) {
            // 按逗号分割字符串，得到字符串数组
            String[] idStrs = user.getSrttings().split(",");
            // 创建对应的 Long 数组
            Long[] ids = new Long[idStrs.length];

            for (int i = 0; i < idStrs.length; i++) {
                try {
                    // 去除空格后转换为 Long
                    ids[i] = Long.parseLong(idStrs[i].trim());
                } catch (NumberFormatException e) {
                    // 单个元素转换失败时，可设为 null 或跳过
                    ids[i] = null;
                }
            }
            user1.setSrttingIds(ids);
        } else {
            // 空值时设为空数组（避免后续使用 NPE）
            user1.setSrttingIds(new Long[0]);
        }

        if (user.getSrttingIds().length < user1.getSrttingIds().length) {
            // 将数据库中的ID集合转换为便于查找的Set
            Set<Long> dbIdSet = new HashSet<>();
            for (Long id : user1.getSrttingIds()) {
                if (id != null) {
                    dbIdSet.add(id);
                }
            }

            // 移除前端传入的ID，剩下的就是缺少的ID
            for (Long id : user.getSrttingIds()) {
                if (id != null) {
                    dbIdSet.remove(id);
                }
            }

            // 输出缺少的ID
            if (!dbIdSet.isEmpty()) {
                //移除与用户绑定柜子相关的钥匙
                // 直接传递Set集合，无需转换为字符串
                userService.removeBoundKeysByUserCabinet(dbIdSet, user.getId());
            }
        }


        if (user.getSrttingIds() != null && user.getSrttingIds().length > 0) {
            StringBuilder sb = new StringBuilder();
            for (Long id : user.getSrttingIds()) {
                if (id != null) {
                    if (sb.length() > 0) {
                        sb.append(",");
                    }
                    sb.append(id);
                }
            }
            // 假设 User 类有 setSrttings 方法用于设置字符串
            user.setSrttings(sb.toString());
        } else {
            // 数组为空时设置空字符串
            user.setSrttings("");
        }
        if (StringUtils.isNotEmpty(user.getName()) && !userService.checkNameUnique(user)) {
            return error("修改用户名称'" + user.getName() + "'失败，用户名称已存在");
        }
        if (StringUtils.isNotEmpty(user.getUserName()) && !userService.checkUserNameUnique(user)) {
            return error("修改用户名'" + user.getUserName() + "'失败，用户名已存在");
        }

        return toAjax(userService.updateUser(user));
    }

    /**
     * 删除用户
     */
    @PreAuthorize("@ss.hasPermi('function:user:remove')")
    @Log(title = "用户", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(userService.deleteUserByIds(ids));
    }
}
