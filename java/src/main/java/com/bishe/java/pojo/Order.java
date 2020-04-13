package com.bishe.java.pojo;

/**
 * @ClassName： Order
 * @description: 从全球蛙爬取到的订单数据
 * @author: 席思诚
 * @create: 2020-01-21 15:36
 **/
public class Order  {
    //下单时间
    private String time;
    //姓名
    private String name;
    //年龄
    private Integer age;
    //所在城市
    private String  city;
    private  Goods goods;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public Order(String time, String name, Integer age, String city, Goods goods) {
        this.time = time;
        this.name = name;
        this.age = age;
        this.city = city;
        this.goods = goods;
    }
}
