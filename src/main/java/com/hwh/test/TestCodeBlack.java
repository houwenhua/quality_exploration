package com.hwh.test;

import com.hwh.ExplorationBaseData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class TestCodeBlack {
    public static void main(String[] args) throws SQLException {
        Connection conn = CodeBlack.staticMethod();
        // 获得所有表的表名和表注解
        String sql =  "select table_name,table_comment from information_schema.tables where table_schema = 'hwh'";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery(sql);
        while (rs.next()) {
            System.out.println(rs.getString("table_name"));
        }
    }
}
