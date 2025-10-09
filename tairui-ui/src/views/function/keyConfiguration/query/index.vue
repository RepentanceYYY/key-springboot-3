<template>
  <div class="app-container p-4 md:p-6">
    <!-- 页面标题 -->
    <div class="mb-6">
      <h2 class="text-[clamp(1.5rem,3vw,1.8rem)] font-semibold text-gray-800">系统设置</h2>
      <el-button type="primary" :loading="saving" class="transition-all" @click="handleQueryId()"
        v-hasPermi="['function:auth:query']">
        高级设置
      </el-button>
      <p class="text-gray-500 mt-1">配置柜子的基本参数和运行选项</p>
    </div>

    <!-- 操作按钮区 -->
    <div class="flex justify-end mb-6 gap-3">
      <el-button type="default" @click="resetForm" :disabled="!hasChanged" class="transition-all">
        <el-icon class="mr-1">
          <RefreshRight />
        </el-icon> 重置
      </el-button>
      <el-button type="primary" @click="saveSettings" :loading="saving" :disabled="!hasChanged || saving"
        class="transition-all">
        <el-icon class="mr-1">
          <Check />
        </el-icon> 保存设置
      </el-button>
    </div>

    <el-row :gutter="20" class="card">
      <el-col :span="24">
        <!-- 主要设置卡片 -->
        <el-card shadow="hover" class="transition-all duration-300 border-0 mb-6">
          <template #header>
            <div class="flex items-center justify-between pb-2">
              <span class="text-lg font-medium flex items-center text-gray-800">
                <el-icon class="mr-2 text-primary">
                  <Tools />
                </el-icon> 基础设置
              </span>
            </div>
          </template>

          <!-- 设置表单区域 -->
          <div class="p-1 md:p-4 space-y-6">
            <!-- 第一行：序列号和名称 -->
            <el-row :gutter="20">
              <!-- 序列号 -->
              <el-col :span="24" :md="8" class="space-y-2">
                <label class="block text-sm font-medium text-gray-700">
                  序列号
                  <span class="text-red-500 ml-1">*</span>
                  <el-tooltip effect="dark" content="用于标识当前柜子的编号，将显示在控制台首页，仅支持数字，最长11位">
                    <el-icon class="text-gray-400 ml-2 cursor-help hover:text-primary transition-colors">
                      <InfoFilled />
                    </el-icon>
                  </el-tooltip>
                </label>
                <el-input v-model="settings.keyNumber" placeholder="请输入序列号" class="w-full transition-all"
                  @input="handleInput('keyNumber')" :class="{ 'error-input': errors.keyNumber }" disabled></el-input>
                <p v-if="errors.keyNumber" class="mt-1 text-sm text-red-500">{{ errors.keyNumber }}</p>
              </el-col>
              <!-- 柜子名称 -->
              <el-col :span="24" :md="8" class="space-y-2">
                <label class="block text-sm font-medium text-gray-700">
                  柜子名称
                  <span class="text-red-500 ml-1">*</span>
                  <el-tooltip effect="dark" content="用于标识当前柜子的名称，将显示在控制台首页">
                    <el-icon class="text-gray-400 ml-2 cursor-help hover:text-primary transition-colors">
                      <InfoFilled />
                    </el-icon>
                  </el-tooltip>
                </label>
                <el-input v-model="settings.cabinetNamePrefix" placeholder="请输入柜子名称" class="w-full transition-all"
                  @input="handleInput('cabinetNamePrefix')"
                  :class="{ 'error-input': errors.cabinetNamePrefix }"></el-input>
                <p v-if="errors.cabinetNamePrefix" class="mt-1 text-sm text-red-500">{{ errors.cabinetNamePrefix }}</p>
              </el-col>
              <el-col :span="24" :md="8" class="space-y-3">
                <label class="block text-sm font-medium text-gray-700">
                  地理位置
                  <span class="text-red-500 ml-1">*</span>
                  <el-tooltip effect="dark" content="钥匙柜的物理位置">
                    <el-icon class="text-gray-400 ml-2 cursor-help hover:text-primary transition-colors">
                      <InfoFilled />
                    </el-icon>
                  </el-tooltip>
                </label>
                <el-input v-model="settings.cabinetNameSuffix" placeholder="请输入柜子名称" class="w-full transition-all"
                  @input="handleInput('cabinetNameSuffix')"
                  :class="{ 'error-input': errors.cabinetNameSuffix }"></el-input>
                <p v-if="errors.cabinetNameSuffix" class="mt-1 text-sm text-red-500">{{ errors.cabinetNameSuffix }}</p>
              </el-col>
            </el-row>

            <!-- 第二行：管理密码和容量 -->
            <el-row :gutter="20" class="mt-6">
              <!-- 管理密码 -->
              <el-col :span="24" :md="12" class="space-y-2">
                <label class="block text-sm font-medium text-gray-700">
                  管理密码
                  <span class="text-red-500 ml-1">*</span>
                  <el-tooltip effect="dark" content="用于登录管理后台的密码，建议定期更换">
                    <el-icon class="text-gray-400 ml-2 cursor-help hover:text-primary transition-colors">
                      <InfoFilled />
                    </el-icon>
                  </el-tooltip>
                </label>
                <el-input v-model="settings.adminPassword" :type="showPassword ? 'text' : 'password'"
                  placeholder="请输入管理密码" class="w-full transition-all" @input="handleInput('adminPassword')"
                  :class="{ 'error-input': errors.adminPassword }" show-password>
                  <template #suffix>
                    <el-icon class="cursor-pointer text-gray-400 hover:text-primary transition-colors"
                      @click="showPassword = !showPassword">
                      <View v-if="showPassword" />
                      <Hide v-else />
                    </el-icon>
                  </template>
                </el-input>
                <p v-if="errors.adminPassword" class="mt-1 text-sm text-red-500">{{ errors.adminPassword }}</p>
              </el-col>
              <!-- 柜子容量 -->
              <el-col :span="24" :md="12" class="space-y-2">
                <label class="block text-sm font-medium text-gray-700">
                  柜子容量
                  <el-tooltip effect="dark" content="容量由硬件决定，不可修改">
                    <el-icon class="text-gray-400 ml-2 cursor-help hover:text-primary transition-colors">
                      <InfoFilled />
                    </el-icon>
                  </el-tooltip>
                </label>
                <div class="flex items-center p-2.5 border border-gray-300 rounded-md bg-gray-50 text-gray-700">
                  {{ settings.cabinetCapacity }} 位
                </div>
              </el-col>
              <p v-if="errors.cabinetCapacity" class="mt-1 text-sm text-red-500">{{ errors.cabinetCapacity }}</p>
            </el-row>

            <!-- 第三行：定时重启设置 -->
            <el-row :gutter="20" class="mt-6">
              <!-- 定时重启 -->
              <el-col :span="24" :md="12" class="space-y-1">
                <label class="block text-sm font-medium text-gray-700">
                  定时重启
                  <span class="text-red-500 ml-1">*</span>
                  <el-tooltip effect="dark" content="开启后将按设定时间自动重启设备，提高稳定性">
                    <el-icon class="text-gray-400 ml-2 cursor-help hover:text-primary transition-colors">
                      <InfoFilled />
                    </el-icon>
                  </el-tooltip>
                </label>
                <div class="flex items-center space-x-3">
                  <el-select v-model="settings.scheduledReboot" placeholder="请选择状态" clearable style="width: 240px"
                    @change="handleSelectChange('scheduledReboot')" :class="{ 'error-input': errors.scheduledReboot }">
                    <el-option v-for="dict in key_cabinet_status" :key="dict.value" :label="dict.label"
                      :value="dict.value" />
                  </el-select>
                  <p v-if="errors.scheduledReboot" class="mt-1 text-sm text-red-500">{{ errors.scheduledReboot }}</p>
                </div>
              </el-col>
              <!-- 重启时间 -->
              <el-col :span="24" :md="12" class="space-y-1" v-if="settings.scheduledReboot === 'enable'">
                <label class="block text-sm font-medium text-gray-700">
                  重启时间
                  <span class="text-red-500 ml-1">*</span>
                  <el-tooltip effect="dark" content="设备将在每天的该时间自动重启，请选择设备使用率较低的时段">
                    <el-icon class="text-gray-400 ml-2 cursor-help hover:text-primary transition-colors">
                      <InfoFilled />
                    </el-icon>
                  </el-tooltip>
                </label>
                <el-time-picker v-model="settings.rebootTime" format="HH:mm" value-format="HH:mm" placeholder="选择重启时间"
                  :disabled="settings.scheduledReboot !== 'enable'" class="no-prefix-icon"
                  @change="handleInput('rebootTime')"></el-time-picker>
                <p v-if="errors.rebootTime" class="mt-1 text-sm text-red-500">{{ errors.rebootTime }}</p>
              </el-col>
            </el-row>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="card">
      <el-col :span="24">
        <!-- 钥匙管理设置卡片 -->
        <el-card shadow="hover" class="transition-all duration-300 border-0 mb-6">
          <template #header>
            <div class="flex items-center justify-between pb-2">
              <span class="text-lg font-medium flex items-center text-gray-800">
                <el-icon class="mr-2 text-primary">
                  <Key />
                </el-icon> 钥匙管理设置
              </span>
            </div>
          </template>

          <div class="p-1 md:p-4 space-y-6">
            <el-row :gutter="20">
              <!-- 钥匙申请 -->
              <el-col :span="24" :md="12" class="space-y-2">
                <label class="block text-sm font-medium text-gray-700">
                  钥匙申请
                  <span class="text-red-500 ml-1">*</span>
                  <el-tooltip effect="dark" content="开启后用户钥匙借用需要申请">
                    <el-icon class="text-gray-400 ml-2 cursor-help hover:text-primary transition-colors">
                      <InfoFilled />
                    </el-icon>
                  </el-tooltip>
                </label>
                <div class="flex items-center space-x-3">
                  <el-select v-model="settings.keyRequest" placeholder="请选择状态" clearable style="width: 240px"
                    @change="handleSelectChange('keyRequest')" :class="{ 'error-input': errors.keyRequest }">
                    <el-option v-for="dict in key_cabinet_status" :key="dict.value" :label="dict.label"
                      :value="dict.value" />
                  </el-select>
                  <p v-if="errors.keyRequest" class="mt-1 text-sm text-red-500">{{ errors.keyRequest }}</p>
                </div>
              </el-col>
              <!-- 借复审 -->
              <el-col :span="24" :md="12" class="space-y-2" v-if="settings.keyRequest === 'enable'">
                <label class="block text-sm font-medium text-gray-700">
                  借用审核
                  <span class="text-red-500 ml-1">*</span>
                  <el-tooltip effect="dark" content="开启后钥匙借用需要管理员审核通过">
                    <el-icon class="text-gray-400 ml-2 cursor-help hover:text-primary transition-colors">
                      <InfoFilled />
                    </el-icon>
                  </el-tooltip>
                </label>
                <div class="flex items-center space-x-3">
                  <el-select v-model="settings.borrowReview" placeholder="请选择状态" clearable style="width: 240px"
                    @change="handleSelectChange('borrowReview')" :class="{ 'error-input': errors.borrowReview }">
                    <el-option v-for="dict in key_cabinet_status" :key="dict.value" :label="dict.label"
                      :value="dict.value" />
                  </el-select>
                  <p v-if="errors.borrowReview" class="mt-1 text-sm text-red-500">{{ errors.borrowReview }}</p>
                </div>
              </el-col>
            </el-row>

            <el-row :gutter="20" class="mt-6">
              <!-- 归还周期 -->
              <el-col :span="24" :md="12" class="space-y-2">
                <label class="block text-sm font-medium text-gray-700">
                  归还周期限制
                  <span class="text-red-500 ml-1">*</span>
                  <el-tooltip effect="dark" content="开启后限制钥匙最长借用时间">
                    <el-icon class="text-gray-400 ml-2 cursor-help hover:text-primary transition-colors">
                      <InfoFilled />
                    </el-icon>
                  </el-tooltip>
                </label>
                <div class="flex items-center space-x-3">
                  <el-select v-model="settings.returnCycle" placeholder="请选择状态" clearable style="width: 240px"
                    @change="handleSelectChange('returnCycle')" :class="{ 'error-input': errors.returnCycle }">
                    <el-option v-for="dict in key_cabinet_status" :key="dict.value" :label="dict.label"
                      :value="dict.value" />
                  </el-select>
                  <p v-if="errors.returnCycle" class="mt-1 text-sm text-red-500">{{ errors.returnCycle }}</p>
                </div>
              </el-col>
              <!-- 归还时间 -->
              <el-col :span="24" :md="12" class="space-y-2" v-if="settings.returnCycle === 'enable'">
                <label class="block text-sm font-medium text-gray-700">
                  最长借用时间
                  <span class="text-red-500 ml-1">*</span>
                  <el-tooltip effect="dark" content="设置钥匙最长可借用的天数">
                    <el-icon class="text-gray-400 ml-2 cursor-help hover:text-primary transition-colors">
                      <InfoFilled />
                    </el-icon>
                  </el-tooltip>
                </label>
                <el-select v-model="settings.returnTime" placeholder="选择天数" class="w-full"
                  @change="handleSelectChange('returnTime')" :class="{ 'error-input': errors.returnTime }">
                  <el-option value="1" label="1天"></el-option>
                  <el-option value="2" label="2天"></el-option>
                  <el-option value="3" label="3天"></el-option>
                  <el-option value="4" label="4天"></el-option>
                  <el-option value="5" label="5天"></el-option>
                </el-select>
                <p v-if="errors.returnTime" class="mt-1 text-sm text-red-500">{{ errors.returnTime }}</p>
              </el-col>
            </el-row>

            <el-row :gutter="20" class="mt-6" v-if="settings.returnCycle === 'enable'">
              <!-- 超时提醒 -->
              <el-col :span="24" :md="12" class="space-y-2">
                <label class="block text-sm font-medium text-gray-700">
                  超时提醒
                  <span class="text-red-500 ml-1">*</span>
                  <el-tooltip effect="dark" content="当钥匙借用超时时发送提醒通知">
                    <el-icon class="text-gray-400 ml-2 cursor-help hover:text-primary transition-colors">
                      <InfoFilled />
                    </el-icon>
                  </el-tooltip>
                </label>
                <div class="flex items-center space-x-3">
                  <el-select v-model="settings.timeoutReminder" placeholder="请选择状态" clearable style="width: 240px"
                    @change="handleSelectChange('timeoutReminder')" :class="{ 'error-input': errors.timeoutReminder }">
                    <el-option v-for="dict in key_cabinet_status" :key="dict.value" :label="dict.label"
                      :value="dict.value" />
                  </el-select>
                  <p v-if="errors.timeoutReminder" class="mt-1 text-sm text-red-500">{{ errors.timeoutReminder }}</p>
                </div>
              </el-col>
            </el-row>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="card">
      <el-col :span="24">
        <!-- 其他设置卡片 -->
        <el-card shadow="hover" class="transition-all duration-300 border-0">
          <template #header>
            <div class="flex items-center justify-between pb-2">
              <span class="text-lg font-medium flex items-center text-gray-800">
                <el-icon class="mr-2 text-primary">
                  <Setting />
                </el-icon> 其他设置
              </span>
            </div>
          </template>
          <div class="p-1 md:p-4 space-y-6">
            <el-row :gutter="20">
              <!-- 自动返回主页 -->
              <el-col :span="24" :md="12" class="space-y-2">
                <label class="block text-sm font-medium text-gray-700">
                  自动返回主页
                  <span class="text-red-500 ml-1">*</span>
                  <el-tooltip effect="dark" content="长时间无操作后自动返回首页的时间设置">
                    <el-icon class="text-gray-400 ml-2 cursor-help hover:text-primary transition-colors">
                      <InfoFilled />
                    </el-icon>
                  </el-tooltip>
                </label>
                <el-select v-model="settings.returnToHome" placeholder="选择时间" class="w-full"
                  @change="handleSelectChange('returnToHome')" :class="{ 'error-input': errors.returnToHome }">
                  <el-option value="3" label="3分钟"></el-option>
                  <el-option value="5" label="5分钟"></el-option>
                </el-select>
                <p v-if="errors.returnToHome" class="mt-1 text-sm text-red-500">{{ errors.returnToHome }}</p>
              </el-col>
              <!-- 语音播报 -->
              <el-col :span="24" :md="12" class="space-y-2">
                <label class="block text-sm font-medium text-gray-700">
                  语音播报
                  <span class="text-red-500 ml-1">*</span>
                  <el-tooltip effect="dark" content="操作时是否进行语音提示">
                    <el-icon class="text-gray-400 ml-2 cursor-help hover:text-primary transition-colors">
                      <InfoFilled />
                    </el-icon>
                  </el-tooltip>
                </label>
                <div class="flex items-center space-x-3">
                  <el-select v-model="settings.voiceAnnouncement" placeholder="请选择状态" clearable style="width: 240px"
                    @change="handleSelectChange('voiceAnnouncement')"
                    :class="{ 'error-input': errors.voiceAnnouncement }">
                    <el-option v-for="dict in key_cabinet_status" :key="dict.value" :label="dict.label"
                      :value="dict.value" />
                  </el-select>
                  <p v-if="errors.voiceAnnouncement" class="mt-1 text-sm text-red-500">{{ errors.voiceAnnouncement }}
                  </p>
                </div>
              </el-col>
            </el-row>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, watch, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { getKeyConfiguration, updateKeyConfiguration } from "@/api/function/keyConfiguration";
import { getCurrentInstance } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus';

// 路由
const route = useRoute();
const router = useRouter();
const { proxy } = getCurrentInstance();
const { key_cabinet_status } = proxy.useDict("key_cabinet_status");

// 原始设置数据，用于重置
const originalSettings = reactive({
  keyNumber: '',
  cabinetName: '',
  cabinetNamePrefix: '',
  cabinetNameSuffix: '',
  cabinetCapacity: '',
  adminPassword: '',
  scheduledReboot: '',
  rebootTime: '',
  keyRequest: '',
  borrowReview: '',
  returnCycle: '',
  returnTime: '',
  timeoutReminder: '',
  returnToHome: '',
  voiceAnnouncement: ''
});

// 当前设置数据
const settings = reactive({ ...originalSettings });

// 错误信息对象
const errors = reactive({
  keyNumber: '',
  cabinetName: '',
  cabinetNamePrefix: '',
  cabinetNameSuffix: '',
  cabinetCapacity: '',
  adminPassword: '',
  scheduledReboot: '',
  rebootTime: '',
  keyRequest: '',
  borrowReview: '',
  returnCycle: '',
  returnTime: '',
  timeoutReminder: '',
  returnToHome: '',
  voiceAnnouncement: ''
});

const showPassword = ref(false);
const saving = ref(false);
const hasChanged = ref(false);

// 加载设置数据
const getKeyId = async () => {
  const id = route.params?.Id;
  const response = await getKeyConfiguration(id);
  Object.assign(settings, response.data);
  cabinetNameData(settings);
  Object.assign(originalSettings, response.data);
};

// 跳转
const handleQueryId = () => {
  const id = route.params?.Id;
  router.push("/function/auth/index/" + id);
};

// 解析柜子名称
const cabinetNameData = (row) => {
  const name = row.cabinetName || '';
  const match = name.match(/^(.*?)（(.*?)）$/);
  if (match) {
    settings.cabinetNamePrefix = match[1];
    settings.cabinetNameSuffix = match[2];
  } else {
    settings.cabinetNamePrefix = name;
    settings.cabinetNameSuffix = '';
  }
};

// 保存设置
const saveSettings = async () => {
  clearErrors();
  let isValid = true;

  // 序列号验证
  if (!settings.keyNumber) {
    errors.keyNumber = '请输入序列号';
    isValid = false;
  } else if (!/^\d+$/.test(settings.keyNumber)) {
    errors.keyNumber = '序列号只能包含数字';
    isValid = false;
  } else if (settings.keyNumber.length > 11) {
    errors.keyNumber = '序列号长度不能超过11位';
    isValid = false;
  }

  // 柜子名称
  if (!settings.cabinetName) {
    errors.cabinetName = '请输入柜子名称';
    isValid = false;
  } else {
    settings.cabinetName = settings.cabinetNameSuffix
      ? `${settings.cabinetNamePrefix}（${settings.cabinetNameSuffix}）`
      : settings.cabinetNamePrefix;
  }

  // 管理密码
  if (!settings.adminPassword) {
    errors.adminPassword = '请输入管理密码';
    isValid = false;
  } else if (settings.adminPassword.length < 6 || settings.adminPassword.length > 10) {
    errors.adminPassword = '密码长度必须为6-10位';
    isValid = false;
  }

  // 定时重启
  if (!settings.scheduledReboot) {
    errors.scheduledReboot = '请设置定时重启';
    isValid = false;
  } else if (settings.scheduledReboot === 'enable' && !settings.rebootTime) {
    errors.rebootTime = '请设置重启时间';
    isValid = false;
  }

  // 钥匙申请
  if (!settings.keyRequest) {
    errors.keyRequest = '请设置钥匙申请状态';
    isValid = false;
  } else if (settings.keyRequest === 'enable' && !settings.borrowReview) {
    errors.borrowReview = '请设置借用审核状态';
    isValid = false;
  }

  // 归还周期
  if (!settings.returnCycle) {
    errors.returnCycle = '请设置归还周期限制';
    isValid = false;
  } else if (settings.returnCycle === 'enable' && !settings.returnTime) {
    errors.returnTime = '请设置最长借用时间';
    isValid = false;
  }

  // 超时提醒
  if (settings.returnCycle === 'enable' && !settings.timeoutReminder) {
    errors.timeoutReminder = '请设置超时提醒';
    isValid = false;
  }

  // 自动返回主页
  if (!settings.returnToHome) {
    errors.returnToHome = '请选择自动返回主页时间';
    isValid = false;
  }

  // 语音播报
  if (!settings.voiceAnnouncement) {
    errors.voiceAnnouncement = '请设置语音播报状态';
    isValid = false;
  }

  if (!isValid) {
    scrollToFirstError();
    return;
  }

  saving.value = true;
  try {
    const response = await updateKeyConfiguration(settings);
    Object.assign(originalSettings, settings);
    Object.assign(settings, response.data);
    hasChanged.value = false;
    ElMessage.success('设置保存成功');
    // 关闭页面逻辑，如果使用 Tab 管理
    // $tab.closeOpenPage({ path: "/function/keyConfiguration" });
  } finally {
    saving.value = false;
  }
};

// 清空错误
const clearErrors = () => {
  Object.keys(errors).forEach(key => {
    errors[key] = '';
  });
};

// 滚动到第一个错误
const scrollToFirstError = () => {
  const firstError = document.querySelector('.text-red-500');
  if (firstError) firstError.scrollIntoView({ behavior: 'smooth', block: 'center' });
};

// 重置表单
const resetForm = () => {
  ElMessageBox.confirm('确定要恢复默认设置吗？当前修改将会丢失', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    getKeyId();
    clearErrors();
    hasChanged.value = false;
    ElMessage.success('已恢复默认设置');
  }).catch(() => { });
};

// 输入变化处理
const handleInput = (field) => {
  if (errors[field]) errors[field] = '';
  checkChanges();
};

// 下拉选择变化处理
const handleSelectChange = (field) => {
  if (errors[field]) errors[field] = '';
  if (field === 'scheduledReboot' && errors.rebootTime) errors.rebootTime = '';
  if (field === 'keyRequest' && errors.borrowReview) errors.borrowReview = '';
  if (field === 'returnCycle') {
    if (errors.returnTime) errors.returnTime = '';
    if (errors.timeoutReminder) errors.timeoutReminder = '';
  }
  checkChanges();
};

// 检查是否有变化
const checkChanges = () => {
  hasChanged.value = JSON.stringify(settings) !== JSON.stringify(originalSettings);
};

onMounted(() => {
  getKeyId();
});
</script>
<style lang="css" scoped>
  .card{
    margin-top: 10px;
    margin-bottom: 10px;
  }
</style>