<template>
  <div class="log-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>📝 操作日志</span>
          <div>
            <el-button type="primary" @click="loadLogs" :loading="loading">
              <el-icon><Refresh /></el-icon>
              刷新
            </el-button>
            <el-switch
              v-model="autoRefresh"
              active-text="自动刷新"
              @change="toggleAutoRefresh"
              style="margin-left: 10px"
            />
          </div>
        </div>
      </template>

      <!-- 统计信息 -->
      <el-row :gutter="20" style="margin-bottom: 20px">
        <el-col :span="6">
          <el-statistic title="总操作数" :value="stats.total">
            <template #prefix>
              <el-icon><Document /></el-icon>
            </template>
          </el-statistic>
        </el-col>
        <el-col :span="6">
          <el-statistic title="今日操作" :value="stats.today">
            <template #prefix>
              <el-icon><Calendar /></el-icon>
            </template>
          </el-statistic>
        </el-col>
        <el-col :span="6">
          <el-statistic title="成功操作" :value="stats.success">
            <template #prefix>
              <el-icon style="color: #67c23a"><SuccessFilled /></el-icon>
            </template>
          </el-statistic>
        </el-col>
        <el-col :span="6">
          <el-statistic title="失败操作" :value="stats.failed">
            <template #prefix>
              <el-icon style="color: #f56c6c"><CircleCloseFilled /></el-icon>
            </template>
          </el-statistic>
        </el-col>
      </el-row>

      <!-- 日志表格 -->
      <el-table :data="logs" v-loading="loading" stripe max-height="600">
        <el-table-column type="index" label="#" width="60" />
        
        <el-table-column label="操作时间" width="180">
          <template #default="{ row }">
            <el-icon><Clock /></el-icon>
            {{ formatTime(row.operateTime) }}
          </template>
        </el-table-column>

        <el-table-column prop="username" label="操作用户" width="120">
          <template #default="{ row }">
            <el-tag type="info" size="small">
              <el-icon><User /></el-icon>
              {{ row.username }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="operation" label="操作类型" width="120">
          <template #default="{ row }">
            <el-tag :type="getOperationType(row.operation)" size="small">
              {{ row.operation }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="ip" label="IP地址" width="140" />

        <el-table-column label="请求参数" min-width="200">
          <template #default="{ row }">
            <el-popover
              placement="top"
              :width="400"
              trigger="hover"
              v-if="row.params && row.params.length > 50"
            >
              <template #reference>
                <span class="params-text">{{ row.params.substring(0, 50) }}...</span>
              </template>
              <pre style="max-height: 300px; overflow-y: auto">{{ formatJson(row.params) }}</pre>
            </el-popover>
            <span v-else class="params-text">{{ row.params || '-' }}</span>
          </template>
        </el-table-column>

        <el-table-column label="操作结果" width="150">
          <template #default="{ row }">
            <el-tag :type="row.result === '成功' ? 'success' : 'danger'" size="small">
              <el-icon v-if="row.result === '成功'"><CircleCheckFilled /></el-icon>
              <el-icon v-else><CircleCloseFilled /></el-icon>
              {{ row.result }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted } from 'vue'
import { getOperationLogs } from '@/api/log'
import { ElMessage } from 'element-plus'

const loading = ref(false)
const logs = ref([])
const autoRefresh = ref(true)
let refreshTimer = null

const stats = reactive({
  total: 0,
  today: 0,
  success: 0,
  failed: 0
})

const loadLogs = async () => {
  loading.value = true
  try {
    const res = await getOperationLogs()
    logs.value = res.data
    
    // 计算统计信息
    stats.total = logs.value.length
    
    const today = new Date().toISOString().split('T')[0]
    stats.today = logs.value.filter(log => 
      log.operateTime && log.operateTime.startsWith(today)
    ).length
    
    stats.success = logs.value.filter(log => log.result === '成功').length
    stats.failed = logs.value.filter(log => log.result !== '成功').length
  } catch (error) {
    console.error('加载操作日志失败', error)
  } finally {
    loading.value = false
  }
}

const formatTime = (time) => {
  if (!time) return '-'
  return time.replace('T', ' ')
}

const formatJson = (str) => {
  try {
    return JSON.stringify(JSON.parse(str), null, 2)
  } catch {
    return str
  }
}

const getOperationType = (operation) => {
  const typeMap = {
    '用户登录': 'primary',
    '用户注册': 'success',
    '上传文件': 'warning',
    '更新头像': 'info',
    '添加员工': 'success',
    '修改员工信息': 'warning',
    '删除员工': 'danger'
  }
  return typeMap[operation] || 'info'
}

const toggleAutoRefresh = (value) => {
  if (value) {
    startAutoRefresh()
  } else {
    stopAutoRefresh()
  }
}

const startAutoRefresh = () => {
  stopAutoRefresh()
  refreshTimer = setInterval(() => {
    loadLogs()
  }, 5000) // 每5秒刷新一次
}

const stopAutoRefresh = () => {
  if (refreshTimer) {
    clearInterval(refreshTimer)
    refreshTimer = null
  }
}

onMounted(() => {
  loadLogs()
  if (autoRefresh.value) {
    startAutoRefresh()
  }
})

onUnmounted(() => {
  stopAutoRefresh()
})
</script>

<style scoped>
.log-container {
  height: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.params-text {
  font-family: 'Courier New', monospace;
  font-size: 12px;
  color: #606266;
}

:deep(.el-statistic__head) {
  font-size: 14px;
  color: #909399;
}

:deep(.el-statistic__content) {
  font-size: 24px;
  font-weight: 600;
}
</style>
