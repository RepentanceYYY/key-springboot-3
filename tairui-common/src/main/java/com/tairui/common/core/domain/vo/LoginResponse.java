package com.tairui.common.core.domain.vo;

import com.tairui.common.core.domain.entity.User;
import lombok.Data;

/**
 * 登录响应 vo
 */
@Data
public class LoginResponse {
    /**
     * 用户信息
     */
    private User user;
    /**
     * 令牌
     */
    private String fakeToken;
}
