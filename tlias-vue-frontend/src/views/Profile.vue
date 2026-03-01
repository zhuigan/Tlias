<template>
  <div class="profile-container">
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card>
          <div class="profile-card">
            <div class="avatar-container">
              <el-avatar :size="120" :src="userInfo.image || defaultAvatar" />
              <el-upload
                class="avatar-upload"
                :show-file-list="false"
                :http-request="handleUpload"
                :before-upload="beforeUpload"
              >
                <el-button size="small" type="primary" circle>
                  <el-icon><Camera /></el-icon>
                </el-button>
              </el-upload>
            </div>
            <h2 style="margin-top: 20px">{{ userInfo.name }}</h2>
            <p style="color: #909399; margin-top: 5px">@{{ userInfo.username }}</p>
            
            <div class="info-list">
              <div class="info-item">
                <span class="label">性别</span>
                <el-tag :type="userInfo.gender === 1 ? 'primary' : 'danger'" size="small">
                  {{ userInfo.gender === 1 ? '男' : '女' }}
                </el-tag>
              </div>
              <div class="info-item">
                <span class="label">职位</span>
                <span class="value">{{ jobMap[userInfo.job] || '-' }}</span>
              </div>
              <div class="info-item">
                <span class="label">部门</span>
                <span class="value">{{ deptName }}</span>
              </div>
              <div class="info-item">
                <span class="label">入职日期</span>
                <span class="value">{{ userInfo.entrydate || '-' }}</span>
              </div>
              <div class="info-item">
                <span class="label">角色</span>
                <el-tag :type="userInfo.role === 1 ? 'warning' : 'success'" size="small">
                  {{ userInfo.role === 1 ? '管理员' : '普通用户' }}
                </el-tag>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="16">
        <el-card>
          <template #header>
            <span>同部门同事</span>
          </template>
          <div class="colleagues-grid" v-loading="loading">
            <div
              v-for="colleague in colleagues"
              :key="colleague.id"
              class="colleague-item"
            >
              <el-avatar :size="60" :src="colleague.image || defaultAvatar" />
              <div class="colleague-info">
                <div class="colleague-name">{{ colleague.name }}</div>
                <div class="colleague-job">{{ jobMap[colleague.job] || '未分配' }}</div>
              </div>
            </div>
            <el-empty v-if="colleagues.length === 0" description="暂无同部门同事" />
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { getCurrentUser, getEmpList, updateAvatar, uploadFile } from '@/api/emp'
import { getDeptList } from '@/api/dept'

const loading = ref(false)
const userInfo = ref({})
const colleagues = ref([])
const deptList = ref([])
const defaultAvatar = 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'

const jobMap = {
  1: '班主任',
  2: '讲师',
  3: '学工主管',
  4: '教研主管',
  5: '咨询师'
}

const deptName = computed(() => {
  const dept = deptList.value.find(d => d.id === userInfo.value.deptId)
  return dept ? dept.name : '-'
})

const loadUserInfo = async () => {
  try {
    const res = await getCurrentUser()
    userInfo.value = res.data
    loadColleagues()
  } catch (error) {
    console.error('加载用户信息失败', error)
  }
}

const loadDeptList = async () => {
  try {
    const res = await getDeptList()
    deptList.value = res.data
  } catch (error) {
    console.error('加载部门列表失败', error)
  }
}

const loadColleagues = async () => {
  loading.value = true
  try {
    const res = await getEmpList({ page: 1, pageSize: 100 })
    colleagues.value = res.data.rows.filter(emp => 
      emp.id !== userInfo.value.id && emp.deptId === userInfo.value.deptId
    )
  } catch (error) {
    console.error('加载同事列表失败', error)
  } finally {
    loading.value = false
  }
}

const beforeUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt5M = file.size / 1024 / 1024 < 5

  if (!isImage) {
    ElMessage.error('只能上传图片文件')
    return false
  }
  if (!isLt5M) {
    ElMessage.error('图片大小不能超过 5MB')
    return false
  }
  return true
}

const handleUpload = async ({ file }) => {
  try {
    ElMessage.info('正在上传...')
    const uploadRes = await uploadFile(file)
    const imageUrl = uploadRes.data
    
    await updateAvatar({
      id: userInfo.value.id,
      image: imageUrl
    })
    
    userInfo.value.image = imageUrl
    ElMessage.success('头像更新成功')
  } catch (error) {
    ElMessage.error('上传失败')
  }
}

onMounted(() => {
  loadUserInfo()
  loadDeptList()
})
</script>

<style scoped>
.profile-container {
  height: 100%;
}

.profile-card {
  text-align: center;
  padding: 20px 0;
}

.avatar-container {
  position: relative;
  display: inline-block;
}

.avatar-upload {
  position: absolute;
  bottom: 0;
  right: 0;
}

.info-list {
  margin-top: 30px;
  text-align: left;
}

.info-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 0;
  border-bottom: 1px solid #f0f0f0;
}

.info-item:last-child {
  border-bottom: none;
}

.info-item .label {
  color: #909399;
  font-weight: 500;
}

.info-item .value {
  color: #303133;
  font-weight: 600;
}

.colleagues-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 20px;
  min-height: 200px;
}

.colleague-item {
  display: flex;
  align-items: center;
  gap: 15px;
  padding: 15px;
  background: #f7f8fa;
  border-radius: 8px;
  transition: all 0.3s;
}

.colleague-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.colleague-info {
  flex: 1;
}

.colleague-name {
  font-weight: 600;
  color: #303133;
  margin-bottom: 5px;
}

.colleague-job {
  font-size: 13px;
  color: #909399;
}
</style>
