package com.example.springboot2303.controller;

import com.example.springboot2303.entity.Article;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;

/**
 * @Description
 * @ClassName ArticleController
 * @Author YGKING e-mail:hrd18960706057@163.com
 * @Date 2023/04/15 12:50
 * @Version 1.0
 */
@Controller
public class ArticleController {
    private static final File articles;

    static {
        articles = new File("./articles");
        if (!articles.exists()) {
            articles.mkdirs();
        }
    }

    /*
     * @Description 发表评论的方法
     * @Return void
     * @param HttpServletRequest request
     * @param HttpServletResponse response
     * @Author YGKING
     * @Date 2023/04/15 12:52:51
     */
    @RequestMapping("/writeArticle")
    public void writeArticle(HttpServletRequest request, HttpServletResponse response) {
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String publisher = request.getParameter("publisher");
        if (title == null || title.isEmpty()
                || content == null || content.isEmpty()
                || publisher == null || publisher.isEmpty()) {
            try {
                response.sendRedirect("/article_fail.html");
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        File file = new File(articles, title + ".obj");
        if (file.exists()) {
            try {
                response.sendRedirect("/article_fail.html");
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Article article = new Article(title, publisher, content);
        try (
                FileOutputStream fos = new FileOutputStream(file);
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            oos.writeObject(article);
            response.sendRedirect("/article_success.html");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/articleList")
    public void articleList(HttpServletRequest request, HttpServletResponse response) {
        ArrayList<Article> articleList = new ArrayList<>();
        File[] files = articles.listFiles(file -> file.getName().endsWith(".obj"));
        assert files != null;
        for (File item : files) {
            try (
                    FileInputStream fis = new FileInputStream(item);
                    ObjectInputStream ois = new ObjectInputStream(fis);
            ) {
                Article article = (Article) ois.readObject();
                articleList.add(article);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        try {
            response.setContentType("text/html;charset=utf-8");
            PrintWriter pw = response.getWriter();
            pw.println("<!DOCTYPE html>");
            pw.println("<html lang=\"en\">");
            pw.println("<head>");
            pw.println("<meta charset=\"UTF-8\">");
            pw.println("<title>文章列表</title>");
            pw.println("</head>");
            pw.println("<body>");
            pw.println("<center>");
            pw.println("<h1>文章列表</h1>");
            pw.println("<table border=\"1\">");
            pw.println("<tr>");
            pw.println("<td>标题</td>");
            pw.println("<th>发表者</th>");
            pw.println("<th>内容</th>");
            pw.println("</tr>");
            articleList.forEach(article -> {
                pw.println("<tr>");
                pw.println("<td>" + article.getTitle() + "</td>");
                pw.println("<td>" + article.getPublisher() + "</td>");
                pw.println("<td>" + article.getContent() + "</td>");
                pw.println("</tr>");
            });
            pw.println("</table>");
            pw.println("</center>");
            pw.println("</body>");
            pw.println("</html>");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
