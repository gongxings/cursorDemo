<template>
  <div class="house-view">
    <el-card>
      <h2>{{ house.title }}</h2>
      <p>区域: {{ house.district }}</p>
      <p>价格: {{ house.price }}元/月</p>
      <p>面积: {{ house.area }}㎡</p>
      <p>房型: {{ house.roomCount }}室</p>
      <p>状态: <el-tag :type="house.status === '已租' ? 'success' : 'info'">{{ house.status }}</el-tag></p>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { getHouseById } from '@/api/house';
import type { HouseData } from '@/api/house';

const route = useRoute();
const house = ref<HouseData | null>(null);

const fetchHouse = async () => {
  const { id } = route.params;
  if (id) {
    const { data } = await getHouseById(id as string);
    house.value = data;
  }
};

onMounted(() => {
  fetchHouse();
});
</script>

<style scoped>
.house-view {
  padding: 20px;
}
</style> 