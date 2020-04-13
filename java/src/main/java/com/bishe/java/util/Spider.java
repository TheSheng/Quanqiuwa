package com.bishe.java.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bishe.java.JavaApplication;
import com.bishe.java.mapper.GoodMapper;
import com.bishe.java.pojo.Goods;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import  org.apache.http.entity.StringEntity;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @ClassName： spider
 * @description: 爬虫
 * @author: 席思诚
 * @create: 2020-01-21 17:33
 **/

@Component
public class Spider {
    @Autowired
    GoodMapper goodMapper;
    final ExecutorService executorService = Executors.newCachedThreadPool();

    //分类列年货好礼的url 通过fiddler获取
    String nianhuoUrl="https://fresh-ldyapi.quanqiuwa.com/easySupport/easySupport/?CustomerId=4071663&JsonItemCategoryId=%7B%20%22CategoryInfo%22%3A%20%5B%7B%22FirstCategoryId%22%3A%221548733527696%22%2C%22SecondCategoryId%22%3A%221548733527697%22%2C%22ThirdCategoryId%22%3A%220%22%7D%5D%20%7D&OrderType=0&OrderTypeIndex=0&PageIndex=1&PageSize=1000&StoreId=81&appVersion=2.7.3&format=json&method=EasySupport.GetTakeAwayFormItemList&postTime=2020-01-21%2017%3A36%3A22&systemType=iOS&token=d0bb3b2c82c20be4220ffcceed400bd5&user=u1city";
    String shuiguo="https://fresh-ldyapi.quanqiuwa.com/easySupport/easySupport/?CustomerId=4071663&JsonItemCategoryId=%7B%20%22CategoryInfo%22%3A%20%5B%7B%22FirstCategoryId%22%3A%221548733525736%22%2C%22SecondCategoryId%22%3A%221548733527359%22%2C%22ThirdCategoryId%22%3A%220%22%7D%5D%20%7D&OrderType=0&OrderTypeIndex=0&PageIndex=1&PageSize=1000&StoreId=81&appVersion=2.7.3&format=json&method=EasySupport.GetTakeAwayFormItemList&postTime=2020-01-21%2019%3A00%3A44&systemType=iOS&token=b77dcbc45c1751ef3ad1e92994dd91be&user=u1city";
    String shucai="https://fresh-ldyapi.quanqiuwa.com/easySupport/easySupport/?CustomerId=4071663&JsonItemCategoryId=%7B%20%22CategoryInfo%22%3A%20%5B%7B%22FirstCategoryId%22%3A%221548733525922%22%2C%22SecondCategoryId%22%3A%221548733527380%22%2C%22ThirdCategoryId%22%3A%220%22%7D%5D%20%7D&OrderType=0&OrderTypeIndex=0&PageIndex=1&PageSize=1000&StoreId=81&appVersion=2.7.3&format=json&method=EasySupport.GetTakeAwayFormItemList&postTime=2020-01-21%2019%3A01%3A37&systemType=iOS&token=82ced22e2f38bc31f57e59e86fa42594&user=u1city";
    String rouqin="https://fresh-ldyapi.quanqiuwa.com/easySupport/easySupport/?CustomerId=4071663&JsonItemCategoryId=%7B%20%22CategoryInfo%22%3A%20%5B%7B%22FirstCategoryId%22%3A%221548733526080%22%2C%22SecondCategoryId%22%3A%221548733527393%22%2C%22ThirdCategoryId%22%3A%220%22%7D%5D%20%7D&OrderType=0&OrderTypeIndex=0&PageIndex=1&PageSize=1000&StoreId=81&appVersion=2.7.3&format=json&method=EasySupport.GetTakeAwayFormItemList&postTime=2020-01-21%2019%3A02%3A42&systemType=iOS&token=f7977762ee832ff20b84ce88a1713b59&user=u1city";
    String haixian="https://fresh-ldyapi.quanqiuwa.com/easySupport/easySupport/?CustomerId=4071663&JsonItemCategoryId=%7B%20%22CategoryInfo%22%3A%20%5B%7B%22FirstCategoryId%22%3A%221548733526330%22%2C%22SecondCategoryId%22%3A%221548733527407%22%2C%22ThirdCategoryId%22%3A%220%22%7D%5D%20%7D&OrderType=0&OrderTypeIndex=0&PageIndex=1&PageSize=1000&StoreId=81&appVersion=2.7.3&format=json&method=EasySupport.GetTakeAwayFormItemList&postTime=2020-01-21%2019%3A03%3A19&systemType=iOS&token=7af8bb7f90e977b31af6456671db3e11&user=u1city";
    String shushi="https://fresh-ldyapi.quanqiuwa.com/easySupport/easySupport/?CustomerId=4071663&JsonItemCategoryId=%7B%20%22CategoryInfo%22%3A%20%5B%7B%22FirstCategoryId%22%3A%221548733526556%22%2C%22SecondCategoryId%22%3A%221548733527461%22%2C%22ThirdCategoryId%22%3A%220%22%7D%5D%20%7D&OrderType=0&OrderTypeIndex=0&PageIndex=1&PageSize=1000&StoreId=81&appVersion=2.7.3&format=json&method=EasySupport.GetTakeAwayFormItemList&postTime=2020-01-21%2019%3A03%3A59&systemType=iOS&token=de2669456244af37a09f7630f9b3d3ba&user=u1city";
    String liangyou="https://fresh-ldyapi.quanqiuwa.com/easySupport/easySupport/?CustomerId=4071663&JsonItemCategoryId=%7B%20%22CategoryInfo%22%3A%20%5B%7B%22FirstCategoryId%22%3A%221548733526463%22%2C%22SecondCategoryId%22%3A%221548733527434%22%2C%22ThirdCategoryId%22%3A%220%22%7D%5D%20%7D&OrderType=0&OrderTypeIndex=0&PageIndex=1&PageSize=1000&StoreId=81&appVersion=2.7.3&format=json&method=EasySupport.GetTakeAwayFormItemList&postTime=2020-01-21%2019%3A04%3A46&systemType=iOS&token=e1ae4594bbc66237119d946ae14adff5&user=u1city";
    String miandian="https://fresh-ldyapi.quanqiuwa.com/easySupport/easySupport/?CustomerId=4071663&JsonItemCategoryId=%7B%20%22CategoryInfo%22%3A%20%5B%7B%22FirstCategoryId%22%3A%221548733525735%22%2C%22SecondCategoryId%22%3A%221548733527486%22%2C%22ThirdCategoryId%22%3A%220%22%7D%5D%20%7D&OrderType=0&OrderTypeIndex=0&PageIndex=1&PageSize=1000&StoreId=81&appVersion=2.7.3&format=json&method=EasySupport.GetTakeAwayFormItemList&postTime=2020-01-21%2019%3A05%3A17&systemType=iOS&token=5c659299d69be48d7b7f6424681cf38a&user=u1city";
    String niunai="https://fresh-ldyapi.quanqiuwa.com/easySupport/easySupport/?CustomerId=4071663&JsonItemCategoryId=%7B%20%22CategoryInfo%22%3A%20%5B%7B%22FirstCategoryId%22%3A%221548733525831%22%2C%22SecondCategoryId%22%3A%221548733527505%22%2C%22ThirdCategoryId%22%3A%220%22%7D%5D%20%7D&OrderType=0&OrderTypeIndex=0&PageIndex=1&PageSize=1000&StoreId=81&appVersion=2.7.3&format=json&method=EasySupport.GetTakeAwayFormItemList&postTime=2020-01-21%2019%3A05%3A41&systemType=iOS&token=7483b5f2288ba1aa788216ab0286dda5&user=u1city";
    //@PostConstruct
    public void generateSprider(){
        Map<String,String> urlMap=new HashMap<>();
        urlMap.put("年货好礼",nianhuoUrl);
        urlMap.put("时令水果",shuiguo);
        urlMap.put("新鲜蔬菜",shucai);
        urlMap.put("肉禽蛋类",rouqin);
        urlMap.put("海鲜水产",haixian);
        urlMap.put("熟食卤味",shushi);
        urlMap.put("粮油干调",liangyou);
        urlMap.put("面点冰品",miandian);
        urlMap.put("牛奶面包",niunai);
        executorService.execute(()-> urlMap.entrySet().forEach(x->Spider(x)));
    }
    public void Spider(Map.Entry<String,String> entry){
        try {
            CloseableHttpClient client = null;
            CloseableHttpResponse response = null;
            try {
                HttpGet httpGet = new HttpGet(entry.getValue());
                client = HttpClients.createDefault();
                response = client.execute(httpGet);
                HttpEntity entity = response.getEntity();
                JSONObject result = JSON.parseObject(EntityUtils.toString(entity),JSONObject.class);
                JSONArray itemList = result.getJSONObject("Result").getJSONArray("itemList");
                itemList.forEach(x->{
                    JSONObject json=(JSONObject)x;
                    Goods goods=new Goods();
                    goods.setPicUrl(json.getString("picUrl"));
                    goods.setPrice(json.getFloat("price"));
                    goods.setSaleNum(json.getInteger("saleNum"));
                    goods.setSellPoint(json.getString("sellPoint"));
                    goods.setVipFinalItemPriceRangeBj(json.getFloat("vipFinalItemPriceRangeBj"));
                    goods.setTitle(json.getString("title"));
                    goods.setType(entry.getKey());
                    goodMapper.insert(goods);
                });

            } finally {
                if (response != null) {
                    response.close();
                }
                if (client != null) {
                    client.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
