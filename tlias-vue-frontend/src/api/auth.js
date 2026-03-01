import request from './request'

export const login = (data) => {
  return request({
    url: '/login',
    method: 'post',
    data
  })
}

export const register = (data) => {
  return request({
    url: '/register',
    method: 'post',
    data
  })
}
