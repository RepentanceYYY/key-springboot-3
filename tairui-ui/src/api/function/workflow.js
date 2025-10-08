import request from '@/utils/request'

// 查询钥匙审批流列表
export function listWorkflow(query) {
  return request({
    url: '/function/workflow/list',
    method: 'get',
    params: query
  })
}

// 查询钥匙审批流详细
export function getWorkflow(id) {
  return request({
    url: '/function/workflow/' + id,
    method: 'get'
  })
}

// 新增钥匙审批流
export function addWorkflow(data) {
  return request({
    url: '/function/workflow',
    method: 'post',
    data: data
  })
}

// 修改钥匙审批流
export function updateWorkflow(data) {
  return request({
    url: '/function/workflow',
    method: 'put',
    data: data
  })
}

// 删除钥匙审批流
export function delWorkflow(id) {
  return request({
    url: '/function/workflow/' + id,
    method: 'delete'
  })
}
