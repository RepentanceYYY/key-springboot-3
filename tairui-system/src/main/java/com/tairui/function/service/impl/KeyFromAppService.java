package com.tairui.function.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tairui.common.core.domain.entity.User;
import com.tairui.common.core.redis.RedisCache;
import com.tairui.common.utils.StringUtils;
import com.tairui.enums.ApplyType;
import com.tairui.enums.ApprovalAction;
import com.tairui.function.domain.*;
import com.tairui.function.domain.dto.KeyApplyDto;
import com.tairui.function.domain.dto.KeyApprovalDto;
import com.tairui.function.domain.pojo.KeyAndKeyCabinetAndBindingStatusAndLastBorrowPOJO;
import com.tairui.function.domain.pojo.KeyWorkflowDetailAndKeyPOJO;
import com.tairui.function.domain.vo.KeyAndLastApplyVo;
import com.tairui.function.domain.vo.KeySummaryStatisticsVO;
import com.tairui.function.domain.vo.KeyWorkflowDetailVo;
import com.tairui.function.domain.vo.WorkflowDetailsAndKeyAndKeyCabinet;
import com.tairui.function.mapper.KeyMapper;
import com.tairui.function.mapper.KeyWorkflowDetailMapper;
import com.tairui.function.mapper.KeyWorkflowMapper;
import com.tairui.function.mapper.UserMapper;
import com.tairui.function.service.IKeyFromAppService;
import com.tairui.handler.ApprovalHandler;
import com.tairui.handler.ApprovalHandlerRegistry;
import com.tairui.utils.BizConstants;
import com.tairui.utils.KeyFlowNoGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class KeyFromAppService implements IKeyFromAppService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private KeyWorkflowDetailMapper keyWorkflowDetailMapper;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private KeyWorkflowMapper keyWorkflowMapper;
    @Autowired
    private KeyMapper keyMapper;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private KeyFlowNoGenerator keyFlowNoGenerator;


    /**
     * 获取钥匙状态概要
     *
     * @param userId
     * @return
     */
    @Override
    public KeySummaryStatisticsVO queryKeySummaryStatistics(Integer userId) {
        // 过滤不可用用户
        User user = userMapper.selectUserById(userId.longValue());
        if (user == null || user.getActive() == "false") {
            throw new RuntimeException("用户不存在或未激活");
        }
        KeySummaryStatisticsVO result = new KeySummaryStatisticsVO();
        // 1.获取可借出钥匙
        List<KeyAndLastApplyVo> keyList = this.queryKeyAndLastBorrowApplyMyLastBindingApplyList(userId);
        //钥匙在位，并且绑定，如果有借用申请过，则加上并且审批流中最后一条记录已结束
        int canBorrowCount = Math.toIntExact(
                keyList.stream()
                        .filter(x -> BizConstants.KEY_AVAILABLE.equals(x.getKeyStatus()))
                        .filter(x -> x.getLastBorrowingWorkflowDetailsStatus() == BizConstants.NOT_APPLIED_DETAIL_STATUS || x.getLastBorrowingWorkflowDetailsStatus() == BizConstants.BIND_END_DETAIL_STATUS)
                        .count()
        );
        result.setCanBorrowCount(canBorrowCount);
        //  2.获取待审批钥匙 普通用户拿到的是自己申请的，管理员拿到的是待自己审批的
        KeyWorkflow keyWorkflow = new KeyWorkflow();
        keyWorkflow.setCurrentStatus(BizConstants.PENDING_APPROVAL.longValue());
        if (BizConstants.ADMIN.equals(user.getRole())) {
            keyWorkflow.setApprovalUserId(userId.longValue());
        } else {
            keyWorkflow.setApplyUserId(userId.longValue());
        }
        List<KeyWorkflow> keyWorkflows = keyWorkflowMapper.selectKeyWorkflowList(keyWorkflow);
        result.setPendingApprovalCount(keyWorkflows.size());
        //  3.今日使用
        int toDayUserKeyCount = keyMapper.selectTodayUseKeyCount(userId, BizConstants.APPLY_TYPE_BORROW, BizConstants.BORROW_APPROVED_PICKED_OR_REJECTED);
        result.setTodayUsageCount(toDayUserKeyCount);

        //  4.需归还
        Key key = new Key();
        key.setLastControlUserId(userId.longValue());
        key.setStatus(BizConstants.KEY_BORROWED);
        List<Key> keys = keyMapper.selectKeyList(key);
        result.setToReturnCount(keys.size());

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void keyApply(KeyApplyDto keyApplyDto) {
        validateApplyUserAndApprovalUser(keyApplyDto);
        if (keyApplyDto.getApplyType() == BizConstants.APPLY_TYPE_BIND) {
            this.validateKeyCanBeBound(keyApplyDto.getKeyIds(), keyApplyDto.getApplyUserId());
        } else if (keyApplyDto.getApplyType() == BizConstants.APPLY_TYPE_BORROW) {
            this.validateKeyCanBeApplied(keyApplyDto.getKeyIds(), keyApplyDto.getApplyUserId());
        }
        //生成申请单
        KeyWorkflow keyWorkflow = new KeyWorkflow();
        keyWorkflow.setWorkflowNo(keyFlowNoGenerator.generateFlowNo());
        keyWorkflow.setApplyUserId(keyApplyDto.getApplyUserId().longValue());
        keyWorkflow.setApplyTime(new Date());
        keyWorkflow.setApplyReason(keyApplyDto.getApplyReason());
        keyWorkflow.setExpectedTime(keyApplyDto.getExpectedTime());
        keyWorkflow.setApprovalUserId(keyApplyDto.getApprovalUserId().longValue());
        keyWorkflow.setCurrentStatus(BizConstants.PENDING_APPROVAL.longValue());
        keyWorkflowMapper.insertKeyWorkflow(keyWorkflow);
        //拿到插入后的流程id
        Long keyWorkflowId = keyWorkflow.getId();
        //批量插入
        List<KeyWorkflowDetail> keyWorkflowDetailList = new ArrayList<>();
        KeyWorkflowDetail details;
        for (Integer keyId : keyApplyDto.getKeyIds()) {
            details = new KeyWorkflowDetail();
            details.setKeyWorkflowId(keyWorkflowId);
            details.setKeyId(keyId.longValue());
            details.setKeyCabinetId(keyApplyDto.getKeyCabinetId().longValue());
            details.setApplyType(keyApplyDto.getApplyType().longValue());
            details.setStatus(BizConstants.PENDING_APPROVAL.longValue());
            details.setUpdateTime(new Date());
            //添加到待插入集合中
            keyWorkflowDetailList.add(details);
        }
        //5分钟后该申请自动过期
        keyWorkflowDetailMapper.insertBatchKeyWorkflowDetail(keyWorkflowDetailList);
        try {
            String value = objectMapper.writeValueAsString(keyWorkflow);
            redisCache.redisTemplate.opsForValue().set(BizConstants.APPLY_CACHE_KEY_PREFIX + keyWorkflowId, value, BizConstants.APPLY_TIMEOUT_MINUTES, TimeUnit.MINUTES);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void keyApproval(KeyApprovalDto keyApprovalDto, boolean verifyUser) {
        if (verifyUser) {
            User user = userMapper.selectUserById(keyApprovalDto.getApprovalUserId().longValue());
            if (user == null || !BizConstants.ADMIN.equals(user.getRole()) || !"true".equals(user.getActive())) {
                throw new RuntimeException("审批该申请的用户无效");
            }
        }
        KeyWorkflow keyWorkflow = keyWorkflowMapper.selectKeyWorkflowById(keyApprovalDto.getKeyWorkflowId().longValue());
        if (keyWorkflow == null) {
            throw new RuntimeException("该钥匙申请流程单不存在");
        }
        if (Long.valueOf(BizConstants.TIMEOUT).equals(keyWorkflow.getCurrentStatus())) {
            throw new RuntimeException("超时未审批，已自动拒绝");
        } else if (keyWorkflow.getCurrentStatus() != BizConstants.PENDING_APPROVAL.longValue()) {
            throw new RuntimeException("该钥匙申请已审批");
        }
        //不管在不在直接删除redis中对应的key
        String redisKey = BizConstants.APPLY_CACHE_KEY_PREFIX + keyApprovalDto.getKeyWorkflowId();
        redisCache.deleteObject(redisKey);

        keyWorkflow.setCurrentStatus(keyApprovalDto.getApprovalAction().longValue());
        keyWorkflow.setApprovalUserId(keyApprovalDto.getApprovalUserId().longValue());
        keyWorkflow.setApprovalComment(keyApprovalDto.getApprovalComment());
        keyWorkflow.setApprovalTime(new Date());
        //  更新流程单
        keyWorkflowMapper.updateKeyWorkflow(keyWorkflow);
        // 找出该流程单下的所有详情
        KeyWorkflowDetail queryParam = new KeyWorkflowDetail();
        queryParam.setKeyWorkflowId(keyApprovalDto.getKeyWorkflowId().longValue());
        List<KeyWorkflowDetail> detailList = keyWorkflowDetailMapper.selectKeyWorkflowDetailList(queryParam);
        if (detailList.isEmpty()) {
            throw new RuntimeException("该钥匙申请流程单下没有申请的钥匙");
        }
        //  如果是绑定申请，则用这个直接插入
        List<KeyUser> awaitKeyUsers = new ArrayList<>();
        //  使用双枚举 + 策略映射表
        for (KeyWorkflowDetail detail : detailList) {
            //  获取申请类型
            ApplyType type = ApplyType.of(detail.getApplyType().intValue());
            //  获取审批动作
            ApprovalAction action = ApprovalAction.of(keyApprovalDto.getApprovalAction());
            //  根据申请类型和审批动作获取到对应处理器
            ApprovalHandler handler = ApprovalHandlerRegistry.get(type, action);
            if (handler == null) {
                throw new RuntimeException("未找到处理器: " + type + " + " + action);
            }
            //  进行处理
            handler.handle(detail, keyApprovalDto, keyWorkflow, awaitKeyUsers);
            detail.setUpdateTime(new Date());
            keyWorkflowDetailMapper.updateKeyWorkflowDetail(detail);
        }
        // 额外处理绑定申请+通过情况
        if (!awaitKeyUsers.isEmpty()) {
            keyMapper.insertBatchKeyUser(awaitKeyUsers);
        }
    }

    @Override
    public List<KeyWorkflowDetailVo> getApplyListByApplyUserId(Integer currentUserId) {
        User currentUser = userMapper.selectUserById(currentUserId.longValue());
        if (currentUser == null) {
            throw new RuntimeException("用户不存在");
        } else if (currentUser.getActive() == "false") {
            throw new RuntimeException("用户已停用");
        }

        KeyWorkflowDetailAndKeyPOJO detailAndKeyAllInfo = new KeyWorkflowDetailAndKeyPOJO();
        detailAndKeyAllInfo.setApplyUserId(currentUserId.longValue());
        List<KeyWorkflowDetailAndKeyPOJO> dbList = keyWorkflowMapper.selectWorkflowDetailKeyWorkflowDetailAndKeyPOJO(detailAndKeyAllInfo);
        return getKeyWorkflowDetailVoList(dbList);
    }

    @Override
    public List<KeyWorkflowDetailVo> getApprovalByApprovalUserId(Integer currentUserId) {
        User currentUser = userMapper.selectUserById(currentUserId.longValue());
        if (currentUser == null) {
            throw new RuntimeException("用户不存在");
        } else if (currentUser.getActive() == "false") {
            throw new RuntimeException("用户已停用");
        }

        KeyWorkflowDetailAndKeyPOJO pojo = new KeyWorkflowDetailAndKeyPOJO();
        pojo.setApprovalUserId(currentUserId.longValue());
        List<KeyWorkflowDetailAndKeyPOJO> dbList = keyWorkflowMapper.selectWorkflowDetailKeyWorkflowDetailAndKeyPOJO(pojo);
        return getKeyWorkflowDetailVoList(dbList);
    }

    @Override
    public List<KeyAndLastApplyVo> queryKeyAndLastBorrowApplyMyLastBindingApplyList(Integer userId) {
        UserBase userBase = userMapper.selectUserBaseById(userId);
        if (userBase == null) {
            throw new RuntimeException("查询钥匙列表的用户不存在");
        }
        List<Integer> ownPermissionKeyCabinetIds = new ArrayList<>();
        if (!BizConstants.ADMIN.equals(userBase.getRole())) {
            // 用户拥有权限的钥匙柜
            ownPermissionKeyCabinetIds = StringUtils.strToIntegerList(userBase.getSrttings(), ",");
            // 如果用户没绑定任何钥匙柜，没必要再查下去了
            if (ownPermissionKeyCabinetIds.size() < 1) {
                return new ArrayList<>();
            }
        }

        // 定义结果
        List<KeyAndLastApplyVo> result = new ArrayList<>();
        // 查询所有信息出来
        KeyAndKeyCabinetAndBindingStatusAndLastBorrowPOJO param = new KeyAndKeyCabinetAndBindingStatusAndLastBorrowPOJO();
        param.setKeyCabinetIds(ownPermissionKeyCabinetIds);
        param.setUserId(userId);
        param.setLastKeyWorkflowDetailApplyType(BizConstants.APPLY_TYPE_BORROW);
        List<KeyAndKeyCabinetAndBindingStatusAndLastBorrowPOJO> list = keyMapper.selectKeyAndKeyCabinetAndBindingStatusAndLastBorrowApply(param);

        if (list.isEmpty()) {
            return result;
        }

        // 转换成 VO
        for (KeyAndKeyCabinetAndBindingStatusAndLastBorrowPOJO item : list) {
            KeyAndLastApplyVo vo = new KeyAndLastApplyVo();
            BeanUtils.copyProperties(item, vo);
            if (item.getIsBinding() == null || item.getIsBinding() == 0) {
                vo.setKeyStatus(BizConstants.KEY_UNBOUND);
            }
            result.add(vo);
        }

        // 查询绑定申请明细
        List<KeyWorkflowDetail> kwdList = keyWorkflowDetailMapper.selectLastKeyWorkflowDetailByUserIdAndApplyType(userId, BizConstants.APPLY_TYPE_BIND);
        // 转成map方便查找
        Map<Long, KeyWorkflowDetail> kwdMap = kwdList.stream().collect(Collectors.toMap(KeyWorkflowDetail::getKeyId, Function.identity(), (a, b) -> a));

        // 填充每个 vo 的最后一次绑定申请信息
        for (KeyAndLastApplyVo vo : result) {
            KeyWorkflowDetail detail = kwdMap.get(vo.getKeyId().longValue());
            if (detail != null) {
                vo.setLastBindingWorkflowDetailsStatus(detail.getStatus().intValue());
            }
        }

        return result;
    }

    @Override
    public List<KeyWorkflowDetailVo> queryTargetUserApplyOrApproval(Integer targetUserId) {
        KeyWorkflowDetailAndKeyPOJO detailAndKeyAllInfo = new KeyWorkflowDetailAndKeyPOJO();
        detailAndKeyAllInfo.setTargetUserId(targetUserId);
        detailAndKeyAllInfo.setWorkflowCurrentStatus(BizConstants.PENDING_APPROVAL.longValue());
        List<KeyWorkflowDetailAndKeyPOJO> dbList = keyWorkflowMapper.selectWorkflowDetailKeyWorkflowDetailAndKeyPOJOByTargetUserId(detailAndKeyAllInfo);
        return getKeyWorkflowDetailVoList(dbList);
    }

    /**
     * 构建成以单子为一级，钥匙为二级数据形式
     *
     * @param
     * @return
     */
    private List<KeyWorkflowDetailVo> getKeyWorkflowDetailVoList(List<KeyWorkflowDetailAndKeyPOJO> list) {
        List<KeyWorkflowDetailVo> resultList = new ArrayList<>();
        if (list == null || list.isEmpty()) {
            return resultList;
        }
        // 用 Map 临时分组，key = workflowId
        Map<Long, KeyWorkflowDetailVo> workflowMap = new LinkedHashMap<>();

        for (KeyWorkflowDetailAndKeyPOJO pojo : list) {

            KeyWorkflowDetailVo workflowVo = workflowMap.get(pojo.getWorkflowId());

            if (workflowVo == null) {
                // 第一次遇到这个 workflowId，新建一个基础信息
                workflowVo = buildKeyWorkflowBaseInfo(pojo);

                workflowMap.put(pojo.getWorkflowId(), workflowVo);
            }

            //  构建钥匙和详情信息并添加到工单中
            workflowVo.getWorkflowDetailsAndKeyList().add(buildKeyIncludeDetailsStatus(pojo));
        }

        resultList.addAll(workflowMap.values());
        return resultList;
    }

    /**
     * 构建vo
     *
     * @param pojo
     * @return
     */
    private KeyWorkflowDetailVo buildKeyWorkflowBaseInfo(KeyWorkflowDetailAndKeyPOJO pojo) {
        KeyWorkflowDetailVo workflowVo = new KeyWorkflowDetailVo();
        workflowVo.setId(pojo.getWorkflowId().intValue());
        workflowVo.setWorkflowNo(pojo.getWorkflowNo());
        workflowVo.setApplyUserId(pojo.getApplyUserId() != null ? pojo.getApplyUserId().intValue() : null);
        workflowVo.setApplyUserName(pojo.getApplyUserName());
        workflowVo.setApplyTime(pojo.getApplyTime());
        workflowVo.setApplyReason(pojo.getApplyReason());
        workflowVo.setExpectedTime(pojo.getExpectedTime());
        workflowVo.setApprovalUserId(pojo.getApprovalUserId() != null ? pojo.getApprovalUserId().intValue() : null);
        workflowVo.setApprovalUserName(pojo.getApprovalUserName());
        workflowVo.setApprovalTime(pojo.getApprovalTime());
        workflowVo.setApprovalComment(pojo.getApprovalComment());
        workflowVo.setCurrentStatus(pojo.getWorkflowCurrentStatus() != null ? pojo.getWorkflowCurrentStatus().intValue() : null);
        workflowVo.setWorkflowDetailsAndKeyList(new ArrayList<>());

        return workflowVo;
    }

    /**
     * 构建工单详情&&钥匙&&所在钥匙柜信息元素
     *
     * @param pojo
     * @return
     */
    private WorkflowDetailsAndKeyAndKeyCabinet buildKeyIncludeDetailsStatus(KeyWorkflowDetailAndKeyPOJO pojo) {
        WorkflowDetailsAndKeyAndKeyCabinet keyVo = new WorkflowDetailsAndKeyAndKeyCabinet();
        //钥匙基础信息
        keyVo.setKeyId(pojo.getKeyId());
        keyVo.setKeyCode(pojo.getKeyCode());
        keyVo.setKeyName(pojo.getKeyName());
        keyVo.setKeyPosition(pojo.getKeyPosition());
        keyVo.setKeyUsageLocation(pojo.getKeyUsageLocation());
        keyVo.setKeyTag(pojo.getKeyTag());
        keyVo.setKeyCurrentTag(pojo.getKeyCurrentTag());
        keyVo.setKeyControlPanelAddress(pojo.getKeyControlPanelAddress());
        keyVo.setKeyCircuitPanelAddress(pojo.getKeyCircuitPanelAddress());
        keyVo.setKeyCreateAt(pojo.getKeyCreateAt());
        keyVo.setKeyLastControlUserId(pojo.getKeyLastControlUserId());
        keyVo.setKeyStatus(pojo.getKeyStatus());
        //所在钥匙柜基础信息
        keyVo.setKeyCabinetId(pojo.getKeyCabinetId());
        keyVo.setKeyCabinetNumber(pojo.getKeyCabinetNumber());
        keyVo.setKeyCabinetName(pojo.getKeyCabinetName());
        keyVo.setKeyCabinetAddressLocation(pojo.getKeyCabinetAddressLocation());
        //流程单详情信息
        keyVo.setKeyCabinetAddressLocation(pojo.getKeyCabinetAddressLocation());
        keyVo.setKeyWorkflowDetailStatus(pojo.getDetailStatus());
        keyVo.setKeyWorkflowDetailApplyType(pojo.getApplyType());
        keyVo.setOfflineData(pojo.getDetailOfflineData());
        return keyVo;
    }

    /**
     * 校验钥匙是否可借出
     *
     * @param keyIds 钥匙ids
     * @param userId 用户id
     */
    private void validateKeyCanBeApplied(Integer[] keyIds, Integer userId) {
        Key key = new Key();
        key.setStatus(BizConstants.KEY_AVAILABLE);
        key.setUserId(userId.toString());
        List<Key> keyListByParam = keyMapper.selectKeyListByKeyIdAndUserId(key);
        if (keyListByParam.isEmpty()) {
            throw new RuntimeException("部分钥匙不存在");
        }

        Set<Long> validKeyIds = keyListByParam.stream().map(Key::getId).collect(Collectors.toSet());

        for (Integer keyId : keyIds) {
            if (!validKeyIds.contains(keyId.longValue())) {
                throw new RuntimeException("申请借用的部分钥匙未和当前用户未绑定或不可用");
            }
            KeyWorkflowDetail keyWorkflowDetail = keyWorkflowDetailMapper.selectLastKeyWorkflowDetailByKeyIdAndApplyType(keyId, BizConstants.APPLY_TYPE_BORROW);
            //没借用申请过
            if (keyWorkflowDetail == null) {
                break;
            }
            //最后一条申请已结束
            if (Long.valueOf(BizConstants.BORROW_APPROVED_PICKED_OR_REJECTED).equals(keyWorkflowDetail.getStatus())) {
                break;
            }
            throw new RuntimeException("部分钥匙正在被借用申请中");
        }
    }

    /**
     * 校验钥匙是否可以和用户绑定
     *
     * @param keyIds
     * @param userId
     */
    private void validateKeyCanBeBound(Integer[] keyIds, Integer userId) {

        //判断是否已经绑定过了
        List<Key> keys = keyMapper.selectBoundKeysByUser(userId);
        Set<Integer> keyIdSet = new HashSet<>(Arrays.asList(keyIds));
        Set<Integer> boundKeyIds = keys.stream().map(x -> x.getId().intValue()).collect(Collectors.toSet());
        if (!Collections.disjoint(keyIdSet, boundKeyIds)) {
            throw new RuntimeException("申请绑定的部分钥匙已是绑定状态");
        }
        //判断是否有正在提交的绑定申请
        List<KeyWorkflowDetail> keyWorkflowDetailList = keyWorkflowDetailMapper.selectKeyWorkflowDetailByUserIdAndApplyType(userId, BizConstants.APPLY_TYPE_BIND);
        Set<Long> awaitFinishKeyIdSet = keyWorkflowDetailList.stream().filter(x -> x.getStatus() != BizConstants.BIND_END_DETAIL_STATUS).map(y -> y.getKeyId()).collect(Collectors.toSet());
        boolean hasIntersection = Arrays.stream(keyIds).map(Long::valueOf).anyMatch(awaitFinishKeyIdSet::contains);
        if (hasIntersection) {
            throw new RuntimeException("申请绑定的部分钥匙正在审批过程中");
        }
    }

    /**
     * 校验申请和审批的用户是否有效
     *
     * @param keyApplyDto
     */
    private void validateApplyUserAndApprovalUser(KeyApplyDto keyApplyDto) {

        List<UserBase> userList = userMapper.selectUserBaseListByIds(
                new Integer[]{keyApplyDto.getApplyUserId(), keyApplyDto.getApprovalUserId()});

        // 将用户列表转成 Map，key = id，value = User，方便快速查找
        Map<Integer, UserBase> userMap = userList.stream()
                .filter(u -> u.getId() != null)
                .collect(Collectors.toMap(UserBase::getId, Function.identity(), (u1, u2) -> u1));

        // 校验申请用户是否存在
        if (!userMap.containsKey(keyApplyDto.getApplyUserId())) {
            throw new RuntimeException("申请钥匙使用的用户不存在");
        }
        UserBase user = userMap.get(keyApplyDto.getApplyUserId());
        // 用户拥有权限的钥匙柜
        Set<Integer> ownCabinetIdSet = StringUtils.strToIntegerSet(user.getSrttings(), ",");
        // 用户拥有的钥匙柜权限中，没有申请的钥匙柜
        if (!ownCabinetIdSet.contains(keyApplyDto.getKeyCabinetId())) {
            throw new RuntimeException("暂未拥有此钥匙所在的钥匙柜权限");
        }
        if ("false".equals(user.getActive())) {
            throw new RuntimeException("用户未激活");
        }
        // 校验审批人是否存在
        UserBase approvalUser = Optional.ofNullable(keyApplyDto.getApprovalUserId())
                .map(userMap::get)
                .orElseThrow(() -> new RuntimeException("申请钥匙使用指定的审批人不存在"));

        // 校验审批人角色
        if (!BizConstants.ADMIN.equals(approvalUser.getRole())) {
            throw new RuntimeException("申请钥匙使用指定的审批人并非管理员");
        }
    }
}
