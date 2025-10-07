package com.tairui.web.controller.uniapp;

import com.tairui.common.core.controller.BaseController;
import com.tairui.common.core.domain.AjaxResult;
import com.tairui.common.core.domain.entity.User;
import com.tairui.common.core.domain.model.LoginBody;
import com.tairui.common.core.domain.vo.LoginResponse;
import com.tairui.framework.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/app/user")
@Tag(name = "用户服务")
public class AppUserController extends BaseController {

    @Autowired
    private IUserService userService;

    /**
     * 登录接口
     */
    @PostMapping("/login")
    @Operation(summary = "登录接口")
    public AjaxResult login(@RequestBody LoginBody loginBody) {
        // 生成令牌
        String fakeToken = userService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(),
                loginBody.getUuid(), loginBody.getNoCaptcha());
        //校验，获取用户信息
        User user = userService.selectUserByUsername(loginBody.getUsername());
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setUser(user);
        loginResponse.setFakeToken(fakeToken);
        return AjaxResult.success("登录成功", loginResponse);
    }
    @GetMapping("/getAllActiveAdmin")
    @Operation(summary = "获取所有正常使用的管理员")
    public AjaxResult getAllActiveAdmin(){
        List<User> users = userService.selectAllActiveUser();
        return AjaxResult.success("获取成功",users);

    }
}
