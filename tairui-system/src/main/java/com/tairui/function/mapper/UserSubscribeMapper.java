package com.tairui.function.mapper;

import com.tairui.function.domain.UserSubscribe;

import java.util.List;

public interface UserSubscribeMapper {
    public List<UserSubscribe> selectUserSubscribeList(UserSubscribe userSubscribe);
    public int insertUserSubscribe(UserSubscribe userSubscribe);
}
