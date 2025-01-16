<template>
  <div class="house-list">
    <!-- 搜索表单 -->
    <el-card class="search-card">
      <el-form :model="searchForm" inline>
        <el-form-item label="区域">
          <el-select v-model="searchForm.district" placeholder="请选择区域" clearable style="width: 200px;">
            <el-option
              v-for="item in districtOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="价格区间">
          <el-input-number
            v-model="searchForm.minPrice"
            :min="0"
            placeholder="最低价"
            style="width: 130px"
          />
          <span class="mx-2">-</span>
          <el-input-number
            v-model="searchForm.maxPrice"
            :min="0"
            placeholder="最高价"
            style="width: 130px"
          />
        </el-form-item>
        <el-form-item label="房型">
          <el-select v-model="searchForm.roomCount" placeholder="请选择房型" clearable style="width: 200px;">
            <el-option
              v-for="item in roomCountOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 房源列表 -->
    <el-card>
      <!-- 操作按钮 -->
      <div class="operation-bar">
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>添加房源
        </el-button>
        <el-upload
            action="/api/houses/import"
            accept=".csv, .xlsx"
            :on-success="handleImportSuccess"
            :show-file-list="false"
            :before-upload="beforeUpload"
        >
          <el-button type="primary">
            <el-icon><Upload /></el-icon>导入房源
          </el-button>
        </el-upload>
        <el-button type="primary" @click="downloadTemplate">
          <el-icon><Download /></el-icon>下载模板
        </el-button>
      </div>
      <el-table
        v-loading="loading"
        :data="houseList"
        border
        style="width: 100%"
      >
        <el-table-column prop="title" label="标题" min-width="200" show-overflow-tooltip />
        <el-table-column prop="district" label="区域" width="120" />
        <el-table-column prop="price" label="价格" width="120">
          <template #default="{ row }">
            {{ row.price }}元/月
          </template>
        </el-table-column>
        <el-table-column prop="area" label="面积" width="120">
          <template #default="{ row }">
            {{ row.area }}㎡
          </template>
        </el-table-column>
        <el-table-column prop="roomCount" label="房型" width="120">
          <template #default="{ row }">
            {{ row.roomCount }}室
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === '已租' ? 'success' : 'info'">
              {{ row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="scope">
            <el-button type="text" size="small" @click="handleView(scope.row)">查看</el-button>
            <el-button type="text" size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="text" size="small" @click="handleDelete(scope.row)">删除</el-button>
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
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessageBox, ElMessage } from 'element-plus';
import { Plus, Upload, Download } from '@element-plus/icons-vue';
import { getHouseList, searchHouses, deleteHouse } from '@/api/house';
import type { HouseData, HouseQuery } from '@/api/house';
import { getRegions as getAllRegions } from '@/api/region';

const router = useRouter();
const loading = ref(false);
const houseList = ref<HouseData[]>([]);
const currentPage = ref(1);
const pageSize = ref(10);
const total = ref(0);

// 搜索表单
const searchForm = reactive<HouseQuery>({
  district: '',
  minPrice: undefined,
  maxPrice: undefined,
  roomCount: undefined
});

// 区域选项
const districtOptions = [
  { value: '朝阳区', label: '朝阳区' },
  { value: '海淀区', label: '海淀区' },
  { value: '东城区', label: '东城区' },
  { value: '西城区', label: '西城区' }
];

// 房型选项
const roomCountOptions = [
  { value: 1, label: '一室' },
  { value: 2, label: '两室' },
  { value: 3, label: '三室' },
  { value: 4, label: '四室' },
  { value: 5, label: '五室' },
  { value: 6, label: '六室' },
  { value: 7, label: '七室' },
  { value: 8, label: '八室' },
  { value: 9, label: '九室' },
  { value: 10, label: '十室及以上' }
];

// 获取房源列表
const fetchHouseList = async () => {
  loading.value = true;
  try {
    const { data } = await getHouseList();
    houseList.value = data.data;
    total.value = data.data.length; // 实际项目中应该从后端获取总数
  } catch (error) {
    console.error('获取房源列表失败:', error);
    ElMessage.error('获取房源列表失败');
  } finally {
    loading.value = false;
  }
};

// 搜索
const handleSearch = async () => {
  loading.value = true;
  try {
    const { data } = await searchHouses(searchForm);
    houseList.value = data.data;
    total.value = data.data.length;
  } catch (error) {
    console.error('搜索房源失败:', error);
    ElMessage.error('搜索房源失败');
  } finally {
    loading.value = false;
  }
};

// 重置搜索
const resetSearch = () => {
  Object.keys(searchForm).forEach(key => {
    searchForm[key as keyof HouseQuery] = undefined;
  });
  handleSearch();
};

// 添加房源
const handleAdd = () => {
  router.push('/house/add');
};

// 编辑房源
const handleEdit = (row: HouseData) => {
  router.push(`/house/edit/${row.id}`);
};

// 查看房源
const handleView = (row: HouseData) => {
  router.push(`/house/view/${row.id}`);
};

// 删除房源
const handleDelete = (row: HouseData) => {
  ElMessageBox.confirm('确定要删除该房源吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteHouse(row.id!);
      ElMessage.success('删除成功');
      fetchHouseList();
    } catch (error) {
      console.error('删除房源失败:', error);
      ElMessage.error('删除房源失败');
    }
  });
};

// 分页相关
const handleSizeChange = (val: number) => {
  pageSize.value = val;
  fetchHouseList();
};

const handleCurrentChange = (val: number) => {
  currentPage.value = val;
  fetchHouseList();
};

const handleImportSuccess = () => {
  ElMessage.success('房源导入成功');
  fetchHouseList(); // 重新获取房源列表
};

// 获取区域选项
const fetchDistrictOptions = async () => {
  try {
    const { data } = await getAllRegions();
    districtOptions.splice(0, districtOptions.length, ...data);
  } catch (error) {
    console.error('获取区域选项失败:', error);
  }
};

// 上传前检查文件类型
const beforeUpload = (file: File) => {
  const isExcel = file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' || file.type === 'application/vnd.ms-excel';
  if (!isExcel) {
    ElMessage.error('只能上传 Excel 文件');
  }
  return isExcel;
};

// 下载模板
const downloadTemplate = () => {
  window.open('/api/houses/template', '_blank');
};

onMounted(() => {
  fetchHouseList();
  fetchDistrictOptions();
});
</script>

<style lang="scss" scoped>
.house-list {
  .search-card {
    margin-bottom: 20px;
  }

  .operation-bar {
  display: flex;
  align-items: center;
  gap: 10px; // 调整按钮之间的间距
  margin-bottom: 20px;
}

  .pagination {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
  }

  :deep(.el-card__body) {
    padding: 20px;
  }

  .mx-2 {
    margin: 0 8px;
  }
}
</style> 