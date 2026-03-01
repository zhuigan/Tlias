<template>
  <div class="register-container">
    <div class="register-box">
      <div class="logo">
        <h1>🏢 员工注册</h1>
        <p>Tlias 员工管理系统</p>
      </div>

      <el-alert
        title="注册说明"
        type="info"
        :closable="false"
        style="margin-bottom: 20px"
      >
        <ul style="margin-left: 20px; margin-top: 8px; font-size: 13px;">
          <li>用户名和密码为必填项</li>
          <li>注册成功后默认为普通用户</li>
          <li>可以使用注册的账号登录系统</li>
        </ul>
      </el-alert>

      <el-form :model="registerForm" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="registerForm.username" placeholder="请输入用户名" />
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-input v-model="registerForm.password" type="password" placeholder="请输入密码" />
        </el-form-item>

        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="registerForm.confirmPassword" type="password" placeholder="请再次输入密码" />
        </el-form-item>

        <el-form-item label="姓名" prop="name">
          <el-input v-model="registerForm.name" placeholder="请输入真实姓名" />
        </el-form-item>

        <el-form-item label="性别" prop="gender">
          <el-radio-group v-model="registerForm.gender">
            <el-radio :label="1">男</el-radio>
            <el-radio :label="2">女</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="职位">
          <el-select v-model="registerForm.job" placeholder="请选择职位" style="width: 100%">
            <el-option label="班主任" :value="1" />
            <el-option label="讲师" :value="2" />
            <el-option label="学工主管" :value="3" />
            <el-option label="教研主管" :value="4" />
            <el-option label="咨询师" :value="5" />
          </el-select>
        </el-form-item>

        <el-form-item label="部门">
          <el-select v-model="registerForm.deptId" placeholder="请选择部门" style="width: 100%">
            <el-option
              v-for="dept in deptList"
              :key="dept.id"
              :label="dept.name"
              :value="dept.id"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="入职日期">
          <el-date-picker
            v-model="registerForm.entrydate"
            type="date"
            placeholder="选择日期"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
            style="width: 100%"
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" :loading="loading" @click="handleRegister" style="width: 100%">
            立即注册
          </el-button>
          <el-button @click="router.push('/login')" style="width: 100%; margin-top: 10px">
            返回登录
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { register } from '@/api/auth'
import { getDeptList } from '@/api/dept'

const router = useRouter()
const formRef = ref(null)
const loading = ref(false)
const deptList = ref([])

const registerForm = reactive({
  username: '',
  password: '',
  confirmPassword: '',
  name: '',
  gender: null,
  job: null,
  deptId: null,
  entrydate: new Date().toISOString().split('T')[0]
})

const validatePassword = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次输入密码'))
  } else if (value !== registerForm.password) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, message: '用户名至少3个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码至少6个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, validator: validatePassword, trigger: 'blur' }
  ],
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' }
  ],
  gender: [
    { required: true, message: '请选择性别', trigger: 'change' }
  ]
}

const loadDepts = async () => {
  try {
    const res = await getDeptList()
    deptList.value = res.data
  } catch (error) {
    console.error('加载部门列表失败', error)
  }
}

const handleRegister = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        const { confirmPassword, ...data } = registerForm
        await register(data)
        ElMessage.success('注册成功，3秒后跳转到登录页面')
        setTimeout(() => {
          router.push('/login')
        }, 3000)
      } catch (error) {
        console.error('注册失败', error)
      } finally {
        loading.value = false
      }
    }
  })
}

onMounted(() => {
  loadDepts()
})
</script>

<style scoped>
.register-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20px;
}

.register-box {
  background: white;
  padding: 40px;
  border-radius: 15px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);
  width: 100%;
  max-width: 600px;
  max-height: 90vh;
  overflow-y: auto;
}

.logo {
  text-align: center;
  margin-bottom: 30px;
}

.logo h1 {
  color: #667eea;
  font-size: 28px;
  margin-bottom: 10px;
}

.logo p {
  color: #718096;
  font-size: 14px;
}
</style>
