package com.tairui.listener;

import com.tairui.function.domain.KeyWorkflow;
import com.tairui.function.domain.dto.KeyApprovalDto;
import com.tairui.function.service.IKeyFromAppService;
import com.tairui.function.service.IKeyWorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class RedisKeyExpiredListener implements MessageListener {
    @Autowired
    private IKeyFromAppService keyFromAppService;
    @Autowired
    private IKeyWorkflowService keyWorkflowService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = new String(message.getBody(), StandardCharsets.UTF_8);
        //  申请超时未审批
        if (expiredKey.startsWith("await_approval:")) {
            Integer keyWorkflowId = Integer.valueOf(expiredKey.substring("await_approval:".length()));
            KeyWorkflow keyWorkflow = keyWorkflowService.selectKeyWorkflowById(keyWorkflowId.longValue());
            KeyApprovalDto keyApprovalDto = new KeyApprovalDto();
            keyApprovalDto.setKeyWorkflowId(keyWorkflowId);
            keyApprovalDto.setApprovalUserId(keyWorkflow.getApprovalUserId().intValue());
            keyApprovalDto.setApprovalAction(3);
            keyApprovalDto.setApprovalComment("超时未审批");
            keyFromAppService.keyApproval(keyApprovalDto,false);
        }
    }
}
