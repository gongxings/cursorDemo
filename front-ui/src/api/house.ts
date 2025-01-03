import request from './request';

export interface HouseQuery {
  district?: string;
  minPrice?: number;
  maxPrice?: number;
  minArea?: number;
  maxArea?: number;
  roomCount?: number;
  type?: string;
  status?: string;
}

export interface HouseData {
  id?: number;
  title: string;
  address: string;
  district: string;
  price: number;
  area: number;
  roomCount: number;
  floor: number;
  totalFloor: number;
  buildYear?: number;
  type: string;
  status?: string;
  features?: string[];
  facilities?: string[];
  description?: string;
}

export function getHouseList() {
  return request({
    url: '/houses',
    method: 'get'
  });
}

export function searchHouses(data: HouseQuery) {
  return request({
    url: '/houses/search',
    method: 'post',
    data
  });
}

export function getHouseById(id: number) {
  return request({
    url: `/houses/${id}`,
    method: 'get'
  });
}

export function createHouse(data: HouseData) {
  return request({
    url: '/houses',
    method: 'post',
    data
  });
}

export function updateHouse(id: number, data: HouseData) {
  return request({
    url: `/houses/${id}`,
    method: 'put',
    data
  });
}

export function deleteHouse(id: number) {
  return request({
    url: `/houses/${id}`,
    method: 'delete'
  });
} 