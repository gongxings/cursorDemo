import request from './request';

// 获取所有区域
export function getRegions() {
  return request({
    url: '/regions',
    method: 'get'
  });
}

// 根据ID获取区域
export function getRegionById(id: number) {
  return request({
    url: `/regions/${id}`,
    method: 'get'
  });
}

// 创建新区域
export function createRegion(data: any) {
  return request({
    url: '/regions',
    method: 'post',
    data
  });
}

// 更新区域信息
export function updateRegion(data: any) {
  return request({
    url: '/regions',
    method: 'put',
    data
  });
}

// 删除区域
export function deleteRegion(id: number) {
  return request({
    url: `/regions/${id}`,
    method: 'delete'
  });
} 