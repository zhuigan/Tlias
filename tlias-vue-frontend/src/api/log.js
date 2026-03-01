import request from './request'

export const getOperationLogs = (limit = 100) => {
  return request({
    url: '/logs/recent',
    method: 'get',
    params: { limit }
  })
}

export const getAllLogs = () => {
  return request({
    url: '/logs',
    method: 'get'
  })
}
