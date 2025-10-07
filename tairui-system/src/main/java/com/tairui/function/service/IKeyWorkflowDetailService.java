package com.tairui.function.service;

import com.tairui.function.domain.KeyWorkflowDetail;

import java.util.List;

/**
 * 钥匙申请审批流程详情
 * Service接口
 *
 * @author tairui
 * @date 2025-09-18
 */
public interface IKeyWorkflowDetailService {
    /**
     * 查询钥匙申请审批流程详情
     *
     * @param id 钥匙申请审批流程详情主键
     * @return 钥匙申请审批流程详情
     */
    public KeyWorkflowDetail selectKeyWorkflowDetailById(Long id);

    /**
     * 查询钥匙申请审批流程详情列表
     *
     * @param keyWorkflowDetail 钥匙申请审批流程详情
     * @return 钥匙申请审批流程详情集合
     */
    public List<KeyWorkflowDetail> selectKeyWorkflowDetailList(KeyWorkflowDetail keyWorkflowDetail);

    /**
     * 新增钥匙申请审批流程详情
     *
     * @param keyWorkflowDetail 钥匙申请审批流程详情
     * @return 结果
     */
    public int insertKeyWorkflowDetail(KeyWorkflowDetail keyWorkflowDetail);

    /**
     * 修改钥匙申请审批流程详情
     *
     * @param keyWorkflowDetail 钥匙申请审批流程详情
     * @return 结果
     */
    public int updateKeyWorkflowDetail(KeyWorkflowDetail keyWorkflowDetail);

    /**
     * 批量删除钥匙申请审批流程详情
     *
     * @param ids 需要删除的钥匙申请审批流程详情主键集合
     * @return 结果
     */
    public int deleteKeyWorkflowDetailByIds(Long[] ids);

    /**
     * 删除钥匙申请审批流程详情信息
     *
     * @param id 钥匙申请审批流程详情主键
     * @return 结果
     */
    public int deleteKeyWorkflowDetailById(Long id);
}
