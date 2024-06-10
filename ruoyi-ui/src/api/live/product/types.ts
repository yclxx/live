export interface ProductVO {
  /**
   * 产品代码
   */
  productCode : string;

  /**
   * 产品名称
   */
  productName : string;

  /**
   * 状态
   */
  status : string;

  /**
   * 类型
   */
  productType ?: string;

  /**
   * 最新信息
   */
  gpInfoVo ?: object;

  /**
   * 创建时间
   */
  createTime : string;

  /**
   * 更新时间
   */
  updateTime : string;

}

export interface GpInfoVO {
  id : string;
  /**
   * 产品代码
   */
  productCode : string;

  /**
   * 产品名称
   */
  productName : string;

  /**
   * 日期 2024-05-24
   */
  infoDate : string;
  /**
   * 开盘价
   */
  f17 : number;
  /**
   * 收盘价
   */
  f2 : number;
  /**
   * 最高价
   */
  f15 : number;
  /**
   * 最低
   */
  f16 : number;
  /**
   * 成交量（手）
   */
  f5 : number;
  /**
   * 成交额（元）
   */
  f6 : number;
  /**
   * 振幅(%)
   */
  f7 : number;
  /**
   * 涨跌幅(%)
   */
  f3 : number;
  /**
   * 涨跌额
   */
  f4 : number;
  /**
   * 换手率
   */
  f8 : number;
  /**
   * 5日均价
   */
  ma5 : number;
  /**
   * 10日均价
   */
  ma10 : number;
  /**
   * 20日均价
   */
  ma20 : number;

  /**
   * 10日最高价
   */
  max10 : number;
}

export interface ProductForm extends BaseEntity {
  /**
   * 产品代码
   */
  productCode ?: string;

  /**
   * 产品名称
   */
  productName ?: string;

  /**
   * 状态
   */
  status ?: string;

  /**
   * 类型
   */
  productType ?: any;

}

export interface ProductQuery extends PageQuery {

  /**
   * 产品代码
   */
  productCode ?: string;

  /**
   * 产品名称
   */
  productName ?: string;

  /**
   * 状态
   */
  status ?: string;

  /**
   * 类型
   */
  productType ?: any;

  /**
   * 日期范围参数
   */
  params ?: any;
}
