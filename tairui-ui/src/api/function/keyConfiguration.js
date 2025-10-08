import request from '@/utils/request'

// 查询钥匙柜配置列表
export function listKeyConfiguration(query) {
  return request({
    url: '/function/keyConfiguration/list',
    method: 'get',
    params: query
  })
}

// 查询钥匙柜配置列表（已绑定 且没有分页）
export function listKeyConfiguration2(query) {
  return request({
    url: '/function/keyConfiguration/list2',
    method: 'get',
    params: query
  })
}
// 查询钥匙柜配置详细
export function getKeyConfiguration(id) {
  return request({
    url: '/function/keyConfiguration/' + id,
    method: 'get'
  })
}

// 新增钥匙柜配置
export function addKeyConfiguration(data) {
  return request({
    url: '/function/keyConfiguration',
    method: 'post',
    data: data
  })
}

// 修改钥匙柜配置
export function updateKeyConfiguration(data) {
  return request({
    url: '/function/keyConfiguration',
    method: 'put',
    data: data
  })
}

// 删除钥匙柜配置
export function delKeyConfiguration(id) {
  return request({
    url: '/function/keyConfiguration/' + id,
    method: 'delete'
  })
}

