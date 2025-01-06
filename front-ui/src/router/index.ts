import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router';
import Layout from '@/layout/index.vue';

const routes: Array<RouteRecordRaw> = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue'),
    meta: { title: '登录', hidden: true }
  },
  {
    path: '/',
    component: Layout,
    meta: { title: '首页', icon: 'House' },
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard', 
        component: () => import('@/views/dashboard/index.vue'),
        meta: { title: '首页' }
      }
    ]
  },
  {
    path: '/house',
    component: Layout,
    meta: { title: '房源管理', icon: 'Office' },
    children: [
      {
        path: '/house/list',
        name: 'HouseList',
        component: () => import('@/views/house/list.vue'),
        meta: { title: '房源列表' }
      },
      {
        path: '/house/add',
        name: 'HouseAdd',
        component: () => import('@/views/house/form.vue'),
        meta: { title: '添加房源' }
      },
      {
        path: '/house/edit/:id',
        name: 'HouseEdit',
        component: () => import('@/views/house/form.vue'),
        meta: { title: '编辑房源', hidden: true }
      }
    ]
  },
  {
    path: '/analysis',
    component: Layout,
    meta: { title: '数据分析', icon: 'DataLine' },
    children: [
      {
        path: '/analysis/market',
        name: 'MarketAnalysis',
        component: () => import('@/views/analysis/market.vue'),
        meta: { title: '市场分析' }
      },
      {
        path: '/analysis/price',
        name: 'PriceAnalysis',
        component: () => import('@/views/analysis/price.vue'),
        meta: { title: '价格分析' }
      },
      {
        path: '/analysis/reports',
        name: 'Reports',
        component: () => import('@/views/analysis/reports.vue'),
        meta: { title: '分析报告' }
      }
    ]
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

// 路由守卫
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token');
  
  // 设置页面标题
  document.title = `${to.meta.title || '房产管理系统'}`;
  
  // 如果是登录页面，直接放行
  if (to.path === '/login') {
    if (token) {
      next('/dashboard');
    } else {
      next();
    }
    return;
  }
  
  // 其他页面检查是否有token
  if (!token) {
    next('/login');
    return;
  }
  
  next();
});

export default router; 