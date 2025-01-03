<template>
  <div class="house-list">
    <!-- 搜索表单 -->
    <el-card class="search-card">
      <el-form :model="searchForm" inline>
        <el-form-item label="区域">
          <el-select v-model="searchForm.district" placeholder="请选择区域" clearable>
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
          <el-select v-model="searchForm.roomCount" placeholder="请选择房型" clearable>
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

    <!-- 操作按钮 -->
    <div class="operation-bar">
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>添加房源
      </el-button>
    </div>

    <!-- 房源列表 -->
    <el-card>
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
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="primary" @click="handleView(row)">查看</el-button>
            <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
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
import { Plus } from '@element-plus/icons-vue';
import { getHouseList, searchHouses, deleteHouse } from '@/api/house';
import type { HouseData, HouseQuery } from '@/api/house';

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
  { value: 4, label: '四室及以上' }
];

// 获取房源列表
const fetchHouseList = async () => {
  loading.value = true;
  try {
    const { data } = await getHouseList();
    houseList.value = data;
    total.value = data.length; // 实际项目中应该从后端获取总数
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
    houseList.value = data;
    total.value = data.length;
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
  // TODO: 实现查看详情功能
  console.log('查看房源:', row);
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

onMounted(() => {
  fetchHouseList();
});
</script>

<style lang="scss" scoped>
.house-list {
  .search-card {
    margin-bottom: 20px;
  }

  .operation-bar {
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