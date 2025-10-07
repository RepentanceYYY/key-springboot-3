package com.tairui.function.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tairui.function.domain.UserSubscribe;

import java.util.List;

public interface IWeChatServer {
    /**
     * 发布订阅消息
     *
     * @param message
     */
    public void sendSubscribeMessage(String message);

    /**
     * 获取临时凭证保存到redis
     *
     * @return access_token
     */
    public String getAccessToken();

    /**
     * 微信登录：获取微信用户openid
     *
     * @param code 微信用户code
     * @return openid
     */
    public String Login(String code) throws JsonProcessingException;

    /**
     * 获取用户订阅列表
     *
     * @param userSubscribe
     * @return
     */

    public List<UserSubscribe> getUserSubscribeList(UserSubscribe userSubscribe);

    /**
     * 用户订阅推送消息
     *
     * @param userSubscribe
     * @return
     */

    public int Subscribe(UserSubscribe userSubscribe);

}
