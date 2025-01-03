import router from './router';
import { useUserStore } from './stores/user';
import { ElMessage } from 'element-plus';

const whiteList = ['/login'];

router.beforeEach(async (to, from, next) => {
  const userStore = useUserStore();
  
  if (userStore.token) {
    if (to.path === '/login') {
      next('/');
    } else {
      if (!userStore.userInfo) {
        try {
          await userStore.getInfo();
          next();
        } catch (error) {
          await userStore.logout();
          ElMessage.error('获取用户信息失败');
          next('/login');
        }
      } else {
        next();
      }
    }
  } else {
    if (whiteList.includes(to.path)) {
      next();
    } else {
      next('/login');
    }
  }
}); 