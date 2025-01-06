import request from './request';

export const getRegionById = (id: number) => {
    return request({
        url: `/regions/${id}`,
        method: 'get'
    });
};

export const getAllRegions = () => {
    return request({
        url: '/regions',
        method: 'get'
    });
};

export const createRegion = (data: any) => {
    return request({
        url: '/regions',
        method: 'post',
        data
    });
};

export const updateRegion = (data: any) => {
    return request({
        url: '/regions',
        method: 'put',
        data
    });
};

export const deleteRegion = (id: number) => {
    return request({
        url: `/regions/${id}`,
        method: 'delete'
    });
}; 