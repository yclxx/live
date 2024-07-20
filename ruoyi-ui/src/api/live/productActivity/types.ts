export interface ProductActivityVO {
  /**
   * 主键
   */
  id : string | number;

  /**
   * 活动编号
   */
  activityId : string | number;

  /**
   * 产品代码
   */
  productCode : string;

  /**
   * 产品名称
   */
  productName : string;

  /**
   * 入选日期
   */
  productDate : string;

  /**
   * 入选价格
   */
  productAmount : number;

  /**
   * 入选后当天价格
   */
  productAmountNow : number;

  /**
   * 入选后1天价格
   */
  productAmount1 : number;

  /**
   * 入选后2天价格
   */
  productAmount2 : number;

  /**
   * 入选后3天价格
   */
  productAmount3 : number;

  /**
   * 选择状态：1-选择，2-默认，3-丢弃
   */
  selectStatus ?: string;

  /**
   * 创建时间
   */
  createTime : string;

  /**
   * 更新时间
   */
  updateTime : string;

}

export interface ProductActivityForm extends BaseEntity {
  /**
   * 主键
   */
  id ?: string | number;

  /**
   * 活动编号
   */
  activityId ?: string | number;

  /**
   * 产品代码
   */
  productCode ?: string;

  /**
   * 产品名称
   */
  productName ?: string;

  /**
   * 入选日期
   */
  productDate ?: string;

  /**
   * 入选价格
   */
  productAmount ?: number;

  /**
   * 入选后当天价格
   */
  productAmountNow ?: number;

  /**
   * 入选后1天价格
   */
  productAmount1 ?: number;

  /**
   * 入选后2天价格
   */
  productAmount2 ?: number;

  /**
   * 入选后3天价格
   */
  productAmount3 ?: number;

  /**
   * 选择状态：1-选择，2-默认，3-丢弃
   */
  selectStatus ?: string;

}

export interface ProductActivityQuery extends PageQuery {

  /**
   * 活动编号
   */
  activityId ?: string | number;

  /**
   * 产品代码
   */
  productCode ?: string;

  /**
   * 产品名称
   */
  productName ?: string;

  /**
   * 产品名称
   */
  queryType ?: string;

  /**
   * 入选日期
   */
  productDate ?: string;

  /**
   * 创建时间
   */
  createTime ?: string;

  /**
   * 选择状态：1-选择，2-默认，3-丢弃
   */
  selectStatus ?: string;

  /**
   * 排序字段
   */
  orderByColumn ?: string;

  /**
   * 排序的方向desc或者asc
   */
  isAsc ?: string;

  /**
   * 日期范围参数
   */
  params ?: any;
}
