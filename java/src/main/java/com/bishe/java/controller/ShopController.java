package com.bishe.java.controller;

import com.alibaba.fastjson.JSON;
import com.bishe.java.mapper.GoodMapper;
import com.bishe.java.pojo.Goods;
import com.bishe.java.pojo.Order;
import com.bishe.java.util.OrderGenerate;
import com.bishe.java.util.RandomUtil;
import com.bishe.java.util.ResponseError;
import com.bishe.java.util.ResponseOk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.nio.ch.ThreadPool;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @ClassName： ShopController
 * @description: 请求获取指标
 * @author: 席思诚
 * @create: 2020-01-22 14:23
 **/
@RestController
@RequestMapping("/target")
@CrossOrigin("*")
public class ShopController {
    @Autowired
    private GoodMapper goodMapper;

    @Autowired
    private  OrderGenerate orderGenerate;

    @Autowired
    private StringRedisTemplate redisTemplate;

    //获取热卖的商品top10  左上部那个图
    @GetMapping("/getTopShop10")
    public ResponseEntity getTop10(){
        try {
            List<Goods> top10 = goodMapper.getTop10();
            Map<String,List> rs=new HashMap();
            List key1=new ArrayList();
            List key2=new ArrayList();
            List keys=new ArrayList();
            Integer start=0;
            for(Goods goods:top10){
                start+=goods.getSaleNum();
            }
            final Integer sum=start;
            top10.forEach(x->{
                key1.add(x.getSaleNum());

                key2.add(
                   new DecimalFormat("###,###,###.##").format(new Float(x.getSaleNum())/sum*100));
                keys.add(x.getTitle());
            });

            rs.put("key1",key1);
            rs.put("key2",key2);
            rs.put("keys",keys);
            return ResponseOk.create(rs);
        }catch (Exception e){
            return ResponseError.create(e.getMessage());
        }
    }
    //最贵的十个商品
    @GetMapping("/getLastShop10")
    public ResponseEntity getLast10(){
        try {
            List<Goods> top10 = goodMapper.getLast10();
            Map<String,List> rs=new HashMap();
            List key1=new ArrayList();
            List key2=new ArrayList();
            List keys=new ArrayList();


            top10.forEach(x->{
                key1.add(x.getSaleNum());

                key2.add(x.getPrice());
                keys.add(x.getTitle());
            });

            rs.put("key1",key1);
            rs.put("key2",key2);
            rs.put("keys",keys);
            return ResponseOk.create(rs);
        }catch (Exception e){
            return ResponseError.create(e.getMessage());
        }
    }
    //对应各大类销量那一块 123名
    @GetMapping("/getTypeTop3")
    public ResponseEntity getTypeTop3(){
        List<Map<String, Object>> type3 = goodMapper.getType3();
        //求销售总量
        BigDecimal sumSaleNum=new BigDecimal(0);
        for(Map<String,Object> map:type3){
            sumSaleNum=sumSaleNum.add(new BigDecimal(map.get("typeSaleNum").toString()));
        }
        List<Object> collect = type3.stream().sorted(Comparator.comparingInt(o -> -(new Integer(o.get("typeSaleNum").toString())).intValue())).map((x) -> x.get("typeSaleNum")).collect(Collectors.toList());
        List<String> types = type3.stream().sorted(Comparator.comparingInt(o -> -(new Integer(o.get("typeSaleNum").toString())).intValue())).map((x) -> x.get("type").toString()).collect(Collectors.toList());

        Map<String,Object> rs=new HashMap<>();
        rs.put("hao",collect.get(0));
        rs.put("zhong",collect.get(1));
        rs.put("cha",collect.get(2));
        rs.put("type1",types.get(0));
        rs.put("type2",types.get(1));
        rs.put("type3",types.get(2));
        rs.put("allnum",sumSaleNum);
        return ResponseOk.create(rs);

    }
    //最赚钱的五大类
    @GetMapping("getTypeMoneyTop5")
    public ResponseEntity getMoneyTop5(){
        List<Map<String, Object>> typeSaleSum = goodMapper.getTypeSaleSum();
        float a=0l;
        for (Map<String, Object> map :
            typeSaleSum) {
            a+=new Float(map.get("typeSaleNum").toString());
        }
        final  float sum=a;
        typeSaleSum.stream().forEach(x->{
            x.put("sum",sum);
            x.put("zhanbi",new DecimalFormat("###,###,###.##").format((new Double(x.get("typeSaleNum").toString())/new Double(sum))*100));
        });
        return  ResponseOk.create(typeSaleSum.stream().limit(5).collect(Collectors.toList()));
    }
    @GetMapping("getTypeMoneyLast5")
    public ResponseEntity getMoneyLast5(){
        List<Map<String, Object>> typeSaleSum = goodMapper.getTypeSaleSum();
        float a=0l;
        for (Map<String, Object> map :
            typeSaleSum) {
            a+=new Float(map.get("typeSaleNum").toString());
        }
        final  float sum=a;
        typeSaleSum.stream().forEach(x->{
            x.put("sum",sum);
            x.put("zhanbi",new DecimalFormat("###,###,###.##").format((new Double(x.get("typeSaleNum").toString())/new Double(sum))*100));
        });
        Collections.reverse(typeSaleSum);
        return  ResponseOk.create(typeSaleSum.stream().limit(5).collect(Collectors.toList()));
    }
    private  final ExecutorService pools = Executors.newFixedThreadPool(8);
    @GetMapping("getRandomOrder")
    public ResponseEntity getRandomOrder(){
        Order order = orderGenerate.generateOrder();
        pools.execute(()->{
            //1.修改数据库对应商品销量+1
            try {
                writeFile(order);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Goods goods = order.getGoods();
            goods.setSaleNum(goods.getSaleNum()+1);
            goodMapper.updateById(goods);
            //2.计算当前小时的收入
            redisTemplate.opsForZSet().incrementScore(RandomUtil.getHour()+":income",order.getCity(),goods.getPrice());
            redisTemplate.expire(RandomUtil.getHour()+":income",60, TimeUnit.MINUTES);
            //3。当前城市的下单数+1
            redisTemplate.opsForZSet().incrementScore(RandomUtil.getHour()+":num",order.getCity(),1);
            redisTemplate.expire(RandomUtil.getHour()+":num",60, TimeUnit.MINUTES);
            //4。当前小时下单年龄段喜好分析
            redisTemplate.opsForZSet().incrementScore(RandomUtil.getHour()+":ageType",getAgeType(order.getAge()),1);
            redisTemplate.expire(RandomUtil.getHour()+":ageType",60, TimeUnit.MINUTES);
        });
        return  ResponseOk.create(order);
    }
    public void writeFile(Order order) throws Exception{
        File fout = new File("order.txt");
        if(!fout.exists()){
            fout.createNewFile();

        }
        String json = JSON.toJSONString(order);
        FileOutputStream fos = new FileOutputStream(fout,true);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

        bw.write(json);
        bw.newLine();

        bw.close();
    }
    @GetMapping("getCityNum")
    public ResponseEntity getCityNum(){
        Set<ZSetOperations.TypedTuple<String>> typedTuples = redisTemplate.opsForZSet().reverseRangeWithScores(RandomUtil.getHour() + ":num", 0, 9);
        List<List> rs=new ArrayList<>();
        List key=new ArrayList();
        List value=new ArrayList();
        typedTuples.forEach(x->{
            key.add(x.getValue());
            value.add(x.getScore().intValue());
        });
        rs.add(key);
        rs.add(value);
        return ResponseOk.create(rs);
    }
    @GetMapping("getCityIncome")
    public ResponseEntity getCityIncome(){
        Set<ZSetOperations.TypedTuple<String>> typedTuples = redisTemplate.opsForZSet().reverseRangeWithScores(RandomUtil.getHour() + ":income", 0, 7);
        Map rs=new HashMap();

        typedTuples.forEach(x->{
           rs.put(x.getValue(),x.getScore().intValue());
        });

        return ResponseOk.create(rs);
    }
    @GetMapping("getAge")
    public ResponseEntity getHourAge(){
        Set<ZSetOperations.TypedTuple<String>> typedTuples = redisTemplate.opsForZSet().rangeWithScores(RandomUtil.getHour() + ":ageType", 0, -1);
        Map<String,Integer> map=new HashMap<>();
        typedTuples.forEach(x->{
            map.put(x.getValue(),x.getScore().intValue());
        });
        return ResponseOk.create(map);
    }
    public  String getAgeType(Integer age){
       if(age<20){
           return "少年";
       }
       if(age<30){
          return "青年";
        }
       if(age<50){
           return  "中年";
       }
       return "老年人";
    }
}

