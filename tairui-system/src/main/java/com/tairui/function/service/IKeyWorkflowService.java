package com.tairui.function.service;

import com.tairui.function.domain.KeyWorkflow;

import java.util.List;

/**
 * 钥匙审批流Service接口
 * 
 * @author tairui
 * @date 2025-09-15
 */
public interface IKeyWorkflowService 
{
    /**
     * 查询钥匙审批流
     * 
     * @param id 钥匙审批流主键
     * @return 钥匙审批流
     */
    public KeyWorkflow selectKeyWorkflowById(Long id);

    /**
     * 查询钥匙审批流列表
     * 
     * @param keyWorkflow 钥匙审批流
     * @return 钥匙审批流集合
     */
    public List<KeyWorkflow> selectKeyWorkflowList(KeyWorkflow keyWorkflow);

    /**
     * 新增钥匙审批流
     * 
     * @param keyWorkflow 钥匙审批流
     * @return 结果
     */
    public int insertKeyWorkflow(KeyWorkflow keyWorkflow);

    /**
     * 修改钥匙审批流
     * 
     * @param keyWorkflow 钥匙审批流
     * @return 结果
     */
    public int updateKeyWorkflow(KeyWorkflow keyWorkflow);

    /**
     * 批量删除钥匙审批流
     * 
     * @param ids 需要删除的钥匙审批流主键集合
     * @return 结果
     */
    public int deleteKeyWorkflowByIds(Long[] ids);

    /**
     * 删除钥匙审批流信息
     * 
     * @param id 钥匙审批流主键
     * @return 结果
     */
    public int deleteKeyWorkflowById(Long id);
}
