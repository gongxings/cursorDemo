<template>
  <div class="region-list">
    <el-card>
      <div class="operation-bar">
        <el-input v-model="searchQuery" placeholder="搜索区域" class="search-input" @input="fetchRegionList"></el-input>
        <el-button type="primary" @click="handleAdd" class="add-button">添加区域</el-button>
      </div>
      <el-table :data="filteredRegionList" style="width: 100%">
        <el-table-column prop="province" label="省份" width="180"></el-table-column>
        <el-table-column prop="city" label="城市" width="180"></el-table-column>
        <el-table-column prop="county" label="县/区" width="180"></el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="scope">
            <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="small" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessageBox, ElMessage } from 'element-plus';
import { getRegions, deleteRegion } from '@/api/region';

const router = useRouter();
const regionList = ref<any[]>([]);
const searchQuery = ref('');

const fetchRegionList = async () => {
  const { data } = await getRegions();
  regionList.value = data?.data;
};

const filteredRegionList = computed(() => {
  if (!searchQuery.value) {
    return regionList.value;
  }
  return regionList.value.filter(region => 
    region.province.includes(searchQuery.value) || 
    region.city.includes(searchQuery.value) || 
    region.county.includes(searchQuery.value)
  );
});

const handleAdd = () => {
  router.push('/region/add');
};

const handleEdit = (region: any) => {
  router.push(`/region/edit/${region.id}`);
};

const handleDelete = (region: any) => {
  ElMessageBox.confirm('确定要删除该区域吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    await deleteRegion(region.id!);
    ElMessage.success('删除成功');
    fetchRegionList();
  }).catch(() => {});
};

onMounted(() => {
  fetchRegionList();
});
</script>

<style scoped>
.operation-bar {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
}
.search-input {
  width: 300px;
}
.add-button {
  margin-left: auto;
}
</style> 