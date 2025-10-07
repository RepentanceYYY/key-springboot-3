package com.tairui.web.controller.wechat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tairui.common.core.domain.AjaxResult;
import com.tairui.function.domain.BorrowRequestNotification;
import com.tairui.function.domain.UserSubscribe;
import com.tairui.function.service.IWeChatServer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wx")
@Tag(name = "微信开放平台对接")
public class WeChatController {
    @Autowired
    private IWeChatServer weChatServer;

    @GetMapping("/getOpenId")
    @Operation(summary = "获取微信用户openId")
    public AjaxResult getOpenId(@Parameter(description = "微信小程序登录时返回的 code") @RequestParam("code") String code) {
        try {
            String openId = weChatServer.Login(code);
            return AjaxResult.success("openId获取成功",openId);
        } catch (JsonProcessingException e) {
            return AjaxResult.error("获取失败：异常消息:" + e.getMessage());
        }
    }

    @PostMapping("/sendSubscribeMessage")
    @Operation(summary = "推送订阅消息", description = "目前只是为了能推送，信息随便填")
    public AjaxResult sendSubscribeMessage(@Parameter(description = "设备借用申请提醒") @RequestBody BorrowRequestNotification borrowRequestNotification) {
        try {
            String messageJson = borrowRequestNotification.buildMessageJson();
            weChatServer.sendSubscribeMessage(messageJson);
            return AjaxResult.success("订阅消息推送成功");
        } catch (Exception e) {
            return AjaxResult.error("推送订阅消息失败，异常消息：" + e.getMessage());
        }
    }

    @GetMapping("/getAccessToken")
    @Operation(summary = "获取临时凭证", description = "调用微信开放平台接口，需要先获取到临时凭证，携带临时凭证去调用接口")
    public AjaxResult getAccessToken() {
        String token = weChatServer.getAccessToken();
        return AjaxResult.success("临时凭证获取成功",token);
    }

    @GetMapping("/getUserSubscribe")
    @Operation(summary = "获取用户消息订阅关系列表")
    public AjaxResult getUserSubscribe(@RequestBody(required = false) UserSubscribe userSubscribe) {
        if(userSubscribe == null){
            userSubscribe = new UserSubscribe();
        }
        List<UserSubscribe> list = weChatServer.getUserSubscribeList(userSubscribe);
        return AjaxResult.success("用户消息订阅关系列表获取成功",list);
    }

    @PostMapping("/subscribe")
    @Operation(summary = "保存用户和消息的订阅关系")
    public AjaxResult Subscribe(UserSubscribe userSubscribe) {
        weChatServer.Subscribe(userSubscribe);
        return AjaxResult.success();
    }
}
