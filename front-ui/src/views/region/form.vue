<template>
  <div class="region-form">
    <el-card>
      <el-form :model="regionForm" ref="regionFormRef" label-width="80px">
        <el-form-item label="省份" prop="province">
          <el-input v-model="regionForm.province" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="城市" prop="city">
          <el-input v-model="regionForm.city" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="县/区" prop="county">
          <el-input v-model="regionForm.county" autocomplete="off"></el-input>
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
import { getRegionById, createRegion, updateRegion } from '@/api/region';

const router = useRouter();
const route = useRoute();
const regionForm = reactive<any>({ name: '' });
const regionFormRef = ref();

const fetchRegion = async () => {
  const { id } = route.params;
  if (id) {
    const { data } = await getRegionById(Number(id));
    Object.assign(regionForm, data);
  }
};

const handleSubmit = async () => {
  if (regionForm.id) {
    await updateRegion(regionForm);
  } else {
    await createRegion(regionForm);1
  }
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