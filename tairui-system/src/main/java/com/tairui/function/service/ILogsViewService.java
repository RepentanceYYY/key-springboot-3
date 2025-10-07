package com.tairui.function.service;

import com.tairui.function.domain.LogsView;

import java.util.List;

/**
 * 操作记录Service接口
 *
 * @author tairui
 * @date 2025-07-31
 */
public interface ILogsViewService
{
    /**
     * 查询操作记录
     *
     * @param id 操作记录主键
     * @return 操作记录
     */
    public LogsView selectLogsViewById(Long id);

    /**
     * 查询操作记录列表
     *
     * @param logsView 操作记录
     * @return 操作记录集合
     */
    public List<LogsView> selectLogsViewList(LogsView logsView);

    /**
     * 新增操作记录
     *
     * @param logsView 操作记录
     * @return 结果
     */
    public int insertLogsView(LogsView logsView);

    /**
     * 修改操作记录
     *
     * @param logsView 操作记录
     * @return 结果
     */
    public int updateLogsView(LogsView logsView);

    /**
     * 批量删除操作记录
     *
     * @param ids 需要删除的操作记录主键集合
     * @return 结果
     */
    public int deleteLogsViewByIds(Long[] ids);

    /**
     * 删除操作记录信息
     *
     * @param id 操作记录主键
     * @return 结果
     */
    public int deleteLogsViewById(Long id);
}
