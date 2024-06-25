export interface ProductMoneyLogVO {
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
   * 主力净流入
   */
  f62: number;

  /**
   * 小单净流入
   */
  f84: number;

  /**
   * 中单净流入
   */
  f78: number;

  /**
   * 大单净流入
   */
  f72: number;

  /**
   * 超大单净流入
   */
  f66: number;

  /**
   * 主力净流入 净占比
   */
  f184: number;

  /**
   * 小单净流入 净占比
   */
  f87: number;

  /**
   * 中单净流入 净占比
   */
  f81: number;

  /**
   * 大单净流入 净占比
   */
  f75: number;

  /**
   * 超大单净流入 净占比
   */
  f69: number;

  /**
   * 更新时间
   */
  updateTime: string;

}

export interface ProductMoneyLogForm extends BaseEntity {
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
   * 主力净流入
   */
  f62?: number;

  /**
   * 小单净流入
   */
  f84?: number;

  /**
   * 中单净流入
   */
  f78?: number;

  /**
   * 大单净流入
   */
  f72?: number;

  /**
   * 超大单净流入
   */
  f66?: number;

  /**
   * 主力净流入 净占比
   */
  f184?: number;

  /**
   * 小单净流入 净占比
   */
  f87?: number;

  /**
   * 中单净流入 净占比
   */
  f81?: number;

  /**
   * 大单净流入 净占比
   */
  f75?: number;

  /**
   * 超大单净流入 净占比
   */
  f69?: number;

}

export interface ProductMoneyLogQuery extends PageQuery {

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



