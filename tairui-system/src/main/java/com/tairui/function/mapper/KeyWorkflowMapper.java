package com.tairui.function.mapper;

import com.tairui.function.domain.KeyWorkflow;
import com.tairui.function.domain.pojo.KeyWorkflowDetailAndKeyPOJO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 钥匙审批流Mapper接口
 *
 * @author tairui
 * @date 2025-09-15
 */
public interface KeyWorkflowMapper {
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
     * 通过用户id和钥匙柜id获取申请审批流列表
     * 只查工资流id和审批人id
     *
     * @param applyUserId
     * @param keyCabinetId
     * @param currentStatus
     * @return
     */
    public List<KeyWorkflow> selectKeyWorkflowListByUserIdAndKeyCabinetId(@Param("applyUserId") Integer applyUserId, @Param("keyCabinetId") Integer keyCabinetId, @Param("currentStatus") Integer currentStatus);

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
     * 删除钥匙审批流
     *
     * @param id 钥匙审批流主键
     * @return 结果
     */
    public int deleteKeyWorkflowById(Long id);

    /**
     * 批量删除钥匙审批流
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteKeyWorkflowByIds(Long[] ids);

    /**
     * 查询指定申请人或审批人的流程单及钥匙信息
     *
     * @param detailAndKeyAllInfo 入参
     * @return 审批流+钥匙信息列表
     */
    List<KeyWorkflowDetailAndKeyPOJO> selectWorkflowDetailKeyWorkflowDetailAndKeyPOJO(KeyWorkflowDetailAndKeyPOJO detailAndKeyAllInfo);

    /**
     * 查询申请人或者审批人是目标用户，并且指定审批状态的流程单以及钥匙信息
     *
     * @param detailAndKeyAllInfo
     * @return
     */
    List<KeyWorkflowDetailAndKeyPOJO> selectWorkflowDetailKeyWorkflowDetailAndKeyPOJOByTargetUserId(KeyWorkflowDetailAndKeyPOJO detailAndKeyAllInfo);

}
