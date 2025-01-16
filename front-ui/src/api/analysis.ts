import request from './request';
import {HouseQuery} from "@/api/house.ts";

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

export interface Report {
  title: '',
  type: '',
  district: '',
  period: ''
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

export function generateReport(data: Report) {
  return request({
    url: `/analysis/report`,
    method: 'post',
    data
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