<template>
  <div class="region-list">
    <el-card class="operation-bar">
      <el-button type="primary" @click="handleAdd">添加区域</el-button>
    </el-card>

    <el-card>
      <el-table :data="regionList" style="width: 100%">
        <el-table-column prop="name" label="区域名称" width="180"></el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="scope">
            <el-button type="text" size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="text" size="small" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessageBox, ElMessage } from 'element-plus';
import { getRegionList, deleteRegion } from '@/api/region';
import type { RegionData } from '@/api/region';

const router = useRouter();
const regionList = ref<RegionData[]>([]);

const fetchRegionList = async () => {
  const { data } = await getRegionList();
  regionList.value = data;
};

const handleAdd = () => {
  router.push('/region/add');
};

const handleEdit = (region: RegionData) => {
  router.push(`/region/edit/${region.id}`);
};

const handleDelete = (region: RegionData) => {
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
  margin-bottom: 20px;
}
</style> 