package com.example.springboot2303.controller;

import com.example.springboot2303.util.DBUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.*;

/**
 * @Description User实体类
 * @ClassName UserController
 * @Author YGKING e-mail:hrd18960706057@163.com
 * @Date 2023/04/14 20:15
 * @Version 1.0
 */
@Controller
public class UserController {
    private static final File userDir;

    static {
        userDir = new File("./userDir");
        if (!userDir.exists()) {
            userDir.mkdirs();
        }
    }

    /*
     * @Description 用户登录的方法
     * @Return void
     * @param HttpServletRequest request
     * @param HttpServletResponse response
     * @Author YGKING
     * @Date 2023/04/14 23:20:15
     */
    @RequestMapping("/loginUser")
    public void login(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        System.out.println(username + ", " + password);
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            try {
                response.sendRedirect("/login_illegal_input.html");
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try (
                Connection connection = DBUtil.getConnection()
        ) {
            String queryString = "SELECT username, password FROM userinfo WHERE username = ?";
            PreparedStatement ps = connection.prepareStatement(queryString);
            ps.setString(1, username);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                String originPwd = resultSet.getString("password");
                if (originPwd.equals(password)) {
                    response.sendRedirect("/login_success.html");
                } else {
                    response.sendRedirect("/pwd_err.html");
                }
            } else {
                response.sendRedirect("/user_not_exist.html");
            }
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
        }
    }

    /*
     * @Description 用户注册的方法
     * @Return void
     * @param HttpServletRequest request
     * @param HttpServletResponse response
     * @Author YGKING
     * @Date 2023/04/15 00:26:53
     */
    @RequestMapping("/regUser")
    public void reg(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String nickname = request.getParameter("nickname");
        String ageStr = request.getParameter("age");
        if (username == null || username.isEmpty() || password == null || password.isEmpty() ||
                nickname == null || nickname.isEmpty() || ageStr == null || !ageStr.matches("[\\d]+")) {
            try {
                response.sendRedirect("/reg_illegal_input.html");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        int age = Integer.parseInt(ageStr);

        try (
                Connection connection = DBUtil.getConnection()
        ) {
            String querySql = "SELECT 1 FROM userinfo WHERE username = ?";
            PreparedStatement ps = connection.prepareStatement(querySql);
            ps.setString(1, username);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                try {
                    response.sendRedirect("/reg_user_exist.html");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                String insertSql = "INSERT INTO userinfo (username, password, nickname, age) VALUES (?, ?, ?, ?)";
                ps = connection.prepareStatement(insertSql);
                ps.setString(1, username);
                ps.setString(2, password);
                ps.setString(3, nickname);
                ps.setInt(4, age);
                int num = ps.executeUpdate();
                if (num > 0) {
                    response.sendRedirect("/reg_success.html");
                } else {
                    response.sendRedirect("/reg_illegal_input.html");
                }
            }
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
        }
    }

    @RequestMapping("/deleteUser")
    public void deleteUser(HttpServletRequest request, HttpServletResponse response) {
        int id = Integer.parseInt(request.getParameter("id"));
        try (
                Connection connection = DBUtil.getConnection()
        ) {
            String sql = "DELETE FROM userinfo WHERE id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            int num = ps.executeUpdate();
            if (num > 0) {
                response.sendRedirect("/userList");
            } else {
                response.sendRedirect("/delete_fail.html");
            }
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
        }
    }

    @RequestMapping("/userList")
    public void userList(HttpServletRequest request, HttpServletResponse response) {
        try (
                Connection connection = DBUtil.getConnection()
        ) {
            response.setContentType("text/html;charset=utf-8");
            PrintWriter pw = response.getWriter();
            pw.println("<!DOCTYPE html>");
            pw.println("<html lang=\"en\">");
            pw.println("<head>");
            pw.println("<meta charset=\"UTF-8\">");
            pw.println("<title>用户列表</title>");
            pw.println("</head>");
            pw.println("<body>");
            pw.println("<center>");
            pw.println("<h1>用户列表</h1>");
            pw.println("<table border=\"1\">");
            pw.println("<tr>");
            pw.println("<td>ID</td>");
            pw.println("<td>用户名</td>");
            pw.println("<td>密码</td>");
            pw.println("<td>昵称</td>");
            pw.println("<td>年龄</td>");
            pw.println("<td>操作</td>");
            pw.println("</tr>");

            String sql = "SELECT id, username, password, nickname, age FROM userinfo";
            final Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                pw.println("<tr>");
                pw.println("<td>" + id + "</td>");
                pw.println("<td>" + resultSet.getString("username") + "</td>");
                pw.println("<td>" + resultSet.getString("password") + "</td>");
                pw.println("<td>" + resultSet.getString("nickname") + "</td>");
                pw.println("<td>" + resultSet.getInt("age") + "</td>");
                pw.println("<td><a href='/deleteUser?id=" + id +"'>删除</a></td>");
                pw.println("</tr>");
            }
            pw.println("</table>");
            pw.println("</center>");
            pw.println("</body>");
            pw.println("</html>");
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
        }
    }
}
