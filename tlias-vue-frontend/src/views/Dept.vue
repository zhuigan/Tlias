<template>
  <div class="dept-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>部门列表</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            添加部门
          </el-button>
        </div>
      </template>

      <el-table :data="deptList" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="部门名称" />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column prop="updateTime" label="更新时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleEdit(row)">
              编辑
            </el-button>
            <el-button type="danger" size="small" @click="handleDelete(row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 添加/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="500px"
    >
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="部门名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入部门名称" />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">
          确定
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getDeptList, addDept, updateDept, deleteDept } from '@/api/dept'

const loading = ref(false)
const deptList = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('添加部门')
const formRef = ref(null)
const submitLoading = ref(false)

const form = reactive({
  id: null,
  name: ''
})

const rules = {
  name: [
    { required: true, message: '请输入部门名称', trigger: 'blur' }
  ]
}

const loadDeptList = async () => {
  loading.value = true
  try {
    const res = await getDeptList()
    deptList.value = res.data
  } catch (error) {
    console.error('加载部门列表失败', error)
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  dialogTitle.value = '添加部门'
  form.id = null
  form.name = ''
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑部门'
  form.id = row.id
  form.name = row.name
  dialogVisible.value = true
}

const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        if (form.id) {
          await updateDept(form)
          ElMessage.success('修改成功')
        } else {
          await addDept({ name: form.name })
          ElMessage.success('添加成功')
        }
        dialogVisible.value = false
        loadDeptList()
      } catch (error) {
        console.error('操作失败', error)
      } finally {
        submitLoading.value = false
      }
    }
  })
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除这个部门吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteDept(row.id)
      ElMessage.success('删除成功')
      loadDeptList()
    } catch (error) {
      console.error('删除失败', error)
    }
  })
}

onMounted(() => {
  loadDeptList()
})
</script>

<style scoped>
.dept-container {
  height: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
