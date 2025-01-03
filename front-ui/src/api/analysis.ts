import request from './request';

export interface MarketAnalysis {
  district: string;
  avgPrice: number;
  maxPrice: number;
  minPrice: number;
  totalCount: number;
  avgArea: number;
  pricePerSquareMeter: number;
}

export interface PriceAnalysis {
  districtAvgPrices: Record<string, number>;
  typeAvgPrices: Record<string, number>;
  roomCountAvgPrices: Record<number, number>;
  overallAvgPrice: number;
  priceGrowthRate: number;
}

export function getMarketAnalysis(district: string) {
  return request({
    url: `/analysis/market/${district}`,
    method: 'get'
  });
}

export function getPriceAnalysis() {
  return request({
    url: '/analysis/price',
    method: 'get'
  });
}

export function generateReport(type: string) {
  return request({
    url: `/analysis/report/${type}`,
    method: 'post'
  });
}

export function getReportHistory() {
  return request({
    url: '/analysis/reports',
    method: 'get'
  });
}

export function exportReport(reportId: number) {
  return request({
    url: `/analysis/report/export/${reportId}`,
    method: 'get',
    responseType: 'blob'
  });
} 