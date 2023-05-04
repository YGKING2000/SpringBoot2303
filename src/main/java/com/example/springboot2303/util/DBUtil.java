package com.example.springboot2303.util;

import com.alibaba.druid.pool.DruidDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;

/**
 * @author YGKING e-mail:hrd18960706057@163.com
 * @version 1.0
 * @description
 * @className DBUtil
 * @date 2023/04/27 17:34
 */
public class DBUtil {
    private static DruidDataSource dataSource;
    static {
        initDataSource();
    }

    private static void initDataSource() {
        dataSource = new DruidDataSource();
        dataSource.setUsername("root");
        dataSource.setPassword("123456");
        dataSource.setUrl("jdbc:mysql://localhost:3306/birdbootdb?characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&rewriteBatchedStatements=true&allowMultiQueries=true");
        dataSource.setMaxActive(30);// 最大连接数
        dataSource.setInitialSize(5);// 初始化连接数
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
