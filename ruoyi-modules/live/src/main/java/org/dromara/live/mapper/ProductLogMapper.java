package org.dromara.live.mapper;

import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.live.domain.ProductLog;
import org.dromara.live.domain.vo.ProductLogVo;

import java.util.List;

/**
 * 产品记录Mapper接口
 *
 * @author xx
 * @date 2024-06-02
 */
public interface ProductLogMapper extends BaseMapperPlus<ProductLog, ProductLogVo> {

    List<ProductLogVo> queryBy20002(String infoDate, String firstInfoDate);
}
