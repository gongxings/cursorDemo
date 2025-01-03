<template>
  <div class="price-analysis">
    <!-- 数据概览 -->
    <el-row :gutter="20" class="data-overview">
      <el-col :span="8">
        <el-card class="data-card">
          <template #header>
            <div class="card-header">
              <span>整体均价</span>
            </div>
          </template>
          <div class="card-body">
            <div class="number">{{ priceData.overallAvgPrice }}</div>
            <div class="label">元/月</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="data-card">
          <template #header>
            <div class="card-header">
              <span>价格增长率</span>
            </div>
          </template>
          <div class="card-body">
            <div class="number" :class="{ 'up': priceData.priceGrowthRate > 0 }">
              {{ priceData.priceGrowthRate > 0 ? '+' : '' }}{{ priceData.priceGrowthRate }}%
            </div>
            <div class="label">同比上月</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="data-card">
          <template #header>
            <div class="card-header">
              <span>价格区间</span>
            </div>
          </template>
          <div class="card-body">
            <div class="price-range">
              <span>{{ minPrice }}</span>
              <span class="separator">-</span>
              <span>{{ maxPrice }}</span>
            </div>
            <div class="label">元/月</div>
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
              <span>各区域均价对比</span>
            </div>
          </template>
          <div class="chart" ref="districtPriceChartRef"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>房型均价对比</span>
            </div>
          </template>
          <div class="chart" ref="roomTypePriceChartRef"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="charts-container">
      <el-col :span="24">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>价格趋势分析</span>
            </div>
          </template>
          <div class="chart" ref="priceTrendChartRef"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, onUnmounted } from 'vue';
import { getPriceAnalysis } from '@/api/analysis';
import type { PriceAnalysis } from '@/api/analysis';
import * as echarts from 'echarts';

const priceData = reactive<PriceAnalysis>({
  districtAvgPrices: {},
  typeAvgPrices: {},
  roomCountAvgPrices: {},
  overallAvgPrice: 0,
  priceGrowthRate: 0
});

const minPrice = ref(0);
const maxPrice = ref(0);

// 图表实例
let districtPriceChart: echarts.ECharts | null = null;
let roomTypePriceChart: echarts.ECharts | null = null;
let priceTrendChart: echarts.ECharts | null = null;

const districtPriceChartRef = ref<HTMLElement>();
const roomTypePriceChartRef = ref<HTMLElement>();
const priceTrendChartRef = ref<HTMLElement>();

// 初始化图表
const initCharts = () => {
  if (districtPriceChartRef.value) {
    districtPriceChart = echarts.init(districtPriceChartRef.value);
    districtPriceChart.setOption({
      title: {
        text: '各区域均价对比',
        left: 'center'
      },
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'shadow'
        }
      },
      xAxis: {
        type: 'category',
        data: ['朝阳区', '海淀区', '东城区', '西城区']
      },
      yAxis: {
        type: 'value',
        name: '元/月'
      },
      series: [{
        data: [6000, 7000, 8000, 9000],
        type: 'bar',
        showBackground: true,
        backgroundStyle: {
          color: 'rgba(180, 180, 180, 0.2)'
        }
      }]
    });
  }

  if (roomTypePriceChartRef.value) {
    roomTypePriceChart = echarts.init(roomTypePriceChartRef.value);
    roomTypePriceChart.setOption({
      title: {
        text: '房型均价对比',
        left: 'center'
      },
      tooltip: {
        trigger: 'axis'
      },
      radar: {
        indicator: [
          { name: '一室', max: 10000 },
          { name: '两室', max: 10000 },
          { name: '三室', max: 10000 },
          { name: '四室及以上', max: 10000 }
        ]
      },
      series: [{
        type: 'radar',
        data: [{
          value: [4200, 6500, 8500, 10000],
          name: '房型均价'
        }]
      }]
    });
  }

  if (priceTrendChartRef.value) {
    priceTrendChart = echarts.init(priceTrendChartRef.value);
    priceTrendChart.setOption({
      title: {
        text: '价格趋势分析',
        left: 'center'
      },
      tooltip: {
        trigger: 'axis'
      },
      xAxis: {
        type: 'category',
        data: ['1月', '2月', '3月', '4月', '5月', '6月']
      },
      yAxis: {
        type: 'value',
        name: '元/月'
      },
      series: [{
        data: [5000, 5200, 5400, 5300, 5600, 5800],
        type: 'line',
        smooth: true,
        markPoint: {
          data: [
            { type: 'max', name: '最大值' },
            { type: 'min', name: '最小值' }
          ]
        },
        markLine: {
          data: [
            { type: 'average', name: '平均值' }
          ]
        }
      }]
    });
  }
};

// 获取价格分析数据
const fetchPriceData = async () => {
  try {
    const { data } = await getPriceAnalysis();
    Object.assign(priceData, data);
    // 更新图表数据
    updateCharts(data);
  } catch (error) {
    console.error('获取价格分析数据失败:', error);
  }
};

// 更新图表数据
const updateCharts = (data: PriceAnalysis) => {
  // 这里根据实际数据更新图表
  // 示例代码仅供参考
};

// 窗口大小变化时重绘图表
const handleResize = () => {
  districtPriceChart?.resize();
  roomTypePriceChart?.resize();
  priceTrendChart?.resize();
};

onMounted(() => {
  initCharts();
  fetchPriceData();
  window.addEventListener('resize', handleResize);
});

onUnmounted(() => {
  window.removeEventListener('resize', handleResize);
  districtPriceChart?.dispose();
  roomTypePriceChart?.dispose();
  priceTrendChart?.dispose();
});
</script>

<style lang="scss" scoped>
.price-analysis {
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

          &.up {
            color: #67C23A;
          }
        }

        .label {
          color: #666;
        }

        .price-range {
          font-size: 20px;
          color: #409EFF;
          margin-bottom: 10px;

          .separator {
            margin: 0 10px;
            color: #666;
          }
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