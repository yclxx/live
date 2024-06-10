import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { ProductVO, ProductForm, ProductQuery } from '@/api/live/product/types';

/**
 * 查询产品管理列表
 * @param query
 * @returns {*}
 */

export const listProduct = (query ?: ProductQuery) : AxiosPromise<ProductVO[]> => {
  return request({
    url: '/live/product/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询产品管理详细
 * @param productCode
 */
export const getProduct = (productCode : string | number) : AxiosPromise<ProductVO> => {
  return request({
    url: '/live/product/' + productCode,
    method: 'get'
  });
};

/**
 * 新增产品管理
 * @param data
 */
export const addProduct = () => {
  return request({
    url: '/live/product',
    method: 'post',
  });
};

/**
 * 修改产品管理
 * @param data
 */
export const updateProduct = (data : ProductForm) => {
  return request({
    url: '/live/product',
    method: 'put',
    data: data
  });
};

/**
 * 修改产品管理
 * @param data
 */
export const updateProductTop = (productCode : string | number) => {
  return request({
    url: '/live/product/editTop/' + productCode,
    method: 'put'
  });
};

/**
 * 删除产品管理
 * @param productCode
 */
export const delProduct = (productCode : string | number | Array<string | number>) => {
  return request({
    url: '/live/product/' + productCode,
    method: 'delete'
  });
};
