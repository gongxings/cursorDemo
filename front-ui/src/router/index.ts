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
        path: 'list',
        name: 'HouseList',
        component: () => import('@/views/house/list.vue'),
        meta: { title: '房源列表' }
      },
      {
        path: 'add',
        name: 'HouseAdd',
        component: () => import('@/views/house/form.vue'),
        meta: { title: '添加房源' ,hidden: true}
      },
      {
        path: 'edit/:id',
        name: 'HouseEdit',
        component: () => import('@/views/house/form.vue'),
        meta: { title: '编辑房源', hidden: true }
      },{
        path: 'view/:id',
        name: 'HouseView',
        component: () => import('@/views/house/view.vue'),
        meta: { title: '查看房源', hidden: true }
      }
    ]
  },
  {
    path: '/analysis',
    component: Layout,
    meta: { title: '数据分析', icon: 'DataLine' },
    children: [
      {
        path: 'market',
        name: 'MarketAnalysis',
        component: () => import('@/views/analysis/market.vue'),
        meta: { title: '市场分析' }
      },
      {
        path: 'price',
        name: 'PriceAnalysis',
        component: () => import('@/views/analysis/price.vue'),
        meta: { title: '价格分析' }
      },
      {
        path: 'reports',
        name: 'Reports',
        component: () => import('@/views/analysis/reports.vue'),
        meta: { title: '分析报告' }
      }
    ]
  },
  {
    path: '/user',
    component: Layout,
    meta: { title: '用户管理', icon: 'User' },
    children: [
      {
        path: 'list',
        name: 'UserList',
        component: () => import('@/views/user/list.vue'),
        meta: { title: '用户列表' }
      },
      {
        path: 'add',
        name: 'UserAdd',
        component: () => import('@/views/user/form.vue'),
        meta: { title: '添加用户', hidden: true }
      },
      {
        path: 'edit/:id',
        name: 'UserEdit',
        component: () => import('@/views/user/form.vue'),
        meta: { title: '编辑用户', hidden: true }
      },
      {
        path: 'register',
        name: 'UserRegister',
        component: () => import('@/views/user/Register.vue'),
        meta: { title: '添加用户', hidden: true }
      },
    ]
  },
  {
    path: '/region',
    component: Layout,
    meta: { title: '区域管理', icon: 'Location' },
    children: [
      {
        path: 'list',
        name: 'RegionList',
        component: () => import('@/views/region/list.vue'),
        meta: { title: '区域列表' }
      },
      {
        path: 'add',
        name: 'RegionAdd',
        component: () => import('@/views/region/form.vue'),
        meta: { title: '添加区域', hidden: true }
      },
      {
        path: 'edit/:id',
        name: 'RegionEdit',
        component: () => import('@/views/region/form.vue'),
        meta: { title: '编辑区域', hidden: true }
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