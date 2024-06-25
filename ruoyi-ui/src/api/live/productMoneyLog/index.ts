import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { ProductMoneyLogVO, ProductMoneyLogForm, ProductMoneyLogQuery } from '@/api/live/productMoneyLog/types';

/**
 * 查询资金流向列表
 * @param query
 * @returns {*}
 */

export const listProductMoneyLog = (query?: ProductMoneyLogQuery): AxiosPromise<ProductMoneyLogVO[]> => {
  return request({
    url: '/live/productMoneyLog/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询资金流向详细
 * @param id
 */
export const getProductMoneyLog = (id: string | number): AxiosPromise<ProductMoneyLogVO> => {
  return request({
    url: '/live/productMoneyLog/' + id,
    method: 'get'
  });
};

/**
 * 新增资金流向
 * @param data
 */
export const addProductMoneyLog = (data: ProductMoneyLogForm) => {
  return request({
    url: '/live/productMoneyLog',
    method: 'post',
    data: data
  });
};

/**
 * 修改资金流向
 * @param data
 */
export const updateProductMoneyLog = (data: ProductMoneyLogForm) => {
  return request({
    url: '/live/productMoneyLog',
    method: 'put',
    data: data
  });
};

/**
 * 删除资金流向
 * @param id
 */
export const delProductMoneyLog = (id: string | number | Array<string | number>) => {
  return request({
    url: '/live/productMoneyLog/' + id,
    method: 'delete'
  });
};
