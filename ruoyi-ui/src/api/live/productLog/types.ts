export interface ProductLogVO {
  /**
   * 主键
   */
  id: string | number;

  /**
   * 产品代码
   */
  productCode: string;

  /**
   * 产品名称
   */
  productName: string;

  /**
   * 日期
   */
  infoDate: string;

  /**
   * 开盘价
   */
  f17: number;

  /**
   * 收盘价
   */
  f2: number;

  /**
   * 最高价
   */
  f15: number;

  /**
   * 最低价
   */
  f16: number;

  /**
   * 成交量（手）
   */
  f5: number;

  /**
   * 成交额（元）
   */
  f6: number;

  /**
   * 振幅(%)
   */
  f7: number;

  /**
   * 涨跌幅(%)
   */
  f3: number;

  /**
   * 涨跌额
   */
  f4: number;

  /**
   * 换手率
   */
  f8: number;

  /**
   * 5日均价
   */
  ma5: number;

  /**
   * 10日均价
   */
  ma10: number;

  /**
   * 20日均价
   */
  ma20: number;

  /**
   * 创建时间
   */
  createTime: string;

}

export interface ProductLogForm extends BaseEntity {
  /**
   * 主键
   */
  id?: string | number;

  /**
   * 产品代码
   */
  productCode?: string;

  /**
   * 产品名称
   */
  productName?: string;

  /**
   * 日期
   */
  infoDate?: string;

  /**
   * 开盘价
   */
  f17?: number;

  /**
   * 收盘价
   */
  f2?: number;

  /**
   * 最高价
   */
  f15?: number;

  /**
   * 最低价
   */
  f16?: number;

  /**
   * 成交量（手）
   */
  f5?: number;

  /**
   * 成交额（元）
   */
  f6?: number;

  /**
   * 振幅(%)
   */
  f7?: number;

  /**
   * 涨跌幅(%)
   */
  f3?: number;

  /**
   * 涨跌额
   */
  f4?: number;

  /**
   * 换手率
   */
  f8?: number;

  /**
   * 5日均价
   */
  ma5?: number;

  /**
   * 10日均价
   */
  ma10?: number;

  /**
   * 20日均价
   */
  ma20?: number;

}

export interface ProductLogQuery extends PageQuery {

  /**
   * 产品代码
   */
  productCode?: string;

  /**
   * 产品名称
   */
  productName?: string;

  /**
   * 日期
   */
  infoDate?: string;

    /**
     * 日期范围参数
     */
    params?: any;
}



