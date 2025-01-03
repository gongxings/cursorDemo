<template>
  <div class="market-analysis">
    <!-- 筛选区域 -->
    <el-card class="filter-card">
      <el-form :model="filterForm" inline>
        <el-form-item label="区域">
          <el-select v-model="filterForm.district" placeholder="请选择区域" clearable @change="handleDistrictChange">
            <el-option
              v-for="item in districtOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
      </el-form>
    </el-card>

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
            <div class="number">{{ marketData.totalCount }}</div>
            <div class="label">套</div>
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
            <div class="number">{{ marketData.avgPrice }}</div>
            <div class="label">元/月</div>
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
            <div class="number">{{ marketData.avgArea }}</div>
            <div class="label">㎡</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="data-card">
          <template #header>
            <div class="card-header">
              <span>均价</span>
            </div>
          </template>
          <div class="card-body">
            <div class="number">{{ marketData.pricePerSquareMeter }}</div>
            <div class="label">元/㎡</div>
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
              <span>各区域房源数量分布</span>
            </div>
          </template>
          <div class="chart" ref="districtChartRef"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>租金区间分布</span>
            </div>
          </template>
          <div class="chart" ref="priceChartRef"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="charts-container">
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
      <el-col :span="12">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>租金趋势</span>
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
import { getMarketAnalysis } from '@/api/analysis';
import type { MarketAnalysis } from '@/api/analysis';
import * as echarts from 'echarts';

const filterForm = reactive({
  district: ''
});

const districtOptions = [
  { value: '朝阳区', label: '朝阳区' },
  { value: '海淀区', label: '海淀区' },
  { value: '东城区', label: '东城区' },
  { value: '西城区', label: '西城区' }
];

const marketData = reactive<MarketAnalysis>({
  district: '',
  avgPrice: 0,
  maxPrice: 0,
  minPrice: 0,
  totalCount: 0,
  avgArea: 0,
  pricePerSquareMeter: 0
});

// 图表实例
let districtChart: echarts.ECharts | null = null;
let priceChart: echarts.ECharts | null = null;
let roomTypeChart: echarts.ECharts | null = null;
let trendChart: echarts.ECharts | null = null;

const districtChartRef = ref<HTMLElement>();
const priceChartRef = ref<HTMLElement>();
const roomTypeChartRef = ref<HTMLElement>();
const trendChartRef = ref<HTMLElement>();

// 初始化图表
const initCharts = () => {
  if (districtChartRef.value) {
    districtChart = echarts.init(districtChartRef.value);
    districtChart.setOption({
      title: {
        text: '各区域房源数量分布',
        left: 'center'
      },
      tooltip: {
        trigger: 'item',
        formatter: '{b}: {c} ({d}%)'
      },
      series: [{
        type: 'pie',
        radius: '60%',
        data: [
          { value: 335, name: '朝阳区' },
          { value: 310, name: '海淀区' },
          { value: 234, name: '东城区' },
          { value: 135, name: '西城区' }
        ]
      }]
    });
  }

  if (priceChartRef.value) {
    priceChart = echarts.init(priceChartRef.value);
    priceChart.setOption({
      title: {
        text: '租金区间分布',
        left: 'center'
      },
      tooltip: {
        trigger: 'axis'
      },
      xAxis: {
        type: 'category',
        data: ['<3000', '3000-5000', '5000-8000', '8000-10000', '>10000']
      },
      yAxis: {
        type: 'value'
      },
      series: [{
        data: [120, 200, 150, 80, 70],
        type: 'bar'
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
        data: [
          { value: 435, name: '一室' },
          { value: 310, name: '两室' },
          { value: 234, name: '三室' },
          { value: 135, name: '四室及以上' }
        ]
      }]
    });
  }

  if (trendChartRef.value) {
    trendChart = echarts.init(trendChartRef.value);
    trendChart.setOption({
      title: {
        text: '租金趋势',
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
        type: 'value'
      },
      series: [{
        data: [5000, 5200, 5400, 5300, 5600, 5800],
        type: 'line',
        smooth: true
      }]
    });
  }
};

// 获取市场分析数据
const fetchMarketData = async (district: string = '') => {
  try {
    const { data } = await getMarketAnalysis(district);
    Object.assign(marketData, data);
    // 更新图表数据
    updateCharts(data);
  } catch (error) {
    console.error('获取市场分析数据失败:', error);
  }
};

// 更新图表数据
const updateCharts = (data: MarketAnalysis) => {
  // 这里根据实际数据更新图表
  // 示例代码仅供参考
};

// 区域变化处理
const handleDistrictChange = (value: string) => {
  fetchMarketData(value);
};

// 窗口大小变化时重绘图表
const handleResize = () => {
  districtChart?.resize();
  priceChart?.resize();
  roomTypeChart?.resize();
  trendChart?.resize();
};

onMounted(() => {
  initCharts();
  fetchMarketData();
  window.addEventListener('resize', handleResize);
});

onUnmounted(() => {
  window.removeEventListener('resize', handleResize);
  districtChart?.dispose();
  priceChart?.dispose();
  roomTypeChart?.dispose();
  trendChart?.dispose();
});
</script>

<style lang="scss" scoped>
.market-analysis {
  .filter-card {
    margin-bottom: 20px;
  }

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

        .label {
          color: #666;
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