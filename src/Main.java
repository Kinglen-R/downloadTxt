import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("***本系统可以下载全本小说，也可下载指定章节***");
        System.out.println("***小说地址可从www.biqugex.com中获取***");
        System.out.println("***小说地址获取方式：进入网站，搜索想要看的书名，进入小说章节页面，将此时浏览器中额地址复制过来即可***");
        Scanner sc = new Scanner(System.in);
        System.out.print("请输入小说地址：");
        String url = sc.nextLine();
        System.out.print("请输入小说名：");
        String name = sc.nextLine();
        System.out.print("是否下载全本小说：0-是，1-否：");
        int all = sc.nextInt();
        while(all != 0 && all != 1) {
            System.out.println("请输入0或者1");
            all = sc.nextInt();
        }
        if (all == 1) {
            System.out.print("请输入小说开始章节序号:(0从第一章开始)");
            int n1 = sc.nextInt();
            System.out.print("请输入小说结束章节序号:(-1为小说最后一章)");
            int n2 = sc.nextInt();
            System.out.println("开始下载");
            NoveltoTxt(url, name, n1, n2);
            System.out.println("下载完毕，请在D盘下查看！");
        } else if (all == 0) {
            List<String> list = getAllChapterUrls(url);
            float size = list.size();
            int threadNum = (int) Math.ceil(size / 500.0);
            for (int i = 1; i <= threadNum; i++) {
                int n1 = (i - 1) * 500;
                int n2 = -1;
                if (i * 500 <= list.size())
                    n2 = i * 500;
                MyThread thread = new MyThread(list, name, n1, n2);
                thread.start();
            }

        }
    }

    public static List<String> getAllChapterUrls(String address) {
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

    public static List<String> getContent(String address){
        Document document;
        // 连接当前的网页
        try {
            // 连接当前的网页
            document = Jsoup.connect(address).header("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36").get();
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

    public static void NoveltoTxt(String url,String name,int n1, int n2){
        List<String> chapterList = getAllChapterUrls(url);
        int i = 1;
        if(n2 != -1)
            chapterList = chapterList.subList(n1, n2);
        else
            chapterList = chapterList.subList(n1, chapterList.size());
        for (String data : chapterList) {
            List<String> contentList = getContent(data);
            BufferedWriter out = null;
            try {
                out = new BufferedWriter(new FileWriter(new File("D:" + File.separator + name +n1+ "_" + n2 + ".txt"),true));
                for(String d:contentList)
                    out.write("    " + d+"\r\n");
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(i++);

        }
    }
}
