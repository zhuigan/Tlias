import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')
  const userInfo = ref(null)

  const isLoggedIn = computed(() => !!token.value)
  const isAdmin = computed(() => userInfo.value?.role === 1)

  function setToken(newToken) {
    token.value = newToken
    localStorage.setItem('token', newToken)
    
    // 解析token获取用户信息
    if (newToken) {
      try {
        const payload = JSON.parse(atob(newToken.split('.')[1]))
        userInfo.value = payload
      } catch (e) {
        console.error('解析token失败', e)
      }
    }
  }

  function clearToken() {
    token.value = ''
    userInfo.value = null
    localStorage.removeItem('token')
  }

  // 初始化时解析token
  if (token.value) {
    try {
      const payload = JSON.parse(atob(token.value.split('.')[1]))
      userInfo.value = payload
    } catch (e) {
      console.error('解析token失败', e)
      clearToken()
    }
  }

  return {
    token,
    userInfo,
    isLoggedIn,
    isAdmin,
    setToken,
    clearToken
  }
})
