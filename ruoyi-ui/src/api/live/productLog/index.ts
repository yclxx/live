import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { ProductLogVO, ProductLogForm, ProductLogQuery } from '@/api/live/productLog/types';

/**
 * 查询产品记录列表
 * @param query
 * @returns {*}
 */

export const listProductLog = (query ?: ProductLogQuery) : AxiosPromise<ProductLogVO[]> => {
  return request({
    url: '/live/productLog/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询产品记录详细
 * @param id
 */
export const getProductLog = (id : string | number) : AxiosPromise<ProductLogVO> => {
  return request({
    url: '/live/productLog/' + id,
    method: 'get'
  });
};

/**
 * 新增产品记录
 * @param data
 */
export const addProductLog = () => {
  return request({
    url: '/live/productLog',
    method: 'post',
  });
};

/**
 * 修改产品记录
 * @param data
 */
export const updateProductLog = (data : ProductLogForm) => {
  return request({
    url: '/live/productLog',
    method: 'put',
    data: data
  });
};

/**
 * 删除产品记录
 * @param id
 */
export const delProductLog = (id : string | number | Array<string | number>) => {
  return request({
    url: '/live/productLog/' + id,
    method: 'delete'
  });
};
