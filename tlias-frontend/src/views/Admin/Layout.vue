<template>
  <div class="admin-layout">
    <el-container>
      <el-header>
        <div class="header-content">
          <h2>Tlias 管理后台</h2>
          <div class="user-info">
            <span>管理员</span>
            <el-button type="danger" size="small" @click="handleLogout">退出</el-button>
          </div>
        </div>
      </el-header>
      <el-container>
        <el-aside width="200px">
          <el-menu
            :default-active="$route.path"
            router
            class="el-menu-vertical"
          >
            <el-menu-item index="/admin/dept">
              <el-icon><OfficeBuilding /></el-icon>
              <span>部门管理</span>
            </el-menu-item>
            <el-menu-item index="/admin/emp">
              <el-icon><User /></el-icon>
              <span>员工管理</span>
            </el-menu-item>
          </el-menu>
        </el-aside>
        <el-main>
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { OfficeBuilding, User } from '@element-plus/icons-vue'
import { removeToken } from '@/utils/auth'

const router = useRouter()

const handleLogout = () => {
  removeToken()
  ElMessage.success('退出成功')
  router.push('/login')
}
</script>

<style scoped>
.admin-layout {
  height: 100vh;
}

.el-container {
  height: 100%;
}

.el-header {
  background-color: #409eff;
  color: white;
  display: flex;
  align-items: center;
}

.header-content {
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-content h2 {
  margin: 0;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
}

.el-aside {
  background-color: #f5f5f5;
}

.el-main {
  background-color: #fff;
}
</style>
