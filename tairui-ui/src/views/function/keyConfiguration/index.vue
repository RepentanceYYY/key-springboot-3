<template>
  <div class="app-container">
    <!-- 搜索区域 -->
    <el-form
        v-show="showSearch"
        :model="queryParams"
        ref="queryFormRef"
        label-width="68px"
        inline
        size="small"
    >
      <el-form-item label="绑定柜子" prop="bindStatus">
        <el-select v-model="queryParams.bindStatus" placeholder="绑定柜子" clearable>
          <el-option
              v-for="dict in dict.type.function_bind_status"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="序列号" prop="keyNumber">
        <el-input
            v-model="queryParams.keyNumber"
            placeholder="请输入序列号"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>

      <el-form-item label="柜子名称" prop="cabinetName">
        <el-input
            v-model="queryParams.cabinetName"
            placeholder="请输入柜子名称"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>

      <el-form-item>
        <el-button type="primary" icon="Search" size="small" @click="handleQuery">
          搜索
        </el-button>
        <el-button icon="Refresh" size="small" @click="resetQuery">
          重置
        </el-button>
      </el-form-item>
    </el-form>

    <!-- 工具栏 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="3">
        <el-button
            type="primary"
            plain
            icon="Plus"
            size="small"
            @click="handleAdd"
            v-hasPermi="['function:keyConfiguration:add']"
        >
          新增
        </el-button>
      </el-col>

      <el-col :span="3">
        <el-button
            type="warning"
            plain
            icon="Download"
            size="small"
            @click="handleExport"
            v-hasPermi="['function:keyConfiguration:export']"
        >
          导出
        </el-button>
      </el-col>

      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"/>
    </el-row>

    <!-- 数据表格 -->
    <el-table
        v-loading="loading"
        :data="keyConfigurationList"
        @selection-change="handleSelectionChange"
        border
    >
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="绑定柜子" prop="bindStatus" align="center">
        <template #default="{ row }">
          <dict-tag :options="dict.type.function_bind_status" :value="row.bindStatus"/>
        </template>
      </el-table-column>

      <el-table-column label="序列号" prop="keyNumber" align="center"/>
      <el-table-column label="柜子名称" prop="cabinetName" align="center"/>

      <el-table-column label="柜子容量" prop="cabinetCapacity" align="center">
        <template #default="{ row }">
          {{ row.cabinetCapacity }} 位
        </template>
      </el-table-column>

      <el-table-column
          label="管理密码"
          prop="adminPassword"
          align="center"
          :formatter="formatPassword"
      />

      <el-table-column label="所绑定用户" prop="relatedUserNames" align="center"/>

      <el-table-column label="最近更新时间" prop="lastUpdateTime" align="center" width="180">
        <template #default="{ row }">
          {{ row.lastUpdateTime }}
        </template>
      </el-table-column>

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="{ row }">
          <el-button
              size="small"
              type="primary"
              link
              icon="View"
              @click="handleQueryId(row)"
              v-hasPermi="['function:keyConfiguration:query']"
              :disabled="row.bindStatus === 0"
          >
            详情
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <pagination
        v-show="total > 0"
        :total="total"
        v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize"
        @pagination="getList"
    />

    <!-- 弹窗 -->
    <el-dialog
        v-model="open"
        :title="title"
        width="500px"
        append-to-body
        :close-on-click-modal="false"
    >
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="序列号" prop="keyNumber">
          <el-input
              v-model="form.keyNumber"
              placeholder="请输入序列号"
              type="number"
              disabled
          />
        </el-form-item>

        <el-form-item label="柜子名称" prop="cabinetName">
          <el-input v-model="form.cabinetName" placeholder="请输入柜子名称"/>
        </el-form-item>

        <el-form-item label="柜子容量" prop="cabinetCapacity">
          <el-input-number v-model="form.cabinetCapacity" :min="1"/>
          <span style="margin-left: 8px">位</span>
        </el-form-item>

        <el-form-item label="管理密码" prop="adminPassword">
          <el-input
              v-model="form.adminPassword"
              placeholder="请输入管理密码"
              type="password"
              show-password
          />
        </el-form-item>
      </el-form>

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
import {ref, reactive, onMounted, getCurrentInstance} from "vue";
import {
  listKeyConfiguration,
  getKeyConfiguration,
  delKeyConfiguration,
  addKeyConfiguration,
  updateKeyConfiguration,
} from "@/api/function/keyConfiguration";

// 获取全局 proxy（相当于 Vue2 的 this）
const {proxy} = getCurrentInstance();

// 遮罩层
const loading = ref(true);
// 选中数组
const ids = ref([]);
// 非单个禁用
const single = ref(true);
// 非多个禁用
const multiple = ref(true);
// 显示搜索条件
const showSearch = ref(true);
// 总条数
const total = ref(0);
// 钥匙柜配置表格数据
const keyConfigurationList = ref([]);
// 弹出层标题
const title = ref("");
// 是否显示弹出层
const open = ref(false);

// 查询参数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  bindStatus: null,
  keyNumber: null,
  cabinetName: null,
});

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
  lastUpdateTime: null,
});

// 表单引用
const formRef = ref(null);
const queryFormRef = ref(null);

// 校验规则
const rules = {
  keyNumber: [
    {required: true, message: "请输入序列号", trigger: "blur"},
    {pattern: /^\d+$/, message: "序列号只能包含数字", trigger: "blur"},
    {min: 8, max: 16, message: "序列号长度在8-16位之间", trigger: "blur"},
  ],
  cabinetName: [{required: true, message: "柜子名称不能为空", trigger: "blur"}],
  cabinetCapacity: [{required: true, message: "柜子容量不能为空", trigger: "blur"}],
  adminPassword: [{required: true, message: "管理密码不能为空", trigger: "blur"}],
};

/** 查询钥匙柜配置列表 */
function getList() {
  loading.value = true;
  listKeyConfiguration(queryParams).then((response) => {
    keyConfigurationList.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
}

/** 格式化密码显示 */
function formatPassword(row) {
  return row.adminPassword ? "******" : "";
}

/** 取消按钮 */
function cancel() {
  open.value = false;
  reset();
}

/** 表单重置 */
function reset() {
  Object.assign(form, {
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
    lastUpdateTime: null,
  });
  proxy.resetForm("form");
}

/** 搜索按钮 */
function handleQuery() {
  queryParams.pageNum = 1;
  getList();
}

/** 重置搜索 */
function resetQuery() {
  proxy.resetForm("queryForm");
  handleQuery();
}

/** 多选框选中 */
function handleSelectionChange(selection) {
  ids.value = selection.map((item) => item.id);
  single.value = selection.length !== 1;
  multiple.value = !selection.length;
}

/** 新增按钮 */
function handleAdd() {
  reset();

  const timestamp = Date.now().toString().slice(-6);
  const randomLen = Math.floor(Math.random() * 9) + 2; // 2~10位随机数
  const minRandom = Math.pow(10, randomLen - 1);
  const maxRandom = Math.pow(10, randomLen) - 1;
  const randomNum = Math.floor(Math.random() * (maxRandom - minRandom + 1) + minRandom).toString();

  form.keyNumber = timestamp + randomNum;

  open.value = true;
  title.value = "添加钥匙柜配置";
}

/** 查看详情 */
function handleQueryId(row) {
  const id = row.id || ids.value;
  proxy.$router.push("/keyConfiguration/query/index/" + id);
}

/** 修改按钮 */
function handleUpdate(row) {
  reset();
  const id = row.id || ids.value;
  getKeyConfiguration(id).then((response) => {
    Object.assign(form, response.data);
    open.value = true;
    title.value = "修改钥匙柜配置";
  });
}

/** 提交按钮 */
function submitForm() {
  formRef.value.validate((valid) => {
    if (valid) {
      if (form.id != null) {
        updateKeyConfiguration(form).then(() => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addKeyConfiguration(form).then(() => {
          proxy.$modal.msgSuccess("新增成功");
          open.value = false;
          getList();
        });
      }
    }
  });
}

/** 删除按钮 */
function handleDelete(row) {
  const delIds = row.id || ids.value;
  proxy.$modal
      .confirm(`是否确认删除钥匙柜配置编号为"${delIds}"的数据项？`)
      .then(() => delKeyConfiguration(delIds))
      .then(() => {
        getList();
        proxy.$modal.msgSuccess("删除成功");
      })
      .catch(() => {
      });
}

/** 导出按钮 */
function handleExport() {
  proxy.download(
      "function/keyConfiguration/export",
      {...queryParams},
      `钥匙柜配置_${new Date().getTime()}.xlsx`
  );
}

// 页面加载时获取列表
onMounted(() => {
  getList();
});
</script>


<style scoped lang="scss">

</style>