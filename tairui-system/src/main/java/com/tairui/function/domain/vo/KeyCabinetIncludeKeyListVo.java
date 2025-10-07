package com.tairui.function.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * 钥匙柜概要以及它的所有已初始化钥匙 vo
 */
@Data
public class KeyCabinetIncludeKeyListVo extends KeyCabinetSummary {
    /**
     * (已初始化钥匙以及绑定情况和在审批流中最后使用情况)列表
     */
    List<KeyAndLastApplyVo> keyAndLastApplyList;
}
