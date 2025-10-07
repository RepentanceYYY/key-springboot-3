package com.tairui.handler;

import com.tairui.function.domain.KeyUser;
import com.tairui.function.domain.KeyWorkflow;
import com.tairui.function.domain.KeyWorkflowDetail;
import com.tairui.function.domain.dto.KeyApprovalDto;

import java.util.List;

@FunctionalInterface
public interface ApprovalHandler {
    void handle(KeyWorkflowDetail detail, KeyApprovalDto dto, KeyWorkflow workflow, List<KeyUser> awaitKeyUsers);
}
