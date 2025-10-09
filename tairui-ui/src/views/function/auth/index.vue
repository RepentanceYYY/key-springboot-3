<template>
  <div class="app-container p-4 md:p-6 max-w-6xl mx-auto">
    <!-- 页面标题区 -->
    <div class="mb-8 animate-fade-in">
      <h2 class="text-[clamp(1.5rem,3vw,1.8rem)] font-semibold text-gray-800 flex items-center">
        <el-icon class="mr-2 text-primary"><Setting /></el-icon>高级配置
      </h2>
      <p class="text-gray-500 mt-1">配置柜子的高级参数</p>
    </div>

    <!-- 操作按钮区 - 固定在顶部便于操作 -->
    <div class="flex justify-end mb-6 gap-3 sticky top-0 z-10 bg-white/80 backdrop-blur-sm p-3 rounded-lg shadow-sm">
      <el-button
        type="default"
        @click="resetForm"
        :disabled="!hasChanged"
        class="transition-all hover:shadow-md"
      >
        <el-icon class="mr-1"><RefreshRight /></el-icon> 重置
      </el-button>
      <el-button
        type="primary"
        @click="saveSettings"
        :loading="saving"
        :disabled="!hasChanged || saving"
        class="transition-all hover:shadow-md"
      >
        <el-icon class="mr-1"><Check /></el-icon> 保存设置
      </el-button>
    </div>

    <el-row :gutter="20">
      <el-col :span="24">
        <!-- 主要设置卡片 - 增强视觉层次感 -->
        <el-card shadow="hover" class="transition-all duration-300 border-0 mb-6 overflow-hidden">
          <template #header>
            <div class="flex items-center justify-between pb-2 bg-gray-50 px-6 py-4">
              <span class="text-lg font-medium flex items-center text-gray-800">
                <el-icon class="mr-2 text-primary"><Tools /></el-icon> 认证方式设置
              </span>
            </div>
          </template>

          <!-- 设置表单区域 - 使用网格系统优化布局 -->
          <div class="p-4 md:p-6 space-y-8">
            <!-- 认证方式组 -->
            <div class="border-b border-gray-100 pb-6">

              <!-- 第一行：人脸认证和活体认证 -->
              <el-row :gutter="20" class="mb-6">
                <!-- 人脸认证 -->
                <el-col :span="24" :md="12" class="space-y-2">
                  <label class="block text-sm font-medium text-gray-700">
                    人脸认证
                    <span class="text-red-500 ml-1">*</span>
                    <el-tooltip effect="dark" content="是否启用人脸认证功能">
                      <el-icon class="text-gray-400 ml-2 cursor-help hover:text-primary transition-colors"><InfoFilled /></el-icon>
                    </el-tooltip>
                  </label>
                  <div class="flex flex-wrap items-center space-x-3">
                    <el-select
                      v-model="authSettings.faceAuth"
                      placeholder="请选择状态"
                      clearable
                      style="width: 240px"
                      @change="handleSelectChange('faceAuth')"
                      :class="{ 'error-input': errors.faceAuth }"
                    >
                      <el-option
                        v-for="dict in key_cabinet_status"
                        :key="dict.value"
                        :label="dict.label"
                        :value="dict.value"
                      />
                    </el-select>
                    <p v-if="errors.faceAuth" class="mt-1 text-sm text-red-500">{{ errors.faceAuth }}</p>
                  </div>
                </el-col>

                <!-- 密码认证 -->
                <el-col :span="24" :md="12" class="space-y-2">
                  <label class="block text-sm font-medium text-gray-700">
                    密码认证
                    <span class="text-red-500 ml-1">*</span>
                    <el-tooltip effect="dark" content="是否启用密码验证功能">
                      <el-icon class="text-gray-400 ml-2 cursor-help hover:text-primary transition-colors"><InfoFilled /></el-icon>
                    </el-tooltip>
                  </label>
                  <div class="flex flex-wrap items-center space-x-3">
                    <el-select
                      v-model="authSettings.passwordAuth"
                      placeholder="请选择状态"
                      clearable
                      style="width: 240px"
                      @change="handleSelectChange('passwordAuth')"
                      :class="{ 'error-input': errors.passwordAuth }"
                    >
                      <el-option
                        v-for="dict in key_cabinet_status"
                        :key="dict.value"
                        :label="dict.label"
                        :value="dict.value"
                      />
                    </el-select>
                    <p v-if="errors.passwordAuth" class="mt-1 text-sm text-red-500">{{ errors.passwordAuth }}</p>
                  </div>
                </el-col>
              </el-row>

              <!-- 第二行：指纹认证和刷卡认证 -->
              <el-row :gutter="20">
                <!-- 指纹认证 -->
                <el-col :span="24" :md="12" class="space-y-2">
                  <label class="block text-sm font-medium text-gray-700">
                    指纹认证
                    <span class="text-red-500 ml-1">*</span>
                    <el-tooltip effect="dark" content="是否启用指纹识别认证功能">
                      <el-icon class="text-gray-400 ml-2 cursor-help hover:text-primary transition-colors"><InfoFilled /></el-icon>
                    </el-tooltip>
                  </label>
                  <div class="flex flex-wrap items-center space-x-3">
                    <el-select
                      v-model="authSettings.fingerprintAuth"
                      placeholder="请选择状态"
                      clearable
                      style="width: 240px"
                      @change="handleSelectChange('fingerprintAuth')"
                      :class="{ 'error-input': errors.fingerprintAuth }"
                    >
                      <el-option
                        v-for="dict in key_cabinet_status"
                        :key="dict.value"
                        :label="dict.label"
                        :value="dict.value"
                      />
                    </el-select>
                    <p v-if="errors.fingerprintAuth" class="mt-1 text-sm text-red-500">{{ errors.fingerprintAuth }}</p>
                  </div>
                </el-col>

                <!-- 刷卡认证 -->
                <el-col :span="24" :md="12" class="space-y-2">
                  <label class="block text-sm font-medium text-gray-700">
                    刷卡认证
                    <span class="text-red-500 ml-1">*</span>
                    <el-tooltip effect="dark" content="是否启用IC/ID卡刷卡认证功能">
                      <el-icon class="text-gray-400 ml-2 cursor-help hover:text-primary transition-colors"><InfoFilled /></el-icon>
                    </el-tooltip>
                  </label>
                  <div class="flex flex-wrap items-center space-x-3">
                    <el-select
                      v-model="authSettings.cardAuth"
                      placeholder="请选择状态"
                      clearable
                      style="width: 240px"
                      @change="handleSelectChange('cardAuth')"
                      :class="{ 'error-input': errors.cardAuth }"
                    >
                      <el-option
                        v-for="dict in key_cabinet_status"
                        :key="dict.value"
                        :label="dict.label"
                        :value="dict.value"
                      />
                    </el-select>
                    <p v-if="errors.cardAuth" class="mt-1 text-sm text-red-500">{{ errors.cardAuth }}</p>
                  </div>
                </el-col>
              </el-row>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    
    <el-row :gutter="20">
      <el-col :span="24">
        <el-card shadow="hover" class="transition-all duration-300 border-0">
          <!-- 高级设置组 -->
          <template #header>
            <div class="flex items-center justify-between pb-2">
              <span class="text-lg font-medium flex items-center text-gray-800">
                <el-icon class="mr-2 text-gray-500"><Setting /></el-icon>高级功能配置
              </span>
            </div>
          </template>
          
          <!-- 第三行：密码认证和酒精检测 -->
          <el-row :gutter="20" class="mb-6">
            <!-- 活体认证 -->
            <el-col :span="24" :md="12" class="space-y-2">
              <label class="block text-sm font-medium text-gray-700">
                活体认证
                <span class="text-red-500 ml-1">*</span>
                <el-tooltip effect="dark" content="是否启用活体检测功能，防止照片欺骗">
                  <el-icon class="text-gray-400 ml-2 cursor-help hover:text-primary transition-colors"><InfoFilled /></el-icon>
                </el-tooltip>
              </label>
              <div class="flex flex-wrap items-center space-x-3">
                <el-select
                  v-model="authSettings.livenessAuth"
                  placeholder="请选择状态"
                  clearable
                  style="width: 240px"
                  @change="handleSelectChange('livenessAuth')"
                  :class="{ 'error-input': errors.livenessAuth }"
                >
                  <el-option
                    v-for="dict in key_cabinet_status"
                    :key="dict.value"
                    :label="dict.label"
                    :value="dict.value"
                  />
                </el-select>
                <p v-if="errors.livenessAuth" class="mt-1 text-sm text-red-500">{{ errors.livenessAuth }}</p>
              </div>
            </el-col>

            <!-- 错位模式 -->
            <el-col :span="24" :md="12" class="space-y-2">
              <label class="block text-sm font-medium text-gray-700">
                错位模式
                <span class="text-red-500 ml-1">*</span>
                <el-tooltip effect="dark" content="是否启用错位放置模式">
                  <el-icon class="text-gray-400 ml-2 cursor-help hover:text-primary transition-colors"><InfoFilled /></el-icon>
                </el-tooltip>
              </label>
              <div class="flex flex-wrap items-center space-x-3">
                <el-select
                  v-model="authSettings.misalignmentMode"
                  placeholder="请选择状态"
                  clearable
                  style="width: 240px"
                  @change="handleSelectChange('misalignmentMode')"
                  :class="{ 'error-input': errors.misalignmentMode }"
                >
                  <el-option
                    v-for="dict in key_cabinet_status"
                    :key="dict.value"
                    :label="dict.label"
                    :value="dict.value"
                  />
                </el-select>
                <p v-if="errors.misalignmentMode" class="mt-1 text-sm text-red-500">{{ errors.misalignmentMode }}</p>
              </div>
            </el-col>
          </el-row>

          <!-- 第四行：酒精检测阈值和错位模式 -->
          <el-row :gutter="20">
            <!-- 酒精检测 -->
            <el-col :span="24" :md="12" class="space-y-2">
              <label class="block text-sm font-medium text-gray-700">
                酒精检测
                <span class="text-red-500 ml-1">*</span>
                <el-tooltip effect="dark" content="是否启用酒精浓度检测功能">
                  <el-icon class="text-gray-400 ml-2 cursor-help hover:text-primary transition-colors"><InfoFilled /></el-icon>
                </el-tooltip>
              </label>
              <div class="flex flex-wrap items-center space-x-3">
                <el-select
                  v-model="authSettings.alcoholDetection"
                  placeholder="请选择状态"
                  clearable
                  style="width: 240px"
                  @change="handleSelectChange('alcoholDetection')"
                  :class="{ 'error-input': errors.alcoholDetection }"
                >
                  <el-option
                    v-for="dict in key_cabinet_status"
                    :key="dict.value"
                    :label="dict.label"
                    :value="dict.value"
                  />
                </el-select>
                <p v-if="errors.alcoholDetection" class="mt-1 text-sm text-red-500">{{ errors.alcoholDetection }}</p>
              </div>
            </el-col>
            
            <!-- 酒精检测阈值 -->
            <el-col :span="24" :md="12" class="space-y-2">
              <label class="block text-sm font-medium text-gray-700">
                酒精检测阈值
                <span class="text-red-500 ml-1">*</span>
                <el-tooltip effect="dark" content="酒精检测的合格阈值标准">
                  <el-icon class="text-gray-400 ml-2 cursor-help hover:text-primary transition-colors"><InfoFilled /></el-icon>
                </el-tooltip>
              </label>
              <div class="flex flex-wrap items-center space-x-3">
                <el-input
                  v-model="authSettings.alcoholThreshold"
                  placeholder="请输入酒精检测阈值"
                  class="w-full transition-all"
                  @change="handleSelectChange('alcoholThreshold')"
                  :class="{ 'error-input': errors.alcoholThreshold }"
                  :disabled="true"
                ></el-input>
                <p v-if="errors.alcoholThreshold" class="mt-1 text-sm text-red-500">{{ errors.alcoholThreshold }}</p>
              </div>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="24">
        <el-card shadow="hover" class="transition-all duration-300 border-0">
          <!-- 显示设置组 -->
          <template #header>
            <div class="flex items-center justify-between pb-2">
              <span class="text-lg font-medium flex items-center text-gray-800">
                <el-icon class="mr-2 text-gray-500"><Monitor /></el-icon>显示设置
              </span>
            </div>
          </template>
          
          <div class="p-1 md:p-4 space-y-6">
            <!-- 第五行：小屏幕 -->
            <el-row :gutter="20">
              <!-- 小屏幕 -->
              <el-col :span="24" :md="12" class="space-y-2">
                <label class="block text-sm font-medium text-gray-700">
                  小屏幕模式
                  <span class="text-red-500 ml-1">*</span>
                  <el-tooltip effect="dark" content="是否适配小屏幕显示模式">
                    <el-icon class="text-gray-400 ml-2 cursor-help hover:text-primary transition-colors"><InfoFilled /></el-icon>
                  </el-tooltip>
                </label>
                <div class="flex flex-wrap items-center space-x-3">
                  <el-select
                    v-model="authSettings.smallScreen"
                    placeholder="请选择状态"
                    clearable
                    style="width: 240px"
                    @change="handleSelectChange('smallScreen')"
                    :class="{ 'error-input': errors.smallScreen }"
                  >
                    <el-option
                      v-for="dict in key_cabinet_status"
                      :key="dict.value"
                      :label="dict.label"
                      :value="dict.value"
                    />
                  </el-select>
                  <p v-if="errors.smallScreen" class="mt-1 text-sm text-red-500">{{ errors.smallScreen }}</p>
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
import { ref, reactive, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getCurrentInstance } from 'vue'
import { getAuth, updateAuth } from "@/api/function/auth"

// 路由对象
const route = useRoute()
const { proxy } = getCurrentInstance();
const { key_cabinet_status } = proxy.useDict("key_cabinet_status");

// 响应式状态
const saving = ref(false)
const hasChanged = ref(false)

// 原始设置数据
const originalSettings = reactive({
  faceAuth: '',
  livenessAuth: '',
  fingerprintAuth: '',
  cardAuth: '',
  passwordAuth: '',
  alcoholDetection: '',
  alcoholThreshold: '',
  misalignmentMode: '',
  smallScreen: ''
})

// 当前设置数据
const authSettings = reactive({
  faceAuth: '',
  livenessAuth: '',
  fingerprintAuth: '',
  cardAuth: '',
  passwordAuth: '',
  alcoholDetection: '',
  alcoholThreshold: '',
  misalignmentMode: '',
  smallScreen: ''
})

// 错误信息对象
const errors = reactive({
  faceAuth: '',
  livenessAuth: '',
  fingerprintAuth: '',
  cardAuth: '',
  passwordAuth: '',
  alcoholDetection: '',
  alcoholThreshold: '',
  misalignmentMode: '',
  smallScreen: ''
})

// 字典示例
const dict = reactive({
  type: {
    key_cabinet_status: [
      { value: 'enable', label: '启用' },
      { value: 'disable', label: '禁用' }
    ]
  }
})

// 加载设置
const getAuthId = () => {
  const id = route.params?.Id
  getAuth(id).then(response => {
    Object.assign(authSettings, response.data)
    Object.assign(originalSettings, response.data)
  })
}

// 清除错误
const clearErrors = () => {
  Object.keys(errors).forEach(key => {
    errors[key] = ''
  })
}

// 滚动到第一个错误
const scrollToFirstError = () => {
  const firstError = document.querySelector('.text-red-500')
  if (firstError) firstError.scrollIntoView({ behavior: 'smooth', block: 'center' })
}

// 检查表单变化
const checkChanges = () => {
  hasChanged.value = JSON.stringify(authSettings) !== JSON.stringify(originalSettings)
}

// 下拉框变化
const handleSelectChange = (field) => {
  if (errors[field]) errors[field] = ''
  if (field === 'alcoholDetection' && authSettings.alcoholDetection !== 'enable') {
    errors.alcoholThreshold = ''
  }
  checkChanges()
}

// 保存设置
const saveSettings = () => {
  clearErrors()
  let isValid = true

  if (!authSettings.faceAuth) { errors.faceAuth = '请选择人脸认证状态'; isValid = false }
  if (!authSettings.livenessAuth) { errors.livenessAuth = '请选择活体认证状态'; isValid = false }
  if (!authSettings.fingerprintAuth) { errors.fingerprintAuth = '请选择指纹认证状态'; isValid = false }
  if (!authSettings.cardAuth) { errors.cardAuth = '请选择刷卡认证状态'; isValid = false }
  if (!authSettings.passwordAuth) { errors.passwordAuth = '请选择密码认证状态'; isValid = false }

  if (!authSettings.alcoholDetection) { errors.alcoholDetection = '请选择酒精检测状态'; isValid = false }
  else if (authSettings.alcoholDetection === 'enable' && !authSettings.alcoholThreshold) {
    errors.alcoholThreshold = '请输入酒精检测阈值'; isValid = false
  }

  if (!authSettings.misalignmentMode) { errors.misalignmentMode = '请选择错位模式状态'; isValid = false }
  if (!authSettings.smallScreen) { errors.smallScreen = '请选择小屏幕模式状态'; isValid = false }

  if (!isValid) { scrollToFirstError(); return }

  saving.value = true
  updateAuth(authSettings).then(response => {
    Object.assign(originalSettings, authSettings)
    Object.assign(authSettings, response.data)
    hasChanged.value = false
    saving.value = false
    ElMessage.success('设置保存成功')
  }).catch(() => {
    saving.value = false
    ElMessage.error('保存失败，请重试')
  })
}

// 重置表单
const resetForm = () => {
  ElMessageBox.confirm('确定要恢复默认设置吗？当前修改将会丢失', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    getAuthId()
    clearErrors()
    hasChanged.value = false
    ElMessage.success('已恢复默认设置')
  }).catch(() => {})
}

// 组件挂载时加载数据
onMounted(() => {
  getAuthId()
})
</script>

<style scoped>
/* 基础样式增强  父组件中修改子组件的样式  */
:deep(.el-card__header) {
  border-bottom: 1px solid #f2f3f5 !important;
  padding: 14px 20px !important;
}

:deep(.el-input__inner),
:deep(.el-time-picker__input > input),
:deep(.el-select .el-input__inner) {
  padding: 10px 12px;
}

:deep(.el-switch__label) {
  padding: 0 10px;
}

/* 错误状态样式 */
:deep(.error-input .el-input__inner),
:deep(.error-input .el-time-picker__input > input),
:deep(.error-input .el-select .el-input__inner) {
  border-color: #f56c6c !important;
  box-shadow: 0 0 0 2px rgba(245, 108, 108, 0.2) !important;
}

/* 下拉框样式优化 */
:deep(.el-select) {
  transition: all 0.2s ease-in-out;
}

:deep(.el-select:hover .el-input__inner) {
  border-color: #409eff;
}

:deep(.el-select-dropdown__item) {
  padding: 8px 16px;
}

:deep(.el-select-dropdown__item.selected) {
  background-color: #f0f7ff;
  color: #409eff;
}

/* 动画效果 */
.transition-all {
  transition: all 0.2s ease-in-out;
}

.transition-colors {
  transition: color 0.2s ease-in-out;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .app-container {
    padding: 16px !important;
    padding-bottom: 80px !important; /* 为底部按钮留出空间 */
  }

  :deep(.el-card__body) {
    padding: 15px 10px !important;
  }
}

.text-red-500 {
  color: #f56c6c !important; /* Element Plus 错误红色 */
}

/* 桌面端隐藏底部按钮 */
@media (min-width: 769px) {
  .fixed.bottom-0 {
    display: none;
  }
}
</style>