import request from '@/utils/request'

// 查询钥匙审批流详情列表
export function listKeyWorkflowDetail(query) {
  return request({
    url: '/function/keyWorkflowDetail/list',
    method: 'get',
    params: query
  })
}

// 查询钥匙审批流详情详细
export function getKeyWorkflowDetail(id) {
  return request({
    url: '/function/keyWorkflowDetail/' + id,
    method: 'get'
  })
}

// 新增钥匙审批流详情
export function addKeyWorkflowDetail(data) {
  return request({
    url: '/function/keyWorkflowDetail',
    method: 'post',
    data: data
  })
}

// 修改钥匙审批流详情
export function updateKeyWorkflowDetail(data) {
  return request({
    url: '/function/keyWorkflowDetail',
    method: 'put',
    data: data
  })
}

// 删除钥匙审批流详情
export function delKeyWorkflowDetail(id) {
  return request({
    url: '/function/keyWorkflowDetail/' + id,
    method: 'delete'
  })
}
