import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { ProductActivityVO, ProductActivityForm, ProductActivityQuery } from '@/api/live/productActivity/types';

/**
 * 查询产品活动列表
 * @param query
 * @returns {*}
 */

export const listProductActivity = (query ?: ProductActivityQuery) : AxiosPromise<ProductActivityVO[]> => {
  return request({
    url: '/live/productActivity/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询产品活动详细
 * @param id
 */
export const getProductActivity = (id : string | number) : AxiosPromise<ProductActivityVO> => {
  return request({
    url: '/live/productActivity/' + id,
    method: 'get'
  });
};

/**
 * 新增产品活动
 * @param data
 */
export const addProductActivity = () => {
  return request({
    url: '/live/productActivity',
    method: 'post',
  });
};

/**
 * 修改产品活动
 * @param data
 */
export const updateProductActivity = () => {
  return request({
    url: '/live/productActivity',
    method: 'put',
  });
};

/**
 * 删除产品活动
 * @param id
 */
export const delProductActivity = (id : string | number | Array<string | number>) => {
  return request({
    url: '/live/productActivity/' + id,
    method: 'delete'
  });
};
