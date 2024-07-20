import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { ActivityVO, ActivityForm, ActivityQuery } from '@/api/live/activity/types';

/**
 * 查询活动管理列表
 * @param query
 * @returns {*}
 */

export const listActivity = (query?: ActivityQuery): AxiosPromise<ActivityVO[]> => {
  return request({
    url: '/live/activity/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询活动管理详细
 * @param activityId
 */
export const getActivity = (activityId: string | number): AxiosPromise<ActivityVO> => {
  return request({
    url: '/live/activity/' + activityId,
    method: 'get'
  });
};

/**
 * 新增活动管理
 * @param data
 */
export const addActivity = (data: ActivityForm) => {
  return request({
    url: '/live/activity',
    method: 'post',
    data: data
  });
};

/**
 * 修改活动管理
 * @param data
 */
export const updateActivity = (data: ActivityForm) => {
  return request({
    url: '/live/activity',
    method: 'put',
    data: data
  });
};

/**
 * 删除活动管理
 * @param activityId
 */
export const delActivity = (activityId: string | number | Array<string | number>) => {
  return request({
    url: '/live/activity/' + activityId,
    method: 'delete'
  });
};
