package com.tairui.function.service.impl;

import com.tairui.function.domain.KeyWorkflow;
import com.tairui.function.mapper.KeyWorkflowMapper;
import com.tairui.function.service.IKeyWorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 钥匙审批流Service业务层处理
 * 
 * @author tairui
 * @date 2025-09-15
 */
@Service
public class KeyWorkflowServiceImpl implements IKeyWorkflowService 
{
    @Autowired
    private KeyWorkflowMapper keyWorkflowMapper;

    /**
     * 查询钥匙审批流
     * 
     * @param id 钥匙审批流主键
     * @return 钥匙审批流
     */
    @Override
    public KeyWorkflow selectKeyWorkflowById(Long id)
    {
        return keyWorkflowMapper.selectKeyWorkflowById(id);
    }

    /**
     * 查询钥匙审批流列表
     * 
     * @param keyWorkflow 钥匙审批流
     * @return 钥匙审批流
     */
    @Override
    public List<KeyWorkflow> selectKeyWorkflowList(KeyWorkflow keyWorkflow)
    {
        return keyWorkflowMapper.selectKeyWorkflowList(keyWorkflow);
    }

    /**
     * 新增钥匙审批流
     * 
     * @param keyWorkflow 钥匙审批流
     * @return 结果
     */
    @Override
    public int insertKeyWorkflow(KeyWorkflow keyWorkflow)
    {
        return keyWorkflowMapper.insertKeyWorkflow(keyWorkflow);
    }

    /**
     * 修改钥匙审批流
     * 
     * @param keyWorkflow 钥匙审批流
     * @return 结果
     */
    @Override
    public int updateKeyWorkflow(KeyWorkflow keyWorkflow)
    {
        return keyWorkflowMapper.updateKeyWorkflow(keyWorkflow);
    }

    /**
     * 批量删除钥匙审批流
     * 
     * @param ids 需要删除的钥匙审批流主键
     * @return 结果
     */
    @Override
    public int deleteKeyWorkflowByIds(Long[] ids)
    {
        return keyWorkflowMapper.deleteKeyWorkflowByIds(ids);
    }

    /**
     * 删除钥匙审批流信息
     * 
     * @param id 钥匙审批流主键
     * @return 结果
     */
    @Override
    public int deleteKeyWorkflowById(Long id)
    {
        return keyWorkflowMapper.deleteKeyWorkflowById(id);
    }
}
