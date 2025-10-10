package com.tairui.function.service.impl;

import com.tairui.common.core.redis.RedisCache;
import com.tairui.function.domain.KeyCabinet;
import com.tairui.function.domain.KeyWorkflow;
import com.tairui.function.domain.SystemSettings;
import com.tairui.function.domain.UserBase;
import com.tairui.function.domain.dto.KeyApprovalDto;
import com.tairui.function.domain.dto.KeyCabinetPermissionDTO;
import com.tairui.function.domain.vo.*;
import com.tairui.function.mapper.*;
import com.tairui.function.service.IKeyCabinetService;
import com.tairui.function.service.IKeyFromAppService;
import com.tairui.utils.BizConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class KeyCabinetService implements IKeyCabinetService {
    @Autowired
    private KeyCabinetMapper keyCabinetMapper;
    @Autowired
    private IKeyFromAppService keyFromAppService;
    @Autowired
    private KeyMapper keyMapper;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private KeyWorkflowMapper keyWorkflowMapper;
    @Autowired
    private SystemSettingsMapper systemSettingsMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public List<KeyCabinetIncludeKeyListVo> selectKeyCabinetIncludeKeyList(Integer userId) {
        UserBase user = userMapper.selectUserBaseById(userId);
        if (user == null) {
            throw new RuntimeException("非法访问");
        }
        List<KeyCabinetSummary> keyCabinetSummaryList;
        if (BizConstants.ADMIN.equals(user.getRole())) {
            keyCabinetSummaryList = keyCabinetMapper.selectKeyCabinetSummary();
        } else {
            keyCabinetSummaryList = keyCabinetMapper.selectKeyCabinetSummaryByUserId(userId.longValue());
        }

        List<KeyAndLastApplyVo> keyAndLastApplyVoList = keyFromAppService.queryKeyAndLastBorrowApplyMyLastBindingApplyList(userId);
        return buildKeyCabinetIncludeKeysList(keyCabinetSummaryList, keyAndLastApplyVoList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void manageUserOwnKeyCabinetPermissions(KeyCabinetPermissionDTO keyCabinetPermissionDTO) {
        //检查钥匙柜是否存在
        this.existsKeyCabinet(keyCabinetPermissionDTO.getKeyCabinetId());
        //获取所有用户以及拥有的钥匙柜str
        List<UserSimpleVo> userSimpleVos = userMapper.selectUserSimpleVo();
        //检查用户是否都是有效用户
        existsAllUser(keyCabinetPermissionDTO, userSimpleVos);
        Set<Integer> userIdSet = new HashSet<>(Arrays.asList(keyCabinetPermissionDTO.getUserIds()));
        //只要传进来的用户
        userSimpleVos = userSimpleVos.stream().filter(x -> userIdSet.contains(x.getUserId())).collect(Collectors.toList());
        //转成Map类型，也许查询快一点
        Map<Integer, UserSimpleVo> userMap = userSimpleVos.stream()
                .collect(Collectors.toMap(UserSimpleVo::getUserId, vo -> vo));
        //转成Map类型进行操作
        Map<Integer, List<Integer>> userIdAndOwnCabinetIds = buildUserIdAndOwnCabinetIds(userSimpleVos);
        for (Map.Entry<Integer, List<Integer>> entry : userIdAndOwnCabinetIds.entrySet()) {

            if (keyCabinetPermissionDTO.getActionType() == 1) {
                //检查是否可以分配权限
                checkCanAssign(entry, userMap, keyCabinetPermissionDTO.getKeyCabinetId());
            } else if (keyCabinetPermissionDTO.getActionType() == 0) {
                //检查是否可以解除权限
                checkRevokeAssign(entry, userMap, keyCabinetPermissionDTO.getKeyCabinetId());
                //自动解绑钥匙
                autoUnbindKeyBecauseUnbindKeyCabinet(entry.getKey(), keyCabinetPermissionDTO.getKeyCabinetId());
                //自动拒绝所有待审批申请
                autoRejectedApplyBecauseUnbindKeyCabinet(entry.getKey(), keyCabinetPermissionDTO.getKeyCabinetId());

            } else {
                throw new RuntimeException("未知操作类型");
            }
            String ownKeyCabinetIdsStr;
            if (entry.getValue() == null || entry.getValue().isEmpty()) {
                ownKeyCabinetIdsStr = null;
            } else if (entry.getValue().size() == 1) {
                ownKeyCabinetIdsStr = String.valueOf(entry.getValue().get(0));
            } else {
                ownKeyCabinetIdsStr = entry.getValue().stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(","));
            }
            userMapper.updateUserOwnCabinetPermissions(entry.getKey(), ownKeyCabinetIdsStr);

        }
    }

    /**
     * 自动拒绝用户待审批的申请因为解绑钥匙柜
     *
     * @param userId
     * @param keyCabinetId
     */
    private void autoRejectedApplyBecauseUnbindKeyCabinet(Integer userId, Integer keyCabinetId) {
        //拒绝该用户在目标钥匙柜的待审批申请
        List<KeyWorkflow> keyWorkflows = keyWorkflowMapper.selectKeyWorkflowListByUserIdAndKeyCabinetId(userId, keyCabinetId, BizConstants.PENDING_APPROVAL);
        if (keyWorkflows.size() > 0) {
            //尽量避免受操作数据库的时间影响，先删除redis中的挂起
            for (KeyWorkflow keyWorkflowTmp : keyWorkflows) {
                //不管在不在直接删除redis中对应的key
                String redisKey = BizConstants.APPLY_CACHE_KEY_PREFIX + keyWorkflowTmp.getId();
                redisCache.deleteObject(redisKey);
            }
            for (KeyWorkflow keyWorkflowTmp : keyWorkflows) {
                KeyApprovalDto keyApprovalTmp = new KeyApprovalDto();
                keyApprovalTmp.setKeyWorkflowId(keyWorkflowTmp.getId().intValue());
                keyApprovalTmp.setApprovalUserId(keyWorkflowTmp.getApprovalUserId().intValue());
                keyApprovalTmp.setApprovalComment("申请人已与申请的钥匙所在钥匙柜解绑，申请自动拒绝");
                keyApprovalTmp.setApprovalAction(BizConstants.REJECTED);
                keyFromAppService.keyApproval(keyApprovalTmp, false);
            }
        }
    }

    /**
     * 自动解绑钥匙因为解绑钥匙柜
     *
     * @param userId
     * @param keyCabinetId
     */
    private void autoUnbindKeyBecauseUnbindKeyCabinet(Integer userId, Integer keyCabinetId) {
        List<Integer> keyIdsByCabinet = keyMapper.selectKeyIdsByKeyCabinetId(keyCabinetId);
        List<Integer> keyIdsByUser = keyMapper.selectKeyIdsByUserId(userId);

        // 求交集
        List<Integer> targetKeyIds = keyIdsByCabinet.stream()
                .filter(keyIdsByUser::contains)
                .collect(Collectors.toList());

        if (targetKeyIds.isEmpty()) {
            return; // 没有需要解绑的
        }

        keyMapper.batchUnBindKeysByUserId(userId, targetKeyIds);
    }

    @Override
    public List<KeyCabinetsUsersPermissionVo> queryKeyCabinetAndUserList() {
        List<KeyCabinetsUsersPermissionVo> result = new ArrayList<>();
        List<KeyCabinet> allKeyCabinet = keyCabinetMapper.selectAllKeyCabinet();
        if (allKeyCabinet.isEmpty()) {
            return result;
        }
        List<UserSimpleVo> userSimpleVos = userMapper.selectUserSimpleVo();
        //过滤掉管理员
        userSimpleVos = userSimpleVos.stream().filter(x -> !"管理员".equals(x.getUserRole())).collect(Collectors.toList());
        //转成Map类型进行操作(用户id,拥有权限的柜子id集合)
        Map<Integer, List<Integer>> userIdAndOwnCabinetIds = buildUserIdAndOwnCabinetIds(userSimpleVos);
        //转成Map类型(用户id，用户信息)
        Map<Integer, UserSimpleVo> userMap = userSimpleVos.stream()
                .collect(Collectors.toMap(UserSimpleVo::getUserId, vo -> vo));

        UserPermissionVo userPermissionTmp;
        KeyCabinetsUsersPermissionVo vo;

        for (KeyCabinet keyCabinet : allKeyCabinet) {
            vo = new KeyCabinetsUsersPermissionVo();
            vo.setKeyCabinetId(keyCabinet.getId());
            vo.setKeyCabinetNumber(keyCabinet.getKeyNumber());
            vo.setKeyCabinetName(keyCabinet.getCabinetName());
            vo.setKeyCabinetAddressLocation(keyCabinet.getAddressLocation());
            vo.setUserPermissionList(new ArrayList<>());
            int ownPermissionUserCountTmp = 0;
            for (Map.Entry<Integer, List<Integer>> x : userIdAndOwnCabinetIds.entrySet()) {

                UserSimpleVo userSimpleVo = userMap.get(x.getKey());
                userPermissionTmp = new UserPermissionVo();
                userPermissionTmp.setUserId(userSimpleVo.getUserId());
                userPermissionTmp.setUserNikeName(userSimpleVo.getUserNikeName());
                userPermissionTmp.setUserName(userSimpleVo.getUserName());
                userPermissionTmp.setUserActive(userSimpleVo.getUserActive());
                userPermissionTmp.setUserCreatedAt(userSimpleVo.getUserCreatedAt());
                //包含则代表有权限
                boolean isContains = x.getValue().contains(keyCabinet.getId());
                userPermissionTmp.setOwnPermission(isContains);
                vo.getUserPermissionList().add(userPermissionTmp);
                if (isContains) ownPermissionUserCountTmp += 1;
            }
            vo.setUserCount(userSimpleVos.size());
            vo.setOwnPermissionUserCount(ownPermissionUserCountTmp);
            result.add(vo);
        }
        return result;
    }

    @Override
    public KeyCabinetsUsersPermissionVo queryKeyCabinetAndUserListById(Integer keyCabinetId) {
        KeyCabinet keyCabinet = keyCabinetMapper.selectKeyCabinetById(keyCabinetId);
        if (keyCabinet == null) {
            throw new RuntimeException("钥匙柜不存在");
        }
        List<UserSimpleVo> userSimpleVos = userMapper.selectUserSimpleVo();
        //过滤掉管理员
        userSimpleVos = userSimpleVos.stream().filter(x -> !"管理员".equals(x.getUserRole())).collect(Collectors.toList());
        //转成Map类型进行操作(用户id,拥有权限的柜子id集合)
        Map<Integer, List<Integer>> userIdAndOwnCabinetIds = buildUserIdAndOwnCabinetIds(userSimpleVos);
        //转成Map类型(用户id，用户信息)
        Map<Integer, UserSimpleVo> userMap = userSimpleVos.stream()
                .collect(Collectors.toMap(UserSimpleVo::getUserId, vo -> vo));

        UserPermissionVo userPermissionTmp;


        KeyCabinetsUsersPermissionVo vo = new KeyCabinetsUsersPermissionVo();
        vo.setKeyCabinetId(keyCabinet.getId());
        vo.setKeyCabinetNumber(keyCabinet.getKeyNumber());
        vo.setKeyCabinetName(keyCabinet.getCabinetName());
        vo.setKeyCabinetAddressLocation(keyCabinet.getAddressLocation());
        vo.setUserPermissionList(new ArrayList<>());
        int ownPermissionUserCountTmp = 0;
        for (Map.Entry<Integer, List<Integer>> x : userIdAndOwnCabinetIds.entrySet()) {

            UserSimpleVo userSimpleVo = userMap.get(x.getKey());
            userPermissionTmp = new UserPermissionVo();
            userPermissionTmp.setUserId(userSimpleVo.getUserId());
            userPermissionTmp.setUserNikeName(userSimpleVo.getUserNikeName());
            userPermissionTmp.setUserName(userSimpleVo.getUserName());
            userPermissionTmp.setUserActive(userSimpleVo.getUserActive());
            userPermissionTmp.setUserCreatedAt(userSimpleVo.getUserCreatedAt());
            //包含则代表有权限
            boolean isContains = x.getValue().contains(keyCabinet.getId());
            userPermissionTmp.setOwnPermission(isContains);
            vo.getUserPermissionList().add(userPermissionTmp);
            if (isContains) ownPermissionUserCountTmp += 1;
        }
        vo.setUserCount(userSimpleVos.size());
        vo.setOwnPermissionUserCount(ownPermissionUserCountTmp);
        return vo;
    }


    private Map<Integer, List<Integer>> buildUserIdAndOwnCabinetIds(List<UserSimpleVo> userSimpleVos) {

        Map<Integer, List<Integer>> userIdAndOwnCabinetIds = new HashMap<>();

        for (UserSimpleVo x : userSimpleVos) {
            List<Integer> list = (x.getKeyCabinetsStr() == null || x.getKeyCabinetsStr().trim().isEmpty())
                    ? new ArrayList<>()
                    : Arrays.stream(x.getKeyCabinetsStr().split(","))
                    .map(String::trim)         // 去除多余空格
                    .filter(s -> !s.isEmpty()) // 过滤掉空字符串
                    .map(Integer::valueOf)     // 转成 Integer
                    .collect(Collectors.toList());

            userIdAndOwnCabinetIds.put(x.getUserId(), list);
        }
        return userIdAndOwnCabinetIds;
    }

    /**
     * 检查是否可以分配权限
     *
     * @param entry
     * @param userMap
     * @param keyCabinetId
     */
    private void checkCanAssign(Map.Entry<Integer, List<Integer>> entry, Map<Integer, UserSimpleVo> userMap, Integer keyCabinetId) {
        boolean contains = entry.getValue().stream().anyMatch(x -> keyCabinetId.equals(x));
        if (contains) {
            throw new RuntimeException("用户 " + userMap.get(entry.getKey()).getUserNikeName() + " 已经分配了该钥匙柜权限，无法重复分配");
        }
        if (entry.getValue() == null) {
            entry.setValue(new ArrayList<>());
        }
        entry.getValue().add(keyCabinetId);
    }

    /**
     * 检查是否可以解除权限分配
     *
     * @param entry
     * @param userMap
     * @param keyCabinetId
     */
    private void checkRevokeAssign(Map.Entry<Integer, List<Integer>> entry, Map<Integer, UserSimpleVo> userMap, Integer keyCabinetId) {
        boolean contains = entry.getValue().stream().anyMatch(x -> keyCabinetId.equals(x));
        if (!contains) {
            throw new RuntimeException("用户 " + userMap.get(entry.getKey()).getUserNikeName() + " 没有该钥匙柜权限，无法重复解除");
        }
        entry.getValue().remove(keyCabinetId);
    }

    /**
     * 检查钥匙柜是否存在
     *
     * @param keyCabinetId
     */
    private void existsKeyCabinet(Integer keyCabinetId) {
        SystemSettings systemSettings = systemSettingsMapper.selectSystemSettingsById(keyCabinetId.longValue());
        if (systemSettings == null) {
            throw new RuntimeException("钥匙柜不存在");
        }
    }

    /**
     * 检查用户是否都有效
     *
     * @param keyCabinetPermissionDTO
     * @param userSimpleVos
     */
    private void existsAllUser(KeyCabinetPermissionDTO keyCabinetPermissionDTO, List<UserSimpleVo> userSimpleVos) {
        Set<Integer> set = userSimpleVos.stream().filter(x -> "true".equals(x.getUserActive())).map(x -> x.getUserId()).collect(Collectors.toSet());
        //判断请求的用户id是否全部有效
        boolean allInSet = Arrays.stream(keyCabinetPermissionDTO.getUserIds()).allMatch(set::contains);
        if (!allInSet) {
            throw new RuntimeException("部分用户已经无效");
        }
    }

    /**
     * 构建钥匙柜(包括属于它的钥匙列表)列表
     *
     * @param keyCabinetSummaryList
     * @param keyList
     * @return
     */
    private List<KeyCabinetIncludeKeyListVo> buildKeyCabinetIncludeKeysList(List<KeyCabinetSummary> keyCabinetSummaryList, List<KeyAndLastApplyVo> keyList) {
        List<KeyCabinetIncludeKeyListVo> resultList = new ArrayList<>();
        if (keyCabinetSummaryList == null || keyList == null) {
            return resultList;
        }
        for (KeyCabinetSummary cabinet : keyCabinetSummaryList) {

            KeyCabinetIncludeKeyListVo vo = new KeyCabinetIncludeKeyListVo();
            // 填充钥匙柜基础信息
            vo.setKeyCabinetId(cabinet.getKeyCabinetId());
            vo.setKeyCabinetNumber(cabinet.getKeyCabinetNumber());
            vo.setKeyCabinetName(cabinet.getKeyCabinetName());
            vo.setKeyCabinetAddressLocation(cabinet.getKeyCabinetAddressLocation());

            // 过滤出属于这个柜子的钥匙
            List<KeyAndLastApplyVo> keysForCurrentCabinet = keyList.stream()
                    .filter(k -> k.getKeyCabinetId() == cabinet.getKeyCabinetId())
                    .collect(Collectors.toList());
            vo.setKeyAndLastApplyList(keysForCurrentCabinet);
            //可借用，说明已绑定，并且在位，并且该钥匙在借用审批流中已走完
            long canBeBorrowedCount = keysForCurrentCabinet.stream().filter(x -> BizConstants.KEY_AVAILABLE.equals(x.getKeyStatus())
                    && (x.getLastBorrowingWorkflowDetailsStatus() == null || x.getLastBorrowingWorkflowDetailsStatus() == BizConstants.BORROW_APPROVED_PICKED_OR_REJECTED)).count();
            vo.setCanBorrowCount((int) canBeBorrowedCount);
            //已初始化数量，存在就是初始化了，因为查询数据库的时候已经过滤了没初始化的
            vo.setInitializedKeySum(keysForCurrentCabinet.size());

            resultList.add(vo);
        }

        return resultList;
    }
}
