export interface ActivityVO {
  /**
   * 活动编号
   */
  activityId : string | number;

  /**
   * 活动名称
   */
  activityName : string;

  /**
   * 活动说明
   */
  activityRemark : string;

  /**
   * 状态
   */
  status : string;

  /**
   * 策略类
   */
  className : string;

  /**
   * 创建时间
   */
  createTime : string;

  /**
   * 更新时间
   */
  updateTime : string;

}

export interface ActivityForm extends BaseEntity {
  /**
   * 活动编号
   */
  activityId ?: string | number;

  /**
   * 活动名称
   */
  activityName ?: string;

  /**
   * 活动说明
   */
  activityRemark ?: string;

  /**
   * 状态
   */
  status ?: string;

  /**
     * 策略类
     */
  className : string;
}

export interface ActivityQuery extends PageQuery {

  /**
   * 活动编号
   */
  activityId ?: string | number;

  /**
   * 活动名称
   */
  activityName ?: string;

  /**
   * 活动说明
   */
  activityRemark ?: string;

  /**
   * 状态
   */
  status ?: string;

  /**
   * 日期范围参数
   */
  params ?: any;
}
