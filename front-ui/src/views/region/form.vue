<template>
  <div class="region-form">
    <el-card>
      <el-form :model="regionForm" ref="regionFormRef" label-width="80px">
        <el-form-item label="区域名称" prop="name">
          <el-input v-model="regionForm.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSubmit">提交</el-button>
          <el-button @click="handleCancel">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { ElMessage } from 'element-plus';
import { getRegionById, saveRegion } from '@/api/region';
import type { RegionData } from '@/api/region';

const router = useRouter();
const route = useRoute();
const regionForm = reactive<RegionData>({ name: '' });
const regionFormRef = ref();

const fetchRegion = async () => {
  const { id } = route.params;
  if (id) {
    const { data } = await getRegionById(id as string);
    Object.assign(regionForm, data);
  }
};

const handleSubmit = async () => {
  await saveRegion(regionForm);
  ElMessage.success('操作成功');
  router.push('/region/list');
};

const handleCancel = () => {
  router.push('/region/list');
};

onMounted(() => {
  fetchRegion();
});
</script>

<style scoped>
.region-form {
  padding: 20px;
}
</style> 