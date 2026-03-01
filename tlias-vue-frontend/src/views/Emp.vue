<template>
  <div class="emp-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>员工列表</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            添加员工
          </el-button>
        </div>
      </template>

      <!-- 搜索栏 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="姓名">
          <el-input v-model="searchForm.name" placeholder="请输入姓名" clearable />
        </el-form-item>
        <el-form-item label="性别">
          <el-select v-model="searchForm.gender" placeholder="请选择" clearable>
            <el-option label="男" :value="1" />
            <el-option label="女" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="入职日期">
          <el-date-picker
            v-model="searchForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>
            查询
          </el-button>
          <el-button @click="handleReset">
            <el-icon><Refresh /></el-icon>
            重置
          </el-button>
        </el-form-item>
      </el-form>

      <!-- 表格 -->
      <el-table :data="empList" v-loading="loading" stripe>
        <el-table-column label="头像" width="80">
          <template #default="{ row }">
            <el-avatar :size="40" :src="row.image || defaultAvatar" />
          </template>
        </el-table-column>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="name" label="姓名" width="100" />
        <el-table-column label="性别" width="80">
          <template #default="{ row }">
            <el-tag :type="row.gender === 1 ? 'primary' : 'danger'" size="small">
              {{ row.gender === 1 ? '男' : '女' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="职位" width="120">
          <template #default="{ row }">
            {{ jobMap[row.job] || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="entrydate" label="入职日期" width="120" />
        <el-table-column label="部门" width="120">
          <template #default="{ row }">
            {{ getDeptName(row.deptId) }}
          </template>
        </el-table-column>
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

      <!-- 分页 -->
      <el-pagination
        v-model:current-page="pagination.page"
        v-model:page-size="pagination.pageSize"
        :total="pagination.total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="loadEmpList"
        @current-change="loadEmpList"
        style="margin-top: 20px; justify-content: flex-end"
      />
    </el-card>

    <!-- 添加/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
    >
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="头像">
          <el-upload
            class="avatar-uploader"
            :show-file-list="false"
            :http-request="handleUpload"
            :before-upload="beforeUpload"
          >
            <img v-if="form.image" :src="form.image" class="avatar" />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item>

        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>

        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name" placeholder="请输入姓名" />
        </el-form-item>

        <el-form-item label="性别" prop="gender">
          <el-radio-group v-model="form.gender">
            <el-radio :label="1">男</el-radio>
            <el-radio :label="2">女</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="职位">
          <el-select v-model="form.job" placeholder="请选择职位" style="width: 100%">
            <el-option label="班主任" :value="1" />
            <el-option label="讲师" :value="2" />
            <el-option label="学工主管" :value="3" />
            <el-option label="教研主管" :value="4" />
            <el-option label="咨询师" :value="5" />
          </el-select>
        </el-form-item>

        <el-form-item label="入职日期">
          <el-date-picker
            v-model="form.entrydate"
            type="date"
            placeholder="选择日期"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
            style="width: 100%"
          />
        </el-form-item>

        <el-form-item label="部门">
          <el-select v-model="form.deptId" placeholder="请选择部门" style="width: 100%">
            <el-option
              v-for="dept in deptList"
              :key="dept.id"
              :label="dept.name"
              :value="dept.id"
            />
          </el-select>
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
import { getEmpList, addEmp, updateEmp, deleteEmp, uploadFile } from '@/api/emp'
import { getDeptList } from '@/api/dept'

const loading = ref(false)
const empList = ref([])
const deptList = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('添加员工')
const formRef = ref(null)
const submitLoading = ref(false)
const defaultAvatar = 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'

const jobMap = {
  1: '班主任',
  2: '讲师',
  3: '学工主管',
  4: '教研主管',
  5: '咨询师'
}

const searchForm = reactive({
  name: '',
  gender: null,
  dateRange: []
})

const pagination = reactive({
  page: 1,
  pageSize: 10,
  total: 0
})

const form = reactive({
  id: null,
  username: '',
  name: '',
  gender: null,
  job: null,
  entrydate: '',
  deptId: null,
  image: ''
})

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' }
  ],
  gender: [
    { required: true, message: '请选择性别', trigger: 'change' }
  ]
}

const getDeptName = (deptId) => {
  const dept = deptList.value.find(d => d.id === deptId)
  return dept ? dept.name : '-'
}

const loadEmpList = async () => {
  loading.value = true
  try {
    const params = {
      page: pagination.page,
      pageSize: pagination.pageSize,
      name: searchForm.name || undefined,
      gender: searchForm.gender || undefined,
      begin: searchForm.dateRange?.[0] || undefined,
      end: searchForm.dateRange?.[1] || undefined
    }
    const res = await getEmpList(params)
    empList.value = res.data.rows
    pagination.total = res.data.total
  } catch (error) {
    console.error('加载员工列表失败', error)
  } finally {
    loading.value = false
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

const handleSearch = () => {
  pagination.page = 1
  loadEmpList()
}

const handleReset = () => {
  searchForm.name = ''
  searchForm.gender = null
  searchForm.dateRange = []
  pagination.page = 1
  loadEmpList()
}

const handleAdd = () => {
  dialogTitle.value = '添加员工'
  Object.assign(form, {
    id: null,
    username: '',
    name: '',
    gender: null,
    job: null,
    entrydate: '',
    deptId: null,
    image: ''
  })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑员工'
  Object.assign(form, {
    id: row.id,
    username: row.username,
    name: row.name,
    gender: row.gender,
    job: row.job,
    entrydate: row.entrydate,
    deptId: row.deptId,
    image: row.image
  })
  dialogVisible.value = true
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
    const res = await uploadFile(file)
    form.image = res.data
    ElMessage.success('上传成功')
  } catch (error) {
    ElMessage.error('上传失败')
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        if (form.id) {
          await updateEmp(form)
          ElMessage.success('修改成功')
        } else {
          await addEmp(form)
          ElMessage.success('添加成功')
        }
        dialogVisible.value = false
        loadEmpList()
      } catch (error) {
        console.error('操作失败', error)
      } finally {
        submitLoading.value = false
      }
    }
  })
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除这个员工吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteEmp([row.id])
      ElMessage.success('删除成功')
      loadEmpList()
    } catch (error) {
      console.error('删除失败', error)
    }
  })
}

onMounted(() => {
  loadEmpList()
  loadDeptList()
})
</script>

<style scoped>
.emp-container {
  height: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-form {
  margin-bottom: 20px;
}

.avatar-uploader {
  width: 100px;
  height: 100px;
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  overflow: hidden;
  transition: all 0.3s;
}

.avatar-uploader:hover {
  border-color: #409eff;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 100px;
  height: 100px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.avatar {
  width: 100px;
  height: 100px;
  display: block;
  object-fit: cover;
}
</style>
