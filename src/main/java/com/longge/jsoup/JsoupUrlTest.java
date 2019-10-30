package com.longge.jsoup;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.Set;

/**
 * @author longge
 * @create 2019-10-29 下午6:08
 */
public class JsoupUrlTest {

    @Test
    public void testUrl() throws Exception{
        //参数1：访问的url，参数2：访问时的超时时间,单位毫秒
        Document doc = Jsoup.parse(new URL("http://www.itcast.cn"), 2000);
        //使用标签选择器，获取title标签中的内容
        String title = doc.getElementsByTag("title").first().text();
        System.out.println(title);
    }

    @Test
    public void testString() throws Exception{
        //使用工具类读取文件，获取字符串
        String content = FileUtils.readFileToString(new File("/Users/longyonghua/Desktop/test.html"),"utf8");
        //解析字符串
        Document doc = Jsoup.parse(content);
        String title = doc.getElementsByTag("title").first().text();
        System.out.println(title);
    }

    @Test
    public void testFile() throws Exception{
        Document doc = Jsoup.parse(new File("/Users/longyonghua/Desktop/test.html"), "utf8");
        String title = doc.getElementsByTag("title").first().text();
        System.out.println(title);
    }

    @Test
    public void testDom() throws Exception{
        Document doc = Jsoup.parse(new File("/Users/longyonghua/Desktop/test.html"),"utf8");
        //根据id查询元素
        Element element = doc.getElementById("city_bj");
        System.out.println(element.text());
        //根据标签获取元素
        Element element2 = doc.getElementsByTag("span").first();
        System.out.println(element2.text());
        //根据class获取元素
        Element element3 = doc.getElementsByClass("class_a class_b").first();
        System.out.println(element3.text());
        //根据属性获取元素
        Element element4 = doc.getElementsByAttribute("abc").first(); //根据属性名
        System.out.println(element4.text());
        Element element5 = doc.getElementsByAttributeValue("href","http://sh.itcast.cn").first(); //根据属性名和属性值
        System.out.println(element5.text());
    }

    @Test
    public void testData() throws Exception{
        Document doc = Jsoup.parse(new File("/Users/longyonghua/Desktop/test.html"), "utf8");
        Element element = doc.getElementById("test");
        //从元素中获取id
        String id = element.id(); //test
        //从元素中获取className
        String className = element.className(); //class_a class_b
        Set<String> set = element.classNames(); //[class_a, class_b]
        //从元素中获取属性的值attr
        String str = element.attr("id"); //test
        //从元素中获取所有属性attributes
        Attributes attributes = element.attributes();
        System.out.println(attributes.toString()); //id="test" class="class_a class_b"
        //从元素中获取文本内容text
        String text = element.text(); //北京
    }

    @Test
    public void tetsSelector() throws Exception{
        Document doc = Jsoup.parse(new File("/Users/longyonghua/Desktop/test.html"),"utf8");
        //通过标签查找元素 "标签名"
        Elements elements = doc.select("span");
        for(Element e : elements){
            System.out.println(e.text());
        }
        //通过id查找元素 "#id名"
        Element e = doc.select("#city_bj").first();
        System.out.println(e.text());
        //通过class名称查找元素 ".class名"
        Element e1 = doc.select(".class_a").first();
        System.out.println(e1.text());
        //利用属性查找元素 "[属性名]"
        Element e2 = doc.select("[abc]").first();
        System.out.println(e2.text());
        //利用属性值查找元素 "[属性名=属性值]"
        Elements e3 = doc.select("[class=s_name]");
        for (Element element : e3){
            System.out.println(element.text());
        }
    }

    @Test
    public void testSelector() throws Exception{
        Document doc = Jsoup.parse(new File("/Users/longyonghua/Desktop/test.html"),"utf8");
        //元素+ID  h3#city_bj
        Element e = doc.select("h3#city_bj").first();
        //元素+class  li.class_a
        Element e2 = doc.select("li.class_a").first();
        //元素+属性名  span[abc]
        Element e3 = doc.select("apan[abc]").first();
        //任意组合  span[abc].s_name
        Element e4 = doc.select("span[abc].s_name").first();
        //查找某个元素下子元素  .city_con li，查找city_con下的所有li元素
        Elements elements = doc.select(".city_con li");
        //查找某个父元素下的直接子元素  parent > child
        Elements elements1 = doc.select(".city_con > ul > li");
        //查找某个父元素下所有直接子元素  parent > *
        Elements elements2 = doc.select(".city_con > ul > *");
    }
}
