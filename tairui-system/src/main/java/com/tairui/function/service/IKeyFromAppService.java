package com.tairui.function.service;

import com.tairui.function.domain.dto.KeyApplyDto;
import com.tairui.function.domain.dto.KeyApprovalDto;
import com.tairui.function.domain.vo.KeyAndLastApplyVo;
import com.tairui.function.domain.vo.KeySummaryStatisticsVO;
import com.tairui.function.domain.vo.KeyWorkflowDetailVo;

import java.util.List;

public interface IKeyFromAppService {
    public KeySummaryStatisticsVO queryKeySummaryStatistics(Integer userId);

    public void keyApply(KeyApplyDto keyApplyDto);

    public void keyApproval(KeyApprovalDto keyApprovalDto, boolean verifyUser);

    public List<KeyWorkflowDetailVo> getApplyListByApplyUserId(Integer currentUserId);

    public List<KeyWorkflowDetailVo> getApprovalByApprovalUserId(Integer currentUserId);

    /**
     * 查询钥匙&&该钥匙最后一条借出申请&&该钥匙我的最后一条绑定申请
     *
     * @param userId 用户id
     * @return
     */
    List<KeyAndLastApplyVo> queryKeyAndLastBorrowApplyMyLastBindingApplyList(Integer userId);

    /**
     * 查询目标用户申请或审批的详情
     * @param targetUserId
     * @return
     */
    List<KeyWorkflowDetailVo> queryTargetUserApplyOrApproval(Integer targetUserId);
}
