import request from './request';

export const getRegionById = (id: number) => {
    return request({
        url: `/api/regions/${id}`,
        method: 'get'
    });
};

export const getAllRegions = () => {
    return request({
        url: '/api/regions',
        method: 'get'
    });
};

export const createRegion = (data: any) => {
    return request({
        url: '/api/regions',
        method: 'post',
        data
    });
};

export const updateRegion = (data: any) => {
    return request({
        url: '/api/regions',
        method: 'put',
        data
    });
};

export const deleteRegion = (id: number) => {
    return request({
        url: `/api/regions/${id}`,
        method: 'delete'
    });
}; 