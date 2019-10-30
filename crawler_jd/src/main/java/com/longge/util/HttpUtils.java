package com.longge.util;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

/**
 * @author longge
 * @create 2019-10-30 上午11:56
 */
@Component
public class HttpUtils {
    private PoolingHttpClientConnectionManager cm;

    public HttpUtils(){
        this.cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(100);
        cm.setDefaultMaxPerRoute(10);
    }

    private RequestConfig getConfig(){
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(2000)
                .setConnectionRequestTimeout(1000)
                .setSocketTimeout(10000)
                .setProxy(new HttpHost("114.239.254.253",49132)) //高匿代理
                .build();
        return config;
    }

    //根据请求地址下载页面数据
    public String doGetHtml(String url){
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(getConfig());
        //设置请求头消息 User-Agent 模拟浏览器
        httpGet.setHeader("User-Agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) " +
                "AppleWebKit/605.1.15 (KHTML, like Gecko) Version/12.1.2 Safari/605.1.15");
        CloseableHttpResponse response = null;
        try{
            response = httpClient.execute(httpGet);
            if(response.getStatusLine().getStatusCode() == 200){
                if(response.getEntity()!=null){
                    String content = EntityUtils.toString(response.getEntity(),"utf8");
                    return content;
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }finally {
            if(response!=null){
                try{
                    response.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    //下载图片
    public String doGetImg(String url){
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
        System.out.println(url);
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(getConfig());
        CloseableHttpResponse response = null;
        try{
            response = httpClient.execute(httpGet);
            if(response.getStatusLine().getStatusCode() == 200){
                if(response.getEntity()!=null){
                    //图片后缀
                    String extName = url.substring(url.lastIndexOf("."));
                    //创建图片名称
                    String picName = UUID.randomUUID().toString()+extName;
                    //下载图片
                    OutputStream out = new FileOutputStream(new File("/Users/longyonghua/Desktop/pic/"+picName));
                    response.getEntity().writeTo(out);
                    //返回图片名称
                    return picName;
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }finally {
            if(response!=null){
                try{
                    response.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return "";
    }
}
