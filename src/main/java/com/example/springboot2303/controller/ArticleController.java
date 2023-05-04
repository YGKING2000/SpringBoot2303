package com.example.springboot2303.controller;

import com.example.springboot2303.entity.Article;
import com.example.springboot2303.util.DBUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.*;
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
        String publisher = request.getParameter("publisher");
        String content = request.getParameter("content");
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
        try (
                Connection connection = DBUtil.getConnection()
        ) {
            String querySql = "SELECT 1 FROM article WHERE title = ?";
            PreparedStatement ps = connection.prepareStatement(querySql);
            ps.setString(1, title);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                try {
                    response.sendRedirect("/article_exist.html");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                String insertSql = "INSERT INTO article (title, publisher, content) VALUES (?, ?, ?)";
                ps = connection.prepareStatement(insertSql);
                ps.setString(1, title);
                ps.setString(2, publisher);
                ps.setString(3, content);
                int num = ps.executeUpdate();
                if (num > 0) {
                    response.sendRedirect("/article_success.html");
                } else {
                    response.sendRedirect("/article_fail.html");
                }
            }
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
        }
    }

    @RequestMapping("/articleList")
    public void articleList(HttpServletRequest request, HttpServletResponse response) {
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
            Connection connection = DBUtil.getConnection();
            Statement statement = connection.createStatement();
            String sql = "SELECT title, publisher, content FROM article";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                pw.println("<tr>");
                pw.println("<td>" + resultSet.getString("title") + "</td>");
                pw.println("<td>" + resultSet.getString("publisher") + "</td>");
                pw.println("<td>" + resultSet.getString("content") + "</td>");
                pw.println("</tr>");
            }
            pw.println("</table>");
            pw.println("</center>");
            pw.println("</body>");
            pw.println("</html>");
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }

    }
}
