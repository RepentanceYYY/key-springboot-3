package com.tairui.handler;

import com.tairui.common.core.redis.RedisCache;
import com.tairui.function.domain.KeyWorkflow;
import com.tairui.function.domain.KeyWorkflowDetail;
import com.tairui.function.mapper.KeyWorkflowDetailMapper;
import com.tairui.function.mapper.KeyWorkflowMapper;
import com.tairui.utils.BizConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 由于有很多操作都需要处理钥匙的申请流程
 */
@Service
public class keyWorkflowHandler {
    @Autowired
    private KeyWorkflowMapper keyWorkflowMapper;
    @Autowired
    private KeyWorkflowDetailMapper keyWorkflowDetailMapper;
    @Autowired
    private RedisCache redisCache;

    /**
     * 拒绝借用申请
     *
     * @param keyId   钥匙id
     * @param comment 理由
     */
    public void rejectKeyRelatedBorrowApply(Long keyId, String comment) {
        KeyWorkflowDetail keyWorkflowDetail = keyWorkflowDetailMapper.selectLastKeyWorkflowDetailByKeyIdAndApplyType(keyId.intValue(), BizConstants.APPLY_TYPE_BORROW);
        if (keyWorkflowDetail == null || Long.valueOf(BizConstants.BORROW_APPROVED_PICKED_OR_REJECTED).equals(keyWorkflowDetail.getStatus())) {
            return;
        }
        rejectBorrowApply(keyWorkflowDetail, comment);
    }

    /**
     * 拒绝借用申请
     *
     * @param keyCabinetIds 钥匙柜ids
     * @param comment       理由
     */
    public void rejectKeyCabinetRelateBorrowApply(List<Long> keyCabinetIds, String comment) {
        List<Integer> status = Arrays.asList(BizConstants.PENDING_APPROVAL, BizConstants.BORROW_APPROVED_NOT_PICKED);
        List<KeyWorkflowDetail> keyWorkflowDetails = keyWorkflowDetailMapper.selectKeyWorkflowDetailByKeyCabinetIdsAndStatus(keyCabinetIds, status, BizConstants.APPLY_TYPE_BORROW);
        if (keyWorkflowDetails.isEmpty()) {
            return;
        }
        //待审批的申请ids
        List<Long> awaitApprovalWorkflowIds = keyWorkflowDetails.stream().map(x -> x.getKeyWorkflowId()).collect(Collectors.toSet()).stream().toList();
        //待结束的申请想去ids
        List<Long> awaitEndWorkflowDetailIds = keyWorkflowDetails.stream().map(y -> y.getId()).collect(Collectors.toList());

        for (Long tmpId : awaitApprovalWorkflowIds) {
            redisCache.deleteObject(BizConstants.APPLY_CACHE_KEY_PREFIX + tmpId);
        }
        keyWorkflowDetailMapper.updateKeyWorkflowDetailStatusByIds(awaitEndWorkflowDetailIds, BizConstants.BIND_END_DETAIL_STATUS);
        KeyWorkflow keyWorkflow = new KeyWorkflow();
        keyWorkflow.setCurrentStatus(Long.valueOf(BizConstants.REJECTED));
        keyWorkflow.setApprovalTime(new Date());
        keyWorkflow.setApprovalComment(comment);
        keyWorkflowMapper.updateKeyWorkflowCurrentStatusByWorkflowIds(awaitApprovalWorkflowIds, keyWorkflow);
    }

    /**
     * 拒绝借用申请
     *
     * @param keyId   钥匙id
     * @param userIds 借用钥匙的候选申请人ids
     * @param comment 拒绝理由
     */
    public void rejectBorrowApplyByKeyIdAndUserId(Long keyId, Set<Long> userIds, String comment) {
        if (userIds.size() == 0) {
            return;
        }
        KeyWorkflowDetail keyWorkflowDetail = keyWorkflowDetailMapper.selectLastKeyWorkflowDetailByKeyIdAndApplyType(keyId.intValue(), BizConstants.APPLY_TYPE_BORROW);
        if (keyWorkflowDetail == null || Long.valueOf(BizConstants.BORROW_APPROVED_PICKED_OR_REJECTED).equals(keyWorkflowDetail.getStatus())) {
            return;
        }
        KeyWorkflow keyWorkflow = keyWorkflowMapper.selectKeyWorkflowById(keyWorkflowDetail.getKeyWorkflowId());
        if (!userIds.contains(keyWorkflow.getApplyUserId())) {
            return;
        }
        rejectBorrowApply(keyWorkflowDetail, comment);
    }

    /**
     * 拒绝借用申请
     *
     * @param keyWorkflowDetail
     * @param comment
     */
    private void rejectBorrowApply(KeyWorkflowDetail keyWorkflowDetail, String comment) {
        KeyWorkflow keyWorkflow = keyWorkflowMapper.selectKeyWorkflowById(keyWorkflowDetail.getKeyWorkflowId());
        keyWorkflow.setCurrentStatus(Long.valueOf(BizConstants.REJECTED));
        keyWorkflow.setApprovalComment(comment);
        keyWorkflow.setApprovalTime(new Date());
        keyWorkflowMapper.updateKeyWorkflow(keyWorkflow);

        KeyWorkflowDetail futureKeyWorkflowDetail = new KeyWorkflowDetail();
        futureKeyWorkflowDetail.setKeyWorkflowId(keyWorkflowDetail.getKeyWorkflowId());
        futureKeyWorkflowDetail.setUpdateTime(new Date());
        futureKeyWorkflowDetail.setStatus(Long.valueOf(BizConstants.BIND_END_DETAIL_STATUS));
        keyWorkflowDetailMapper.updateKeyWorkflowDetailByKeyWorkflowId(futureKeyWorkflowDetail);

        redisCache.deleteObject(BizConstants.APPLY_CACHE_KEY_PREFIX + keyWorkflow.getId());
    }
}
