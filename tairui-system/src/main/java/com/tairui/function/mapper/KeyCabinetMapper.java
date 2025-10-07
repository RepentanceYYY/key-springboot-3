package com.tairui.function.mapper;

import com.tairui.function.domain.KeyCabinet;
import com.tairui.function.domain.vo.KeyCabinetSummary;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 自定义钥匙柜mapper
 */
public interface KeyCabinetMapper {
    public List<KeyCabinetSummary> selectKeyCabinetSummaryByUserId(@Param("userId") Long userId);
    public List<KeyCabinetSummary> selectKeyCabinetSummary();
    public List<KeyCabinet> selectAllKeyCabinet();
    public KeyCabinet selectKeyCabinetById(@Param("keyCabinetId")Integer keyCabinetId);

}
