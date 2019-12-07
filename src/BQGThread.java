import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: rjl
 * @date: 2019/11/30
 */
public class BQGThread extends Thread{

    private List<String> url;
    private String name;
    private int n1;
    private int n2;

    public BQGThread(List<String> list,String name,int n1,int n2){
        this.url = list;
        this.name = name;
        this.n1 = n1;
        this.n2 = n2;
    }

    @Override
    public void run(){
        List<String> chapterList = url;
        int i = 1;
        if(n2 == -1)
            chapterList = chapterList.subList(n1, chapterList.size());
        else
            chapterList = chapterList.subList(n1, n2);
        for (String data : chapterList) {
            List<String> contentList = getContent(data);
            BufferedWriter out = null;
            try {
                out = new BufferedWriter(new FileWriter(new File("D:" + File.separator + name + n1+ "_" + n2 + ".txt"),true));
                for(String d:contentList)
                    out.write("    " + d+"\r\n");
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(n1 + "线程：" + i++);

        }
        System.out.println(n1+"线程下载完毕！");
    }

    public List<String> getContent(String address){
        Document document;
        // 连接当前的网页
        try {
            // 连接当前的网页
            document = Jsoup.connect(address).header("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:52.0) Gecko/20100101 Firefox/52.0").get();
            // 获取a标签属性为href的内容
            Element link = document.selectFirst("div[id=content]");
            List<TextNode> list = link.textNodes();
            List<String> stringList = new ArrayList<String>();
            for (int i = 0;i<list.size()-2;i++) {
                if (!list.get(i).isBlank() && !list.get(i).equals(" ")) {
                    stringList.add(list.get(i).text());
                }
            }
            return stringList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
