<template>
  <div class="dept-manage">
    <div class="toolbar">
      <el-button type="primary" @click="handleAdd">新增部门</el-button>
    </div>

    <el-table :data="deptList" border style="width: 100%">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="部门名称" />
      <el-table-column prop="createTime" label="创建时间" />
      <el-table-column prop="updateTime" label="更新时间" />
      <el-table-column label="操作" width="180">
        <template #default="{ row }">
          <el-button size="small" @click="handleEdit(row)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="部门名称">
          <el-input v-model="form.name" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getDeptList, addDept, updateDept, deleteDept } from '@/api/dept'

const deptList = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('')
const form = ref({ name: '' })
const isEdit = ref(false)

const loadDeptList = async () => {
  try {
    const res = await getDeptList()
    if (res.code === 1) {
      deptList.value = res.data
    }
  } catch (error) {
    ElMessage.error('加载部门列表失败')
  }
}

const handleAdd = () => {
  dialogTitle.value = '新增部门'
  isEdit.value = false
  form.value = { name: '' }
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑部门'
  isEdit.value = true
  form.value = { ...row }
  dialogVisible.value = true
}

const handleSubmit = async () => {
  try {
    const res = isEdit.value 
      ? await updateDept(form.value)
      : await addDept(form.value)
    
    if (res.code === 1) {
      ElMessage.success(isEdit.value ? '更新成功' : '添加成功')
      dialogVisible.value = false
      loadDeptList()
    }
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该部门吗？', '提示', {
      type: 'warning'
    })
    const res = await deleteDept(row.id)
    if (res.code === 1) {
      ElMessage.success('删除成功')
      loadDeptList()
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

onMounted(() => {
  loadDeptList()
})
</script>

<style scoped>
.dept-manage {
  padding: 20px;
}

.toolbar {
  margin-bottom: 20px;
}
</style>
