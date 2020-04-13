package com.bishe.java.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * @ClassName： Goods
 * @description:
 * @author: 席思诚
 * @create: 2020-01-21 19:21
 **/
@TableName("bishe_goods")

public class Goods implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String title;
    @TableField("sellPoint")
    private  String sellPoint;
    @TableField("picUrl")
    private  String picUrl;
    private Float price;
    @TableField("saleNum")
    private  Integer saleNum;
    @TableField("vipFinalItemPriceRangeBj")
    private  Float vipFinalItemPriceRangeBj;

    private String type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSellPoint() {
        return sellPoint;
    }

    public void setSellPoint(String sellPoint) {
        this.sellPoint = sellPoint;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }



    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Integer getSaleNum() {
        return saleNum;
    }

    public void setSaleNum(Integer saleNum) {
        this.saleNum = saleNum;
    }

    public Float getVipFinalItemPriceRangeBj() {
        return vipFinalItemPriceRangeBj;
    }

    public void setVipFinalItemPriceRangeBj(Float vipFinalItemPriceRangeBj) {
        this.vipFinalItemPriceRangeBj = vipFinalItemPriceRangeBj;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
