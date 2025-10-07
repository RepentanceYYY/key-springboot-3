package com.tairui.function.service.impl;

import com.tairui.function.domain.KeyWorkflowDetail;
import com.tairui.function.mapper.KeyWorkflowDetailMapper;
import com.tairui.function.service.IKeyWorkflowDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 钥匙申请审批流程详情
 * Service业务层处理
 *
 * @author tairui
 * @date 2025-09-18
 */
@Service
public class KeyWorkflowDetailServiceImpl implements IKeyWorkflowDetailService {
    @Autowired
    private KeyWorkflowDetailMapper keyWorkflowDetailMapper;

    /**
     * 查询钥匙申请审批流程详情
     *
     * @param id 钥匙申请审批流程详情主键
     * @return 钥匙申请审批流程详情
     */
    @Override
    public KeyWorkflowDetail selectKeyWorkflowDetailById(Long id) {
        return keyWorkflowDetailMapper.selectKeyWorkflowDetailById(id);
    }

    /**
     * 查询钥匙申请审批流程详情列表
     *
     * @param keyWorkflowDetail 钥匙申请审批流程详情
     * @return 钥匙申请审批流程详情
     */
    @Override
    public List<KeyWorkflowDetail> selectKeyWorkflowDetailList(KeyWorkflowDetail keyWorkflowDetail) {
        return keyWorkflowDetailMapper.selectKeyWorkflowDetailList(keyWorkflowDetail);
    }

    /**
     * 新增钥匙申请审批流程详情
     *
     * @param keyWorkflowDetail 钥匙申请审批流程详情
     * @return 结果
     */
    @Override
    public int insertKeyWorkflowDetail(KeyWorkflowDetail keyWorkflowDetail) {
        return keyWorkflowDetailMapper.insertKeyWorkflowDetail(keyWorkflowDetail);
    }

    /**
     * 修改钥匙申请审批流程详情
     *
     * @param keyWorkflowDetail 钥匙申请审批流程详情
     * @return 结果
     */
    @Override
    public int updateKeyWorkflowDetail(KeyWorkflowDetail keyWorkflowDetail) {
        return keyWorkflowDetailMapper.updateKeyWorkflowDetail(keyWorkflowDetail);
    }

    /**
     * 批量删除钥匙申请审批流程详情
     *
     * @param ids 需要删除的钥匙申请审批流程详情主键
     * @return 结果
     */
    @Override
    public int deleteKeyWorkflowDetailByIds(Long[] ids) {
        return keyWorkflowDetailMapper.deleteKeyWorkflowDetailByIds(ids);
    }

    /**
     * 删除钥匙申请审批流程详情信息
     *
     * @param id 钥匙申请审批流程详情主键
     * @return 结果
     */
    @Override
    public int deleteKeyWorkflowDetailById(Long id) {
        return keyWorkflowDetailMapper.deleteKeyWorkflowDetailById(id);
    }
}
