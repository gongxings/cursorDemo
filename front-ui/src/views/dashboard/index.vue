<template>
  <div class="dashboard">
    <!-- 数据概览 -->
    <el-row :gutter="20" class="data-overview">
      <el-col :span="6">
        <el-card class="data-card">
          <template #header>
            <div class="card-header">
              <span>房源总数</span>
            </div>
          </template>
          <div class="card-body">
            <div class="number">{{ dashboardData.totalHouses }}</div>
            <div class="sub-text">今日新增: {{ dashboardData.todayNewHouses }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="data-card">
          <template #header>
            <div class="card-header">
              <span>用户总数</span>
            </div>
          </template>
          <div class="card-body">
            <div class="number">{{ dashboardData.totalUsers }}</div>
            <div class="sub-text">活跃用户: {{ dashboardData.activeUsers }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="data-card">
          <template #header>
            <div class="card-header">
              <span>平均租金</span>
            </div>
          </template>
          <div class="card-body">
            <div class="number">{{ dashboardData.avgPrice }}元/月</div>
            <div class="sub-text">{{ dashboardData.pricePerSquare }}元/㎡</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="data-card">
          <template #header>
            <div class="card-header">
              <span>平均面积</span>
            </div>
          </template>
          <div class="card-body">
            <div class="number">{{ dashboardData.avgArea }}㎡</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表展示 -->
    <el-row :gutter="20" class="charts-container">
      <el-col :span="12">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>区域分布</span>
            </div>
          </template>
          <div class="chart" ref="districtChartRef"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>房型分布</span>
            </div>
          </template>
          <div class="chart" ref="roomTypeChartRef"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row>
      <el-col :span="24">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>价格趋势（近7天）</span>
            </div>
          </template>
          <div class="chart" ref="trendChartRef"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, onUnmounted } from 'vue';
import * as echarts from 'echarts';
import { getDashboardStats } from '@/api/dashboard';

// 图表实例
let districtChart: echarts.ECharts | null = null;
let roomTypeChart: echarts.ECharts | null = null;
let trendChart: echarts.ECharts | null = null;

const districtChartRef = ref<HTMLElement>();
const roomTypeChartRef = ref<HTMLElement>();
const trendChartRef = ref<HTMLElement>();

// 数据
const dashboardData = reactive({
  totalHouses: 0,
  todayNewHouses: 0,
  totalUsers: 0,
  activeUsers: 0,
  avgPrice: 0,
  avgArea: 0,
  pricePerSquare: 0,
  districtDistribution: [],
  roomTypeDistribution: [],
  priceTrend: []
});

// 初始化图表
const initCharts = () => {
  if (districtChartRef.value) {
    districtChart = echarts.init(districtChartRef.value);
    districtChart.setOption({
      title: {
        text: '区域分布',
        left: 'center'
      },
      tooltip: {
        trigger: 'item',
        formatter: '{b}: {c} ({d}%)'
      },
      series: [{
        type: 'pie',
        radius: '60%',
        data: dashboardData.districtDistribution
      }]
    });
  }

  if (roomTypeChartRef.value) {
    roomTypeChart = echarts.init(roomTypeChartRef.value);
    roomTypeChart.setOption({
      title: {
        text: '房型分布',
        left: 'center'
      },
      tooltip: {
        trigger: 'item',
        formatter: '{b}: {c} ({d}%)'
      },
      series: [{
        type: 'pie',
        radius: ['40%', '70%'],
        data: dashboardData.roomTypeDistribution
      }]
    });
  }

  if (trendChartRef.value) {
    trendChart = echarts.init(trendChartRef.value);
    trendChart.setOption({
      title: {
        text: '价格趋势',
        left: 'center'
      },
      tooltip: {
        trigger: 'axis'
      },
      xAxis: {
        type: 'category',
        data: dashboardData.priceTrend.map(item => item.date)
      },
      yAxis: {
        type: 'value',
        name: '元/月'
      },
      series: [{
        data: dashboardData.priceTrend.map(item => item.avgPrice),
        type: 'line',
        smooth: true
      }]
    });
  }
};

// 获取数据
const fetchData = async () => {
  try {
    const { data } = await getDashboardStats();
    Object.assign(dashboardData, data);
    initCharts();
  } catch (error) {
    console.error('获取首页数据失败:', error);
  }
};

// 窗口大小变化时重绘图表
const handleResize = () => {
  districtChart?.resize();
  roomTypeChart?.resize();
  trendChart?.resize();
};

onMounted(() => {
  fetchData();
  window.addEventListener('resize', handleResize);
});

onUnmounted(() => {
  window.removeEventListener('resize', handleResize);
  districtChart?.dispose();
  roomTypeChart?.dispose();
  trendChart?.dispose();
});
</script>

<style lang="scss" scoped>
.dashboard {
  .data-overview {
    margin-bottom: 20px;

    .data-card {
      .card-body {
        text-align: center;
        padding: 20px 0;

        .number {
          font-size: 24px;
          font-weight: bold;
          color: #409EFF;
          margin-bottom: 10px;
        }

        .sub-text {
          color: #666;
          font-size: 14px;
        }
      }
    }
  }

  .charts-container {
    margin-bottom: 20px;

    .chart-card {
      .chart {
        height: 300px;
      }
    }
  }
}
</style> 