<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryFormRef" :inline="true" v-show="showSearch" label-width="85px">
      <el-form-item label="所属柜子" prop="srttingsName">
        <el-input
          v-model="queryParams.srttingsName"
          placeholder="请输入所属柜子"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="钥匙名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入钥匙名称"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="钥匙编号" prop="code">
        <el-input
          v-model="queryParams.code"
          placeholder="请输入钥匙编号"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态 " prop="status">
        <el-select v-model="queryParams.status" style="width: 200px;" placeholder="请选择状态 " clearable>
          <el-option
            v-for="dict in function_key_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="借用人" prop="lastControlUser">
        <el-input
          v-model="queryParams.lastControlUser"
          placeholder="请输入钥匙编号"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="所绑定用户" prop="userNames">
        <el-input
          v-model="queryParams.userNames"
          placeholder="请输入所绑定用户"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['function:key:export']"
        >导出</el-button>
      </el-col>

      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="Link"
          v-hasPermi="['function:key:bind']"
          @click="handleBatchOperation('bind')"
          :disabled="!keyList.length"
        >
          一键绑定所有钥匙
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Unlink"
          v-hasPermi="['function:key:unBind']"
          @click="handleBatchOperation('unbind')"
          :disabled="!keyList.length"
        >
          一键解绑所有钥匙
        </el-button>
      </el-col>
      <right-toolbar :showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="keyList" @selection-change="handleSelectionChange">
      <el-table-column label="所属柜子" align="center" prop="srttingsName" />
      <el-table-column label="所属位置" align="center" prop="position">
        <template #default="scope">
          {{ scope.row.position }} 位
        </template>
      </el-table-column>
      <el-table-column label="钥匙名称" align="center" prop="name">
        <template #default="scope">
          <span v-if="scope.row.name !== '未命名钥匙'">{{ scope.row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column label="钥匙编号" align="center" prop="code">
        <template #default="scope">
          <span v-if="scope.row.code !== '未命名编号'">{{ scope.row.code }}</span>
        </template>
      </el-table-column>
      <el-table-column label="所绑定用户" width="240" show-overflow-tooltip align="center" prop="userNames">
        <template #default="scope">
          {{ scope.row.userNames }}
        </template>
      </el-table-column>
      <el-table-column label="使用位置" align="center" prop="usageLocation">
        <template #default="scope">
          <span v-if="scope.row.usageLocation">{{ scope.row.usageLocation }}</span>
          <span v-else style="color:red">未填写</span>
        </template>
      </el-table-column>
      <el-table-column label="绑定标签" align="center" prop="tag" />
      <el-table-column label="实时标签" align="center" prop="currentTag" />
      <el-table-column label="状态 " align="center" prop="status">
        <template #default="scope">
          <dict-tag :options="function_key_status" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="最后借用人" align="center" prop="lastControlUser">
        <template #default="scope">
          <span v-if="scope.row.status === 'borrowed'">{{ scope.row.lastControlUser }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button
            type="text"
            icon="Edit"
            @click="handleUpdate(scope.row)"
            :disabled="!['unbound', 'available'].includes(scope.row.status)"
            v-hasPermi="['function:key:edit']"
          >绑定钥匙</el-button>

          <el-button
            type="text"
            icon="Edit"
            @click="handleInitKey(scope.row)"
            :disabled="!['available'].includes(scope.row.status)"
            v-hasPermi="['function:key:initKey']"
          >钥匙初始化</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改钥匙对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body :close-on-click-modal="false">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="钥匙名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入钥匙名称" />
        </el-form-item>
        <el-form-item label="钥匙编号" prop="code">
          <el-input v-model="form.code" placeholder="请输入钥匙编号" />
        </el-form-item>
        <el-form-item label="绑定用户" prop="userList">
          <el-select v-model="form.userList" multiple placeholder="请选择用户">
            <el-option
              v-for="item in userList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 批量操作对话框 -->
    <el-dialog
      :title="batchDialogTitle"
      v-model="batchDialogVisible"
      width="400px"
      append-to-body
      :close-on-click-modal="false"
      @close="handleDialogClose"
    >
      <el-form
        ref="batchFormRef"
        :model="batchForm"
        :rules="batchFormRules"
        label-width="100px"
      >
        <el-form-item label="钥匙柜选择" prop="srttingIds">
          <el-select
            v-model="batchForm.srttingIds"
            multiple
            placeholder="请选择钥匙柜"
            clearable
            style="width: 100%"
          >
            <el-option
              v-for="item in keyOptions"
              :key="item.id"
              :label="item.cabinetName"
              :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="batchDialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="confirmBatchOperation">确 定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { listKey, getKey, delKey, addKey, updateKey, initKeyApi, batchBindKeys, batchUnBindKeys } from "@/api/function/key"
import { getCurrentInstance } from 'vue'
import { getUsersByKeyPosition } from "@/api/function/user"
import { listKeyConfiguration2 } from "@/api/function/keyConfiguration"

const { proxy } = getCurrentInstance();
const { function_key_status } = proxy.useDict("function_key_status");

// 响应式数据
const loading = ref(true)
const showSearch = ref(true)
const userList = ref([])
const total = ref(0)
const keyList = ref([])
const title = ref("")
const open = ref(false)
const batchDialogVisible = ref(false)
const batchDialogTitle = ref('')
const batchOperationType = ref('')
const keyOptions = ref([])
const ids = ref([])
const single = ref(true)
const multiple = ref(true)

const queryFormRef = ref()
const formRef = ref()
const batchFormRef = ref()

const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  name: null,
  code: null,
  status: null,
  srttingsName: null,
  userNames: null,
})

const form = reactive({
  id: null,
  name: null,
  code: null,
  position: null,
  tag: null,
  currentTag: null,
  status: null,
  controlPanelAddress: null,
  circuitPanelAddress: null,
  createAt: null,
  lastControlUserId: null,
  srttings: null,
  userList: [],
})

const batchForm = reactive({
  srttingIds: []
})

// 表单校验规则
const batchFormRules = reactive({
  srttingIds: [
    { required: true, message: "请选择钥匙柜", trigger: "blur" }
  ]
})

const rules = reactive({
  userList: [
    { required: true, message: "绑定用户不能为空", trigger: "blur" },
  ],
  name: [
    { required: true, message: "钥匙名称不能为空", trigger: "blur" },
    {
      validator: (rule, value, callback) => {
        if (value?.trim() === "未命名钥匙") {
          callback(new Error("钥匙名称不能为'未命名钥匙'"))
        } else {
          callback()
        }
      },
      trigger: "blur"
    }
  ],
  code: [
    { required: true, message: "钥匙编号不能为空", trigger: "blur" },
    {
      validator: (rule, value, callback) => {
        if (value?.trim() === "未命名编号") {
          callback(new Error("钥匙编号不能为'未命名编号'"))
        } else {
          callback()
        }
      },
      trigger: "blur"
    }
  ],
  status: [
    { required: true, message: "状态 不能为空", trigger: "change" }
  ],
})

// 方法
/** 查询钥匙列表 */
const getList = () => {
  listKeyConfiguration2(queryParams).then(response => {
    keyOptions.value = response.rows
  })
  loading.value = true
  listKey(queryParams).then(response => {
    keyList.value = response.rows
    total.value = response.total
    loading.value = false
  })
}

// 统一处理批量操作入口
const handleBatchOperation = (type) => {
  batchOperationType.value = type
  batchDialogTitle.value = type === 'bind'
    ? "选择钥匙柜进行批量绑定"
    : "选择钥匙柜进行批量解绑"

  // 重置表单
  batchForm.srttingIds = []
  nextTick(() => {
    batchFormRef.value?.resetFields()
  })

  // 加载钥匙柜选项并显示对话框
  loadKeyOptions().then(() => {
    batchDialogVisible.value = true
  })
}

// 确认批量操作
const confirmBatchOperation = () => {
  batchFormRef.value.validate(valid => {
    if (valid) {
      // 绑定操作直接执行，解绑操作增加二次确认
      const confirmMessage = batchOperationType.value === 'unbind'
        ? `确定要从所选 ${batchForm.srttingIds.length} 个钥匙柜中解绑所有钥匙吗？`
        : `确定要从所选 ${batchForm.srttingIds.length} 个钥匙柜中绑定所有钥匙吗？`
      
      ElMessageBox.confirm(confirmMessage).then(() => {
        executeBatchOperation()
      })
    }
  })
}

// 执行批量操作
const executeBatchOperation = () => {
  const srttings = batchForm.srttingIds.join(',')
  const api = batchOperationType.value === 'bind'
    ? batchBindKeys
    : batchUnBindKeys
  const successMsg = batchOperationType.value === 'bind'
    ? "批量绑定成功"
    : "批量解绑成功"
  const errorMsg = batchOperationType.value === 'bind'
    ? "批量绑定失败"
    : "批量解绑失败"

  loading.value = true
  api(srttings)
    .then(() => {
      ElMessage.success(successMsg)
      batchDialogVisible.value = false
      getList() // 刷新列表
    })
    .catch(error => {
      ElMessage.error(`${errorMsg}：${error.message || '未知错误'}`)
      console.error(`${errorMsg}:`, error)
    })
    .finally(() => {
      loading.value = false
    })
}

// 加载钥匙柜选项
const loadKeyOptions = () => {
  if (keyOptions.value.length > 0) {
    return Promise.resolve()
  }
  return listKeyConfiguration2(queryParams)
    .then(response => {
      keyOptions.value = response.rows || []
      return Promise.resolve()
    })
    .catch(error => {
      ElMessage.error("加载钥匙柜列表失败，请重试")
      console.error("加载钥匙柜失败:", error)
      return Promise.reject(error)
    })
}

// 对话框关闭时重置
const handleDialogClose = () => {
  batchForm.srttingIds = []
  nextTick(() => {
    batchFormRef.value?.resetFields()
  })
}

// 取消按钮
const cancel = () => {
  open.value = false
  reset()
}

// 表单重置
const reset = () => {
  Object.assign(form, {
    id: null,
    name: null,
    code: null,
    position: null,
    tag: null,
    currentTag: null,
    status: null,
    controlPanelAddress: null,
    circuitPanelAddress: null,
    createAt: null,
    lastControlUserId: null,
    srttings: null,
    userList: [],
  })
  nextTick(() => {
    formRef.value?.resetFields()
  })
}

/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.pageNum = 1
  getList()
}

/** 重置按钮操作 */
const resetQuery = () => {
  queryFormRef.value.resetFields()
  handleQuery()
}

// 多选框选中数据
const handleSelectionChange = (selection) => {
  ids.value = selection.map(item => item.id)
  single.value = selection.length !== 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
const handleAdd = () => {
  reset()
  open.value = true
  title.value = "添加钥匙"
}

/** 初始化按钮操作 */
const handleInitKey = (row) => {
  ElMessageBox.confirm(`确定要初始化钥匙【${row.name || row.code}】吗？初始化后将重置钥匙状态和绑定信息`).then(() => {
    initKeyApi(row.id).then(response => {
      ElMessage.success("初始化成功")
      getList()
    }).catch(err => {
      ElMessage.error("初始化失败")
    })
  })
}

/** 修改按钮操作 */
const handleUpdate = (row) => {
  const id = row.id || ids.value
  queryParams.id = id
  getUsersByKeyPosition(queryParams).then(response => {
    userList.value = response.rows
  })
  reset()

  getKey(id).then(response => {
    Object.assign(form, response.data)
    form.userList = response.data.userId ? response.data.userId.split(',').map(Number) : []
    open.value = true
    title.value = "所属柜子：" + response.data.srttingsName
  })
}

/** 提交按钮 */
const submitForm = () => {
  formRef.value.validate(valid => {
    if (valid) {
      if (form.id != null) {
        form.userId = form.userList.join(',')
        updateKey(form).then(response => {
          ElMessage.success("修改成功")
          open.value = false
          getList()
        })
      } else {
        addKey(form).then(response => {
          ElMessage.success("新增成功")
          open.value = false
          getList()
        })
      }
    }
  })
}

/** 删除按钮操作 */
const handleDelete = (row) => {
  const ids = row.id || ids.value
  ElMessageBox.confirm('是否确认删除钥匙编号为"' + ids + '"的数据项？').then(function() {
    return delKey(ids)
  }).then(() => {
    getList()
    ElMessage.success("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
const handleExport = () => {
  // 这里需要根据你的下载方法进行调整
  // this.download('function/key/export', {
  //   ...queryParams
  // }, `钥匙信息_${new Date().getTime()}.xlsx`)
  console.log('导出功能需要根据实际下载方法实现')
}

// 生命周期
onMounted(() => {
  getList()
})
</script>