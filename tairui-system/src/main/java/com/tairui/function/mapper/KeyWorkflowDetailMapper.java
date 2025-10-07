package com.tairui.function.mapper;

import com.tairui.function.domain.KeyWorkflowDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 钥匙申请审批流程详情
 * Mapper接口
 *
 * @author tairui
 * @date 2025-09-18
 */
public interface KeyWorkflowDetailMapper {
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
     * 通过详情ids查询详情单列表
     *
     * @param ids
     * @return
     */
    public List<KeyWorkflowDetail> selectKeyWorkflowDetailListByIds(@Param("ids") Integer[] ids);

    /**
     * 查询钥匙在工作流中最后一次出现的详情单
     *
     * @param keyId     钥匙id
     * @param applyType 申请类型
     * @return
     */

    public KeyWorkflowDetail selectLastKeyWorkflowDetailByKeyIdAndApplyType(@Param("keyId") Integer keyId, @Param(("applyType")) Integer applyType);

    /**
     * 新增钥匙申请审批流程详情
     *
     * @param keyWorkflowDetail 钥匙申请审批流程详情
     * @return 结果
     */
    public int insertKeyWorkflowDetail(KeyWorkflowDetail keyWorkflowDetail);

    /**
     * 批量新增钥匙申请审批流程详情
     *
     * @param keyWorkflowDetailList
     * @return
     */

    public int insertBatchKeyWorkflowDetail(List<KeyWorkflowDetail> keyWorkflowDetailList);

    /**
     * 修改钥匙申请审批流程详情
     *
     * @param keyWorkflowDetail 钥匙申请审批流程详情
     * @return 结果
     */
    public int updateKeyWorkflowDetail(KeyWorkflowDetail keyWorkflowDetail);

    /**
     * 查询用户的指定申请类型的所有工作流详情
     *
     * @param applyUserId
     * @param applyType
     * @return
     */
    public List<KeyWorkflowDetail> selectKeyWorkflowDetailByUserIdAndApplyType(@Param("applyUserId") Integer applyUserId, @Param("applyType") Integer applyType);

    /**
     * 查询用户的指定申请类型的所有工作流详情,每个(钥匙柜+钥匙联合)的申请只要最新一行
     * @param applyUserId
     * @param applyType
     * @return
     */
    public List<KeyWorkflowDetail> selectLastKeyWorkflowDetailByUserIdAndApplyType(@Param("applyUserId") Integer applyUserId, @Param("applyType") Integer applyType);

    /**
     * 删除钥匙申请审批流程详情
     *
     * @param id 钥匙申请审批流程详情主键
     * @return 结果
     */
    public int deleteKeyWorkflowDetailById(Long id);

    /**
     * 批量删除钥匙申请审批流程详情
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteKeyWorkflowDetailByIds(Long[] ids);

}
