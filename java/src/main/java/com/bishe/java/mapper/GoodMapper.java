package com.bishe.java.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bishe.java.pojo.Goods;
import com.bishe.java.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 席思诚123
 * @since 2020-01-13
 */
public interface GoodMapper extends BaseMapper<Goods> {
    List<Goods>  getTop10();
    List<Goods>  getLast10();
    List<Map<String, Object>> getType3();
    List<Map<String,Object>> getTypeSaleSum();
    Goods  getRandomOne();

}
