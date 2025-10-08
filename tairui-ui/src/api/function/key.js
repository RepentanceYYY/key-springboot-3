import request from '@/utils/request'

// 查询钥匙列表
export function listKey(query) {
  return request({
    url: '/function/key/list',
    method: 'get',
    params: query
  })
}


// 一键绑定所有钥匙
export function batchBindKeys(srttings) {
  return request({
    url: '/function/key/batchBindKeys/' + srttings,
    method: 'post'  // 修改为POST方法更符合语义
  })
}

// 一键解绑所有钥匙
export function batchUnBindKeys(srttings) {  // 修正方法名拼写
  return request({
    url: '/function/key/batchUnBindKeys/' + srttings,  // 修正URL拼写
    method: 'post'  // 修改为POST方法更符合语义
  })
}



// 查询钥匙详细
export function getKey(id) {
  return request({
    url: '/function/key/' + id,
    method: 'get'
  })
}
// 钥匙初始化
export function initKeyApi(keyId) {
  return request({
    url: '/function/key/initKeyApi/' + keyId,
    method: 'get'
  })
}


// 新增钥匙
export function addKey(data) {
  return request({
    url: '/function/key',
    method: 'post',
    data: data
  })
}

// 修改钥匙
export function updateKey(data) {
  return request({
    url: '/function/key',
    method: 'put',
    data: data
  })
}

// 删除钥匙
export function delKey(id) {
  return request({
    url: '/function/key/' + id,
    method: 'delete'
  })
}
