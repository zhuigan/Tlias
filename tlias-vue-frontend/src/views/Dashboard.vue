<template>
  <div class="dashboard">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background: #409eff">
              <el-icon :size="30"><User /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.empCount }}</div>
              <div class="stat-label">员工总数</div>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background: #67c23a">
              <el-icon :size="30"><OfficeBuilding /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.deptCount }}</div>
              <div class="stat-label">部门总数</div>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background: #e6a23c">
              <el-icon :size="30"><UserFilled /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.maleCount }}</div>
              <div class="stat-label">男性员工</div>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background: #f56c6c">
              <el-icon :size="30"><UserFilled /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.femaleCount }}</div>
              <div class="stat-label">女性员工</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="24">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>欢迎使用 Tlias 员工管理系统</span>
            </div>
          </template>
          <div class="welcome-content">
            <h2>👋 你好，{{ userStore.userInfo?.name }}！</h2>
            <p style="margin-top: 20px; color: #606266; line-height: 1.8">
              欢迎来到 Tlias 员工管理系统。这是一个基于 Vue 3 + Spring Boot 的前后端分离项目。
            </p>
            <div style="margin-top: 30px">
              <el-tag type="info" style="margin-right: 10px">
                角色：{{ userStore.isAdmin ? '管理员' : '普通用户' }}
              </el-tag>
              <el-tag type="success">
                用户名：{{ userStore.userInfo?.username }}
              </el-tag>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import { getEmpList } from '@/api/emp'
import { getDeptList } from '@/api/dept'

const userStore = useUserStore()

const stats = ref({
  empCount: 0,
  deptCount: 0,
  maleCount: 0,
  femaleCount: 0
})

const loadStats = async () => {
  try {
    // 加载员工统计
    const empRes = await getEmpList({ page: 1, pageSize: 1000 })
    const emps = empRes.data.rows || []
    stats.value.empCount = empRes.data.total || 0
    stats.value.maleCount = emps.filter(e => e.gender === 1).length
    stats.value.femaleCount = emps.filter(e => e.gender === 2).length

    // 加载部门统计
    const deptRes = await getDeptList()
    stats.value.deptCount = deptRes.data.length || 0
  } catch (error) {
    console.error('加载统计数据失败', error)
  }
}

onMounted(() => {
  loadStats()
})
</script>

<style scoped>
.dashboard {
  padding: 0;
}

.stat-card {
  cursor: pointer;
  transition: all 0.3s;
}

.stat-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.stat-content {
  display: flex;
  align-items: center;
  gap: 20px;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  margin-top: 5px;
}

.card-header {
  font-weight: 600;
  font-size: 16px;
}

.welcome-content {
  padding: 20px 0;
}

.welcome-content h2 {
  color: #303133;
  font-size: 24px;
}
</style>
