import request from '@/utils/request'

// 查询高级配置列表
export function listAuth(query) {
  return request({
    url: '/function/auth/list',
    method: 'get',
    params: query
  })
}

// 查询高级配置详细
export function getAuth(id) {
  return request({
    url: '/function/auth/' + id,
    method: 'get'
  })
}

// 新增高级配置
export function addAuth(data) {
  return request({
    url: '/function/auth',
    method: 'post',
    data: data
  })
}

// 修改高级配置
export function updateAuth(data) {
  return request({
    url: '/function/auth',
    method: 'put',
    data: data
  })
}

// 删除高级配置
export function delAuth(id) {
  return request({
    url: '/function/auth/' + id,
    method: 'delete'
  })
}
