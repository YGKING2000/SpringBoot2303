package com.example.springboot2303.entity;

/**
 * @Description
 * @ClassName Demo
 * @Author YGKING e-mail:hrd18960706057@163.com
 * @Date 2023/04/15 01:11
 * @Version 1.0
 */
public class Demo {
    public static void main(String[] args) {
        User user = Demo.user;
        Demo demo = new Demo();
        demo.test();
        System.out.println(user == Demo.user);
    }

    public void test() {
        user = new User();
    }

    private static User user;
    static {
        user = new User();
    }
}
