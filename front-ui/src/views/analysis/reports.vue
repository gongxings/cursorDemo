<template>
  <div class="reports">
    <!-- 报告列表 -->
    <el-card>
      <!-- 操作按钮 -->
      <div class="operation-bar">
        <el-button type="primary" @click="handleGenerateReport">
          <el-icon><Plus /></el-icon>生成报告
        </el-button>
      </div>
      <el-table
        v-loading="loading"
        :data="reportList"
        border
        style="width: 100%"
      >
        <el-table-column prop="title" label="报告标题" min-width="200" show-overflow-tooltip />
        <el-table-column prop="type" label="报告类型" width="120">
          <template #default="{ row }">
            <el-tag :type="getReportTypeTag(row.type)">
              {{ getReportTypeName(row.type) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="生成时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusTag(row.status)">
              {{ row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button 
              link 
              type="primary" 
              @click="handlePreview(row)"
              :disabled="row.status !== '已完成'"
            >
              预览
            </el-button>
            <el-button 
              link 
              type="primary" 
              @click="handleExport(row)"
              :disabled="row.status !== '已完成'"
            >
              导出
            </el-button>
            <el-button 
              link 
              type="danger" 
              @click="handleDelete(row)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :total="total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 生成报告对话框 -->
    <el-dialog
      v-model="dialogVisible"
      title="生成分析报告"
      width="500px"
    >
      <el-form
        ref="formRef"
        :model="reportForm"
        :rules="rules"
        label-width="100px"
      >
        <el-form-item label="报告标题" prop="title">
          <el-input v-model="reportForm.title" placeholder="请输入报告标题" />
        </el-form-item>
        <el-form-item label="报告类型" prop="type">
          <el-select v-model="reportForm.type" placeholder="请选择报告类型">
            <el-option label="市场分析报告" value="market" />
            <el-option label="价格分析报告" value="price" />
            <el-option label="综合分析报告" value="comprehensive" />
          </el-select>
        </el-form-item>
        <el-form-item label="分析区域" prop="district">
          <el-select v-model="reportForm.district" placeholder="请选择分析区域" clearable>
            <el-option
              v-for="item in districtOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="分析周期" prop="period">
          <el-select v-model="reportForm.period" placeholder="请选择分析周期">
            <el-option label="最近一周" value="week" />
            <el-option label="最近一月" value="month" />
            <el-option label="最近三月" value="quarter" />
            <el-option label="最近一年" value="year" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitGenerateReport" :loading="submitting">
            确定
          </el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 预览对话框 -->
    <el-dialog
      v-model="previewVisible"
      title="报告预览"
      width="800px"
      class="preview-dialog"
    >
      <div class="preview-content" v-loading="previewLoading">
        <div class="report-title">{{ currentReport?.title }}</div>
        <div class="report-info">
          <span>生成时间：{{ formatDate(currentReport?.createTime) }}</span>
          <span>报告类型：{{ getReportTypeName(currentReport?.type) }}</span>
        </div>
        <div class="report-content" v-html="reportContent"></div>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="previewVisible = false">关闭</el-button>
          <el-button type="primary" @click="handleExport(currentReport)">
            导出报告
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { Plus } from '@element-plus/icons-vue';
import { getReportHistory, generateReport, exportReport } from '@/api/analysis';
import { formatDate } from '@/utils/date';

// 列表数据
const loading = ref(false);
const reportList = ref([]);
const currentPage = ref(1);
const pageSize = ref(10);
const total = ref(0);

// 生成报告相关
const dialogVisible = ref(false);
const submitting = ref(false);
const formRef = ref();
const reportForm = reactive({
  title: '',
  type: '',
  district: '',
  period: ''
});

const rules = {
  title: [{ required: true, message: '请输入报告标题', trigger: 'blur' }],
  type: [{ required: true, message: '请选择报告类型', trigger: 'change' }],
  period: [{ required: true, message: '请选择分析周期', trigger: 'change' }]
};

// 预览相关
const previewVisible = ref(false);
const previewLoading = ref(false);
const currentReport = ref();
const reportContent = ref('');

// 区域选项
const districtOptions = [
  { value: '朝阳区', label: '朝阳区' },
  { value: '海淀区', label: '海淀区' },
  { value: '东城区', label: '东城区' },
  { value: '西城区', label: '西城区' }
];

// 获取报告列表
const fetchReportList = async () => {
  loading.value = true;
  try {
    const { data } = await getReportHistory();
    reportList.value = data?.data;
    total.value = data?.data.length;
  } catch (error) {
    console.error('获取报告列表失败:', error);
    ElMessage.error('获取报告列表失败');
  } finally {
    loading.value = false;
  }
};

// 生成报告
const handleGenerateReport = () => {
  reportForm.title = `${getReportTypeName(reportForm.type)}_${formatDate(new Date(), 'YYYY-MM-DD')}`;
  dialogVisible.value = true;
};

// 提交生成报告
const submitGenerateReport = async () => {
  if (!formRef.value) return;
  
  await formRef.value.validate(async (valid: boolean) => {
    if (valid) {
      submitting.value = true;
      try {
        await generateReport(reportForm.type);
        ElMessage.success('报告生成任务已提交');
        dialogVisible.value = false;
        fetchReportList();
      } catch (error) {
        console.error('生成报告失败:', error);
        ElMessage.error('生成报告失败');
      } finally {
        submitting.value = false;
      }
    }
  });
};

// 预览报告
const handlePreview = async (row: any) => {
  currentReport.value = row;
  previewVisible.value = true;
  previewLoading.value = true;
  
  try {
    // 这里应该调用预览API获取报告内容
    reportContent.value = '报告内容示例';
  } catch (error) {
    console.error('获取报告内容失败:', error);
    ElMessage.error('获取报告内容失败');
  } finally {
    previewLoading.value = false;
  }
};

// 导出报告
const handleExport = async (row: any) => {
  try {
    const blob = await exportReport(row.id);
    const url = window.URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = `${row.title}.pdf`;
    link.click();
    window.URL.revokeObjectURL(url);
  } catch (error) {
    console.error('导出报告失败:', error);
    ElMessage.error('导出报告失败');
  }
};

// 删除报告
const handleDelete = (row: any) => {
  ElMessageBox.confirm('确定要删除该报告吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      // 这里应该调用删除API
      ElMessage.success('删除成功');
      fetchReportList();
    } catch (error) {
      console.error('删除报告失败:', error);
      ElMessage.error('删除报告失败');
    }
  });
};

// 获取报告类型标签样式
const getReportTypeTag = (type: string) => {
  const map: Record<string, string> = {
    market: 'success',
    price: 'warning',
    comprehensive: 'info'
  };
  return map[type] || 'info';
};

// 获取报告类型名称
const getReportTypeName = (type: string) => {
  const map: Record<string, string> = {
    market: '市场分析报告',
    price: '价格分析报告',
    comprehensive: '综合分析报告'
  };
  return map[type] || type;
};

// 获取状态标签样式
const getStatusTag = (status: string) => {
  const map: Record<string, string> = {
    '已完成': 'success',
    '生成中': 'warning',
    '失败': 'danger'
  };
  return map[status] || 'info';
};

// 分页相关
const handleSizeChange = (val: number) => {
  pageSize.value = val;
  fetchReportList();
};

const handleCurrentChange = (val: number) => {
  currentPage.value = val;
  fetchReportList();
};

onMounted(() => {
  fetchReportList();
});
</script>

<style lang="scss" scoped>
.reports {
  .operation-bar {
    margin-bottom: 20px;
  }

  .pagination {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
  }

  .preview-dialog {
    :deep(.el-dialog__body) {
      padding: 20px;
    }

    .preview-content {
      min-height: 400px;

      .report-title {
        font-size: 20px;
        font-weight: bold;
        text-align: center;
        margin-bottom: 20px;
      }

      .report-info {
        color: #666;
        margin-bottom: 20px;
        display: flex;
        justify-content: space-between;
      }

      .report-content {
        line-height: 1.8;
      }
    }
  }
}
</style> 