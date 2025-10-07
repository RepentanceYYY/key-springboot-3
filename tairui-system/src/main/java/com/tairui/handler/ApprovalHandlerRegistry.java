package com.tairui.handler;

import com.tairui.enums.ApplyType;
import com.tairui.enums.ApprovalAction;
import com.tairui.function.domain.KeyUser;

import java.util.HashMap;
import java.util.Map;

public class ApprovalHandlerRegistry {
    private static final Map<String, ApprovalHandler> HANDLERS = new HashMap<>();

    static {
        // 绑定 + 同意
        register(ApplyType.BIND, ApprovalAction.APPROVE, (detail, dto, workflow, awaitUsers) -> {
            detail.setStatus(2L); // 已绑定
            KeyUser keyUser = new KeyUser();
            keyUser.setUserId(workflow.getApplyUserId().intValue());
            keyUser.setKeyId(detail.getKeyId().intValue());
            awaitUsers.add(keyUser);
        });

        // 绑定 + 拒绝
        register(ApplyType.BIND, ApprovalAction.REJECT, (detail, dto, workflow, awaitUsers) -> {
            detail.setStatus(2L); // 拒绝也设为 2L
        });

        // 绑定 + 超时
        register(ApplyType.BIND, ApprovalAction.TIMEOUT, (detail, dto, workflow, awaitUsers) -> {
            detail.setStatus(2L); // 超时处理为拒绝或锁定
        });

        // 借用 + 同意
        register(ApplyType.BORROW, ApprovalAction.APPROVE, (detail, dto, workflow, awaitUsers) -> {
            detail.setStatus(1L); // 借用中
        });

        // 借用 + 拒绝
        register(ApplyType.BORROW, ApprovalAction.REJECT, (detail, dto, workflow, awaitUsers) -> {
            detail.setStatus(2L); // 拒绝
        });

        // 借用 + 超时
        register(ApplyType.BORROW, ApprovalAction.TIMEOUT, (detail, dto, workflow, awaitUsers) -> {
            detail.setStatus(2L); // 超时处理为拒绝
        });
    }

    private static void register(ApplyType type, ApprovalAction action, ApprovalHandler handler) {
        HANDLERS.put(type.name() + "_" + action.name(), handler);
    }

    public static ApprovalHandler get(ApplyType type, ApprovalAction action) {
        return HANDLERS.get(type.name() + "_" + action.name());
    }
}