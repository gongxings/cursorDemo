import { createApp } from 'vue';
import { createPinia } from 'pinia';
import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css';
import * as ElementPlusIcons from '@element-plus/icons-vue';
import App from './App.vue';
import router from './router';
import './permission';

const app = createApp(App);

// 注册所有图标
for (const [key, component] of Object.entries(ElementPlusIcons)) {
  app.component(key, component);
}

app.use(createPinia())
   .use(router)
   .use(ElementPlus)
   .mount('#app'); 