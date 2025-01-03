import request from './request';

export interface DashboardData {
  totalHouses: number;
  todayNewHouses: number;
  totalUsers: number;
  activeUsers: number;
  avgPrice: number;
  avgArea: number;
  pricePerSquare: number;
  districtDistribution: Array<{
    name: string;
    value: number;
  }>;
  roomTypeDistribution: Array<{
    name: string;
    value: number;
  }>;
  priceTrend: Array<{
    date: string;
    avgPrice: number;
  }>;
}

export function getDashboardStats() {
  return request<DashboardData>({
    url: '/dashboard/stats',
    method: 'get'
  });
} 