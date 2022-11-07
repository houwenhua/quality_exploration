package com.hwh.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CodeBlack {
    static String aaa = "";
    static String DB_URL = "jdbc:mysql://localhost:3306/hwh?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    static String USER = "root";
    static String PASS = "123456";
    {
        aaa = "张三";
        System.out.println("普通代码块");
    }

    static {
        System.out.println("静态代码块");
    }

    public static Connection staticMethod() {
        System.out.println("静态方法" + aaa);
        try {
            return DriverManager.getConnection(DB_URL,USER,PASS);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {

    }
}
