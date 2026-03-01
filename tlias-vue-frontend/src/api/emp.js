import request from './request'

export const getEmpList = (params) => {
  return request({
    url: '/emps',
    method: 'get',
    params
  })
}

export const getEmpById = (id) => {
  return request({
    url: `/emps/${id}`,
    method: 'get'
  })
}

export const getCurrentUser = () => {
  return request({
    url: '/emps/current',
    method: 'get'
  })
}

export const addEmp = (data) => {
  return request({
    url: '/emps',
    method: 'post',
    data
  })
}

export const updateEmp = (data) => {
  return request({
    url: '/emps',
    method: 'put',
    data
  })
}

export const deleteEmp = (ids) => {
  return request({
    url: `/emps/${ids.join(',')}`,
    method: 'delete'
  })
}

export const updateAvatar = (data) => {
  return request({
    url: '/emps/avatar',
    method: 'put',
    data
  })
}

export const uploadFile = (file) => {
  const formData = new FormData()
  formData.append('image', file)
  return request({
    url: '/upload',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}
