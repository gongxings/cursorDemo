<template>
  <div class="house-form">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>{{ isEdit ? '编辑房源' : '添加房源' }}</span>
        </div>
      </template>
      
      <el-form
        ref="formRef"
        :model="formData"
        :rules="rules"
        label-width="100px"
        class="house-form"
      >
        <el-form-item label="标题" prop="title">
          <el-input v-model="formData.title" placeholder="请输入房源标题" />
        </el-form-item>

        <el-form-item label="区域" prop="district">
          <el-select v-model="formData.district" placeholder="请选择区域">
            <el-option
              v-for="item in districtOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="详细地址" prop="address">
          <el-input v-model="formData.address" placeholder="请输入详细地址" />
        </el-form-item>

        <el-form-item label="月租金" prop="price">
          <el-input-number
            v-model="formData.price"
            :min="0"
            :precision="2"
            :step="100"
            placeholder="请输入月租金"
          />
          <span class="unit">元/月</span>
        </el-form-item>

        <el-form-item label="面积" prop="area">
          <el-input-number
            v-model="formData.area"
            :min="0"
            :precision="2"
            :step="1"
            placeholder="请输入面积"
          />
          <span class="unit">㎡</span>
        </el-form-item>

        <el-form-item label="房型" prop="roomCount">
          <el-select v-model="formData.roomCount" placeholder="请选择房型">
            <el-option
              v-for="item in roomCountOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="楼层" prop="floor">
          <el-input-number
            v-model="formData.floor"
            :min="1"
            placeholder="所在楼层"
            style="width: 180px"
          />
          <span class="mx-2">/</span>
          <el-input-number
            v-model="formData.totalFloor"
            :min="1"
            placeholder="总楼层"
            style="width: 180px"
          />
        </el-form-item>

        <el-form-item label="建筑年代" prop="buildYear">
          <el-date-picker
            v-model="formData.buildYear"
            type="year"
            placeholder="请选择建筑年代"
          />
        </el-form-item>

        <el-form-item label="房屋类型" prop="type">
          <el-radio-group v-model="formData.type">
            <el-radio label="普通住宅">普通住宅</el-radio>
            <el-radio label="公寓">公寓</el-radio>
            <el-radio label="别墅">别墅</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="房屋特色" prop="features">
          <el-checkbox-group v-model="formData.features">
            <el-checkbox label="精装修">精装修</el-checkbox>
            <el-checkbox label="拎包入住">拎包入住</el-checkbox>
            <el-checkbox label="南北通透">南北通透</el-checkbox>
            <el-checkbox label="采光好">采光好</el-checkbox>
            <el-checkbox label="近地铁">近地铁</el-checkbox>
          </el-checkbox-group>
        </el-form-item>

        <el-form-item label="配套设施" prop="facilities">
          <el-checkbox-group v-model="formData.facilities">
            <el-checkbox label="冰箱">冰箱</el-checkbox>
            <el-checkbox label="洗衣机">洗衣机</el-checkbox>
            <el-checkbox label="空调">空调</el-checkbox>
            <el-checkbox label="热水器">热水器</el-checkbox>
            <el-checkbox label="床">床</el-checkbox>
            <el-checkbox label="衣柜">衣柜</el-checkbox>
            <el-checkbox label="电视">电视</el-checkbox>
            <el-checkbox label="宽带">宽带</el-checkbox>
          </el-checkbox-group>
        </el-form-item>

        <el-form-item label="房源描述" prop="description">
          <el-input
            v-model="formData.description"
            type="textarea"
            :rows="4"
            placeholder="请输入房源描述"
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleSubmit" :loading="loading">
            {{ isEdit ? '保存' : '提交' }}
          </el-button>
          <el-button @click="handleCancel">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import type { FormInstance } from 'element-plus';
import { getHouseById, createHouse, updateHouse } from '@/api/house';
import type { HouseData } from '@/api/house';

const route = useRoute();
const router = useRouter();
const formRef = ref<FormInstance>();
const loading = ref(false);
const isEdit = ref(false);

const formData = reactive<HouseData>({
  title: '',
  address: '',
  district: '',
  price: 0,
  area: 0,
  roomCount: 1,
  floor: 1,
  totalFloor: 1,
  buildYear: '',
  type: '普通住宅',
  features: [],
  facilities: [],
  description: ''
});

const rules = {
  title: [{ required: true, message: '请输入房源标题', trigger: 'blur' }],
  district: [{ required: true, message: '请选择区域', trigger: 'change' }],
  address: [{ required: true, message: '请输入详细地址', trigger: 'blur' }],
  price: [{ required: true, message: '请输入月租金', trigger: 'blur' }],
  area: [{ required: true, message: '请输入面积', trigger: 'blur' }],
  roomCount: [{ required: true, message: '请选择房型', trigger: 'change' }],
  floor: [{ required: true, message: '请输入所在楼层', trigger: 'blur' }],
  totalFloor: [{ required: true, message: '请输入总楼层', trigger: 'blur' }],
  type: [{ required: true, message: '请选择房屋类型', trigger: 'change' }]
};

// 区域选项
const districtOptions = [
  { value: '朝阳区', label: '朝阳区' },
  { value: '海淀区', label: '海淀区' },
  { value: '东城区', label: '东城区' },
  { value: '西城区', label: '西城区' }
];

// 房型选项
const roomCountOptions = [
  { value: 1, label: '一室' },
  { value: 2, label: '两室' },
  { value: 3, label: '三室' },
  { value: 4, label: '四室及以上' }
];

// 获取房源详情
const fetchHouseDetail = async (id: number) => {
  try {
    const { data } = await getHouseById(id);
    Object.assign(formData, data);
  } catch (error) {
    console.error('获取房源详情失败:', error);
    ElMessage.error('获取房源详情失败');
  }
};

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return;
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true;
      try {
        if (isEdit.value) {
          await updateHouse(Number(route.params.id), formData);
          ElMessage.success('更新成功');
        } else {
          await createHouse(formData);
          ElMessage.success('添加成功');
        }
        router.push('/house/list');
      } catch (error) {
        console.error('保存失败:', error);
        ElMessage.error('保存失败');
      } finally {
        loading.value = false;
      }
    }
  });
};

// 取消
const handleCancel = () => {
  router.back();
};

onMounted(() => {
  const id = route.params.id;
  if (id) {
    isEdit.value = true;
    fetchHouseDetail(Number(id));
  }
});
</script>

<style lang="scss" scoped>
.house-form {
  .unit {
    margin-left: 10px;
    color: #666;
  }

  .mx-2 {
    margin: 0 8px;
  }

  :deep(.el-form-item) {
    max-width: 800px;
  }
}
</style> 