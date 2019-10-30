package com.longge.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.longge.pojo.Item;
import com.longge.service.ItemService;
import com.longge.util.HttpUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @author longge
 * @create 2019-10-30 下午1:39
 */
@Component
public class ItemTask {
    @Autowired
    HttpUtils httpUtils;
    @Autowired
    ItemService service;

    @Scheduled(fixedDelay = 100*1000) //当下载任务完成后，间隔100s进行下一次任务
    public void itemTask() throws Exception{
        //声明需要解析的初始地址
        String url = "https://search.jd.com/Search?keyword=%E6%89%8B%E6%9C%BA&enc=utf-8" +
                "&qrst=1&rt=1&stop=1&vt=2&wq=shou%27ji&s=106&click=0&page=";
        //按照页面对手机的搜索结果进行遍历解析
        for(int i=1;i<10;i=i+2){
            String html = httpUtils.doGetHtml(url+i);
            //解析页面，获取商品数据并存储
            this.parse(html);
        }
        System.out.println("手机数据抓取完成！");
    }

    private void parse(String html) throws Exception{
        //解析html获取document
        Document doc = Jsoup.parse(html);
        //获取spu
        Elements spuElements = doc.select("div#J_goodsList > ul > li");
        for(Element spuElement : spuElements){
            long spu = Long.parseLong(spuElement.attr("data-spu"));
            //获取sku
            Elements skuElements = spuElement.select("li.ps-item");
            for(Element skuElement : skuElements){
                long sku = Long.parseLong(skuElement.select("[data-sku]").attr("data-sku"));
                //根据sku查询商品数据 若商品已存在，则不保存，否则保存
                Item item = new Item();
                item.setSku(sku);
                List<Item> list = service.findAll(item);
                if(list.size()>0){
                    continue;
                }
                item.setSpu(spu);

                //获取商品详情的url
                String itemUrl = "https://item.jd.com/"+sku+".html";
                item.setUrl(itemUrl);

                //获取商品图片
                //注意：在浏览器中的开发者工具中看到的属性名可能跟实际中不一样，需要打断点，debug，实际跑一下看看
                //System.out.println(skuElement.toString());
                String picUrl = "https:"+skuElement.select("img[data-sku]").first().attr("data-lazy-img");
                if(StringUtils.isBlank(picUrl)){
                    picUrl = "https:"+skuElement.select("img[data-sku]").first().attr("data-lazy-img-slave");
                }
                System.out.println(picUrl);
                picUrl = picUrl.replace("/n9/","/n1/");
                String picName = httpUtils.doGetImg(picUrl);
                item.setPic(picName);

                //获取商品价格
                String priceJson = httpUtils.doGetHtml("https://p.3.cn/prices/mgets?skuIds=J_"+sku);
                double price = new ObjectMapper().readTree(priceJson).get(0).get("p").asDouble();
                item.setPrice(price);

                //获取商品标题
                String itemInfo = httpUtils.doGetHtml(item.getUrl());
                Document itemDoc = Jsoup.parse(itemInfo);
                String title = itemDoc.select("div.sku-name").text();
                item.setTitle(title);

                item.setCreated(new Date());
                item.setUpdated(item.getCreated());

                service.save(item);
            }
        }
    }
}
