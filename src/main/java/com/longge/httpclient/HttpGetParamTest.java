package com.longge.httpclient;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author longge
 * @create 2019-10-29 下午12:24
 */
public class HttpGetParamTest {
    public static void main(String[] args) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        //创建URIBuilder，并用其设置参数 （setParameter方法会覆盖原有的同名参数，而addParameter方法不会）
        URIBuilder uriBuilder = new URIBuilder("http://yun.itheima.com/search");
        uriBuilder.setParameter("keys","Java"); //相当于访问：http://yun.itheima.com/search?keys=Java

        HttpGet httpGet = new HttpGet(uriBuilder.build());
        CloseableHttpResponse response = null;
        try{
            response = httpClient.execute(httpGet);
            if(response.getStatusLine().getStatusCode()==200){
                String content = EntityUtils.toString(response.getEntity(), "utf8");
                System.out.println(content.length());
            }
        }catch(IOException e){
            e.printStackTrace();
        }finally {
            try {
                if(response!=null)
                    response.close();
            }catch(IOException e){
                e.printStackTrace();
            }
            try{
                httpClient.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
