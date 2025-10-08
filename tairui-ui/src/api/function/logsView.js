import request from '@/utils/request'

// 查询操作记录列表
export function listLogsView(query) {
  return request({
    url: '/function/logsView/list',
    method: 'get',
    params: query
  })
}

// 查询操作记录详细
export function getLogsView(id) {
  return request({
    url: '/function/logsView/' + id,
    method: 'get'
  })
}

// 新增操作记录
export function addLogsView(data) {
  return request({
    url: '/function/logsView',
    method: 'post',
    data: data
  })
}

// 修改操作记录
export function updateLogsView(data) {
  return request({
    url: '/function/logsView',
    method: 'put',
    data: data
  })
}

// 删除操作记录
export function delLogsView(id) {
  return request({
    url: '/function/logsView/' + id,
    method: 'delete'
  })
}
