import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { ProductAnalyseVO, ProductAnalyseForm, ProductAnalyseQuery } from '@/api/live/productAnalyse/types';

/**
 * 查询统计分析列表
 * @param query
 * @returns {*}
 */

export const listProductAnalyse = (query ?: ProductAnalyseQuery) : AxiosPromise<ProductAnalyseVO[]> => {
  return request({
    url: '/live/productAnalyse/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询统计分析详细
 * @param id
 */
export const getProductAnalyse = (id : string | number) : AxiosPromise<ProductAnalyseVO> => {
  return request({
    url: '/live/productAnalyse/' + id,
    method: 'get'
  });
};

/**
 * 新增统计分析
 * @param data
 */
export const addProductAnalyse = () => {
  return request({
    url: '/live/productAnalyse',
    method: 'post',
  });
};

/**
 * 修改统计分析
 * @param data
 */
export const updateProductAnalyse = () => {
  return request({
    url: '/live/productAnalyse',
    method: 'put'
  });
};

/**
 * 删除统计分析
 * @param id
 */
export const delProductAnalyse = (id : string | number | Array<string | number>) => {
  return request({
    url: '/live/productAnalyse/' + id,
    method: 'delete'
  });
};
