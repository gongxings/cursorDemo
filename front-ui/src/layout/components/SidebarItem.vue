<template>
  <template v-if="!item.meta?.hidden">
    <el-sub-menu v-if="item.children && item.children.length > 1" :index="item.path">
      <template #title>
        <el-icon><component :is="item.meta?.icon" /></el-icon>
        <span>{{ item.meta?.title }}</span>
      </template>
      <sidebar-item
        v-for="child in item.children"
        :key="child.path"
        :item="child"
        :base-path="item.path"
      />
    </el-sub-menu>

    <template v-else>
      <el-menu-item
        v-if="item.children"
        :index="item.children[0].path"
      >
        <el-icon><component :is="item.children[0].meta?.icon || item.meta?.icon" /></el-icon>
        <template #title>{{ item.children[0].meta?.title || item.meta?.title }}</template>
      </el-menu-item>
      <el-menu-item
        v-else
        :index="item.path"
      >
        <el-icon><component :is="item.meta?.icon" /></el-icon>
        <template #title>{{ item.meta?.title }}</template>
      </el-menu-item>
    </template>
  </template>
</template>

<script setup lang="ts">
import { RouteRecordRaw } from 'vue-router';
import * as ElementPlusIcons from '@element-plus/icons-vue';

defineProps<{
  item: RouteRecordRaw;
  basePath?: string;
}>();
</script> 