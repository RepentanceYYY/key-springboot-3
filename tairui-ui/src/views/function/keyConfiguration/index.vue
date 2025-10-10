<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">

      <el-form-item label="绑定柜子" prop="bindStatus">
        <el-select v-model="queryParams.bindStatus" placeholder="绑定柜子" style="width: 200px;" clearable>
          <el-option v-for="item in function_bind_status" :key="item.value" :label="item.label"
                     :value="item.value"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="序列号" prop="keyNumber">
        <el-input v-model="queryParams.keyNumber" placeholder="请输入序列号" clearable @keyup.enter="handleQuery"/>
      </el-form-item>

      <el-form-item label="柜子名称" prop="cabinetName">
        <el-input v-model="queryParams.cabinetName" placeholder="请输入柜子名称" clearable @keyup.enter="handleQuery"/>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['function:keyConfiguration:add']">新增
        </el-button>
      </el-col>

      <el-col :span="1.5">
        <el-button type="warning" plain icon="Download" @click="handleExport"
                   v-hasPermi="['function:keyConfiguration:export']">导出
        </el-button>
      </el-col>

      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="keyConfigurationList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>

      <el-table-column label="绑定柜子" align="center" prop="bindStatus">
        <template v-slot="scope">
          <dict-tag :options="function_bind_status" :value="scope.row.bindStatus"/>
        </template>
      </el-table-column>

      <el-table-column label="序列号" align="center" prop="keyNumber"/>
      <el-table-column label="柜子名称" align="center" prop="cabinetName"/>

      <el-table-column label="柜子容量" align="center" prop="cabinetCapacity">
        <template v-slot="scope">
          <span>{{ scope.row.cabinetCapacity }}位</span>
        </template>
      </el-table-column>

      <el-table-column label="管理密码" align="center" prop="adminPassword" :formatter="formatPassword"/>

      <el-table-column label="所绑定用户" width="240" show-overflow-tooltip align="center" prop="relatedUserNames"/>

      <el-table-column label="最近更新时间" align="center" prop="lastUpdateTime" width="180">
        <template v-slot="scope">
          <span>{{ scope.row.lastUpdateTime }}</span>
        </template>
      </el-table-column>

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template v-slot="scope">
          <el-button type="text" icon="el-icon-query" @click="handleQueryId(scope.row)"
                     v-hasPermi="['function:keyConfiguration:query']" :disabled="scope.row.bindStatus === 0">详情
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
                v-model:limit="queryParams.pageSize" @pagination="getList"/>

    <!-- 添加或修改钥匙柜配置对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body :close-on-click-modal="false">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="序列号" prop="keyNumber">
          <el-input v-model="form.keyNumber" placeholder="请输入序列号" type="number"
                    @input="(val) => form.keyNumber = val.replace(/[^\d]/g, '')" disabled/>
        </el-form-item>

        <el-form-item label="柜子名称" prop="cabinetName">
          <el-input v-model="form.cabinetName" placeholder="请输入柜子名称"/>
        </el-form-item>

        <el-form-item label="柜子容量" prop="cabinetCapacity">
          <el-input-number v-model="form.cabinetCapacity" :min="1" placeholder="请输入柜子容量" style="width: 80%;"/>
          <span style="margin-left: 8px;">位</span>
        </el-form-item>

        <el-form-item label="管理密码" prop="adminPassword">
          <el-input v-model="form.adminPassword" type="password" show-password placeholder="请输入管理密码"/>
        </el-form-item>
      </el-form>

      <!-- footer 区域 -->
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>


</template>

<script setup>
import {ref, reactive, onMounted} from 'vue'
import {useRouter} from 'vue-router'
import {ElMessage} from 'element-plus'
import {
  listKeyConfiguration,
  getKeyConfiguration,
  delKeyConfiguration,
  addKeyConfiguration,
  updateKeyConfiguration,
  getNewKeyNumber
} from "@/api/function/keyConfiguration"
import {getCurrentInstance} from 'vue'

// 路由
const router = useRouter()
const {proxy} = getCurrentInstance();
const {function_bind_status} = proxy.useDict("function_bind_status");

// 状态
const loading = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const showSearch = ref(true)
const total = ref(0)
const keyConfigurationList = ref([])
const title = ref('')
const open = ref(false)


// 查询参数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  bindStatus: "0",
  keyNumber: null,
  cabinetName: null,
})

// 表单参数
const form = reactive({
  id: null,
  keyNumber: null,
  cabinetName: null,
  cabinetCapacity: null,
  adminPassword: null,
  scheduledReboot: null,
  rebootTime: null,
  networkMode: null,
  keyRequest: null,
  borrowReview: null,
  returnCycle: null,
  returnTime: null,
  timeoutReminder: null,
  returnToHome: null,
  voiceAnnouncement: null,
  lastUpdateTime: null
})

// 表单实例
const formRef = ref(null)
const queryFormRef = ref(null)

// 表单校验规则
const rules = reactive({
  keyNumber: [
    {required: true, message: "请输入序列号", trigger: "blur"},
    {pattern: /^\d+$/, message: "序列号只能包含数字", trigger: "blur"},
    {min: 8, max: 16, message: "序列号长度在8-16位之间", trigger: "blur"}
  ],
  cabinetName: [
    {required: true, message: "柜子名称不能为空", trigger: "blur"}
  ],
  cabinetCapacity: [
    {required: true, message: "柜子容量不能为空", trigger: "blur"}
  ],
  adminPassword: [
    {required: true, message: "管理密码不能为空", trigger: "blur"}
  ],
})

// 获取列表
const getList = () => {
  loading.value = true
  listKeyConfiguration(queryParams).then(response => {
    keyConfigurationList.value = response.rows
    total.value = response.total
    loading.value = false
  })
}

// 格式化密码
const formatPassword = (row) => row.adminPassword ? '******' : ''

// 搜索按钮
const handleQuery = () => {
  queryParams.pageNum = 1
  getList()
}

// 重置查询
const resetQuery = () => {
  queryFormRef.value?.resetFields()
  handleQuery()
}

// 重置表单
const resetForm = (formTarget) => {
  if (formTarget === 'form') formRef.value?.resetFields()
  if (formTarget === 'queryForm') queryFormRef.value?.resetFields()
}

// 取消弹窗
const cancel = () => {
  open.value = false
  resetForm('form')
}

// 多选框选中
const handleSelectionChange = (selection) => {
  ids.value = selection.map(item => item.id)
  single.value = selection.length !== 1
  multiple.value = selection.length === 0
}

// 新增
const handleAdd = async () => {
  resetForm('form')
  const res = await getNewKeyNumber();

  form.keyNumber = res.data;

  open.value = true
  title.value = "添加钥匙柜配置"
}

// 查询详情
const handleQueryId = (row) => {
  const id = row.id || ids.value
  router.push("/keyConfiguration/query/index/" + id)
}

// 修改
const handleUpdate = (row) => {
  resetForm('form')
  const id = row.id || ids.value
  getKeyConfiguration(id).then(response => {
    Object.assign(form, response.data)
    open.value = true
    title.value = "修改钥匙柜配置"
  })
}

// 提交表单
const submitForm = () => {
  formRef.value?.validate((valid) => {
    if (valid) {
      if (form.id != null) {
        updateKeyConfiguration(form).then(() => {
          ElMessage.success("修改成功")
          open.value = false
          getList()
        })
      } else {
        addKeyConfiguration(form).then(() => {
          ElMessage.success("新增成功")
          open.value = false
          getList()
        })
      }
    }
  })
}

// 删除
const handleDelete = (row) => {
  const delIds = row.id || ids.value
  ElMessageBox.confirm(`是否确认删除钥匙柜配置编号为"${delIds}"的数据项？`).then(() => {
    return delKeyConfiguration(delIds)
  }).then(() => {
    getList()
    ElMessage.success("删除成功")
  }).catch(() => {
  })
}

// 导出
const handleExport = () => {
  download('function/keyConfiguration/export', {...queryParams}, `钥匙柜配置_${Date.now()}.xlsx`)
}

// 页面挂载后获取列表
onMounted(() => {
  getList()
})
</script>


<style scoped lang="scss"></style>