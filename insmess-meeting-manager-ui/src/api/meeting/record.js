import request from '@/utils/request'

// 查询会议录屏列表
export function listRecord(query) {
  return request({
    url: '/meeting/record/list',
    method: 'get',
    params: query
  })
}