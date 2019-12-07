import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: rjl
 * @date: 2019/12/7
 */
public class Test {
    @org.junit.Test
    public void gettest(){
        String str = "https://www.bxwxorg.com/read/13169/";
        str = str.substring(12, 16);
        System.out.println(str);
    }

    public List<String> getAllChapterUrlsFromBXWX(String address){
        Document document;
        // 连接当前的网页
        try {
            // 连接当前的网页
            document = Jsoup.connect(address).get();
            // 获取a标签属性为href的内容
            Element link = document.selectFirst("dl");
            Elements links = link.children();
            Elements link2 = links.select("dd");
            List<String> abc = new ArrayList<String>();
            for (int i = 12; i < link2.size(); i++) {
                String str = link2.get(i).selectFirst("a[href]").absUrl("href");
                abc.add(str);
            }
            return abc;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> getContent(String address){
        Document document;
        // 连接当前的网页
        try {
            // 连接当前的网页
            document = Jsoup.connect(address).header("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:52.0) Gecko/20100101 Firefox/52.0").get();
            // 获取a标签属性为href的内容
            Element link = document.selectFirst("div[id=content]");
            Elements list = link.select("p");
            List<String> stringList = new ArrayList<String>();
            for (int i = 0;i<list.size()-1;i++) {
                    stringList.add(list.get(i).text());
            }
            return stringList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
