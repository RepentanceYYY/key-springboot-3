package com.tairui.function.service.impl;

import com.tairui.function.domain.LogsView;
import com.tairui.function.mapper.LogsViewMapper;
import com.tairui.function.service.ILogsViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 操作记录Service业务层处理
 *
 * @author tairui
 * @date 2025-07-31
 */
@Service
public class LogsViewServiceImpl implements ILogsViewService
{
    @Autowired
    private LogsViewMapper logsViewMapper;

    /**
     * 查询操作记录
     *
     * @param id 操作记录主键
     * @return 操作记录
     */
    @Override
    public LogsView selectLogsViewById(Long id)
    {
        return logsViewMapper.selectLogsViewById(id);
    }

    /**
     * 查询操作记录列表
     *
     * @param logsView 操作记录
     * @return 操作记录
     */
    @Override
    public List<LogsView> selectLogsViewList(LogsView logsView)
    {
        return logsViewMapper.selectLogsViewList(logsView);
    }

    /**
     * 新增操作记录
     *
     * @param logsView 操作记录
     * @return 结果
     */
    @Override
    public int insertLogsView(LogsView logsView)
    {
        return logsViewMapper.insertLogsView(logsView);
    }

    /**
     * 修改操作记录
     *
     * @param logsView 操作记录
     * @return 结果
     */
    @Override
    public int updateLogsView(LogsView logsView)
    {
        return logsViewMapper.updateLogsView(logsView);
    }

    /**
     * 批量删除操作记录
     *
     * @param ids 需要删除的操作记录主键
     * @return 结果
     */
    @Override
    public int deleteLogsViewByIds(Long[] ids)
    {
        return logsViewMapper.deleteLogsViewByIds(ids);
    }

    /**
     * 删除操作记录信息
     *
     * @param id 操作记录主键
     * @return 结果
     */
    @Override
    public int deleteLogsViewById(Long id)
    {
        return logsViewMapper.deleteLogsViewById(id);
    }
}
