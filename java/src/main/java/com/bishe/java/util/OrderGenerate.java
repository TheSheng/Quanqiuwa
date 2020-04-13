package com.bishe.java.util;

import com.bishe.java.mapper.GoodMapper;
import com.bishe.java.pojo.Goods;
import com.bishe.java.pojo.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName： OrderGenerate
 * @description: 订单生成器
 * @author: 席思诚
 * @create: 2020-01-21 15:40
 **/
@Component
public class OrderGenerate {
    @Autowired
    private GoodMapper goodMapper;
    public  Order generateOrder(){
        String name = RandomUtil.getChineseName();
        Integer age = RandomUtil.getAge();
        String time=RandomUtil.getTime();
        String citty = RandomUtil.getAddr();
        Goods goods = goodMapper.getRandomOne();
        Order order=new Order(time,name,age,citty,goods);
        return order;
    }

}
