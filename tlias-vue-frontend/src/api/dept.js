import request from './request'

export const getDeptList = () => {
  return request({
    url: '/depts',
    method: 'get'
  })
}

export const getDeptById = (id) => {
  return request({
    url: `/depts/${id}`,
    method: 'get'
  })
}

export const addDept = (data) => {
  return request({
    url: '/depts',
    method: 'post',
    data
  })
}

export const updateDept = (data) => {
  return request({
    url: '/depts',
    method: 'put',
    data
  })
}

export const deleteDept = (id) => {
  return request({
    url: `/depts/${id}`,
    method: 'delete'
  })
}
