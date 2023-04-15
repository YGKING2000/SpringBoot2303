package com.example.springboot2303.controller;

import com.example.springboot2303.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @Description User实体类
 * @ClassName UserController
 * @Author YGKING e-mail:hrd18960706057@163.com
 * @Date 2023/04/14 20:15
 * @Version 1.0
 */
@Controller
public class UserController {
    private static File userDir;

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
        File file = new File(userDir, username + ".obj");
        if (!file.exists()) {
            try {
                response.sendRedirect("/user_not_exist.html");
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try (
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
        ) {
            User user = (User) ois.readObject();
            assert password != null;
            if (!password.equals(user.getPassword())) {
                response.sendRedirect("/pwd_err.html");
            } else {
                response.sendRedirect("/login_success.html");
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
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
    public void regUser(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String nickname = request.getParameter("nickname");
        String ageStr = request.getParameter("age");
        System.out.println(username + ", " + password + ", " + nickname + ", " + ageStr);
        if (username == null || username.isEmpty() ||
                password == null || password.isEmpty() ||
                nickname == null || nickname.isEmpty() ||
                ageStr == null || !ageStr.matches("[0-9]+")) {
            try {
                response.sendRedirect("/reg_illegal_input.html");
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        assert ageStr != null;
        int age = Integer.parseInt(ageStr);

        User user = new User(username, password, nickname, age);
        File file = new File(userDir, username + ".obj");
        if (file.exists()) {
            try {
                response.sendRedirect("/user_exist.html");
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try (
                FileOutputStream fos = new FileOutputStream(file);
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            oos.writeObject(user);
            userDir = new File("./userDir");
            response.sendRedirect("/reg_success.html");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
