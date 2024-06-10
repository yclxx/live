export interface ProductAnalyseVO {
  /**
   * 主键
   */
  id: string | number;

  /**
   * 日期
   */
  infoDate: string;

  /**
   * 规则Id
   */
  analyseNo: string;

  /**
   * 统计结果
   */
  analyseJson: string;

  /**
   * 验证结果
   */
  verifyJson: string;

  /**
   * 正确率
   */
  accuracy: number;

  /**
   * 更新时间
   */
  updateTime: string;

}

export interface ProductAnalyseForm extends BaseEntity {
  /**
   * 主键
   */
  id?: string | number;

  /**
   * 日期
   */
  infoDate?: string;

  /**
   * 规则Id
   */
  analyseNo?: string;

  /**
   * 统计结果
   */
  analyseJson?: string;

  /**
   * 验证结果
   */
  verifyJson?: string;

  /**
   * 正确率
   */
  accuracy?: number;

}

export interface ProductAnalyseQuery extends PageQuery {

  /**
   * 日期
   */
  infoDate?: string;

  /**
   * 规则Id
   */
  analyseNo?: string;

    /**
     * 日期范围参数
     */
    params?: any;
}



