package com.hwh;

import com.hwh.util.ReadConfigFileUtil;
import com.hwh.vo.ExplorationVo;
import org.apache.log4j.Logger;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

/**
 * 探查内容：
 * 表级：
 * 1.数据量
 * 2.主键字段
 *
 * 字段级：
 * 1.每个字段 distinct 数据量
 * 2.每个字段 非空数据量
 * 3.有值率
 * 4.非空重复率
 */
public class ExplorationBaseData {

    public static Logger log = Logger.getLogger(ExplorationBaseData.class);

    static String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
//    static String DB_URL = "jdbc:mysql://localhost:3306/hwh?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
//    static String USER = "root";
//    static String PASS = "123456";
    //初始化数据库连接
    static Properties p = ReadConfigFileUtil.readConfig();
    static String USER = p.getProperty("user");
    static String PASS = p.getProperty("password");
    static String db_name = p.getProperty("db_name");
    static String DB_URL = "jdbc:mysql://" + p.getProperty("ip") + ":" + p.getProperty("port") + "/" + p.getProperty("db_name") + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getCon() {
        try {
            return DriverManager.getConnection(DB_URL,USER,PASS);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Connection conn = null;
        PreparedStatement ps = null;
        ArrayList<ExplorationVo> list = new ArrayList<ExplorationVo>();
        try{
            log.info("连接数据库......" + db_name);
            conn = getCon();

            // 获得所有表的表名和表注解
            String sql =  "select table_name,table_comment from information_schema.tables where table_schema = '" + db_name + "'";
            ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery(sql);

            log.info("获取数据库所有表及字段......");
            // 展开结果集数据库
            while(rs.next()){
                // 获得所有表的表名和表注解
                String table_name  = rs.getString("table_name");
                String table_comment = rs.getString("table_comment");

               //获取每张表的数据量
                String table_num = null;
                sql = "select count(*) as total from " + table_name;
                ps = conn.prepareStatement(sql);
                ResultSet bj_rs = ps.executeQuery(sql);
                while (bj_rs.next()) {
                    table_num = bj_rs.getString("total");
                }

                //获取主键
                String primary_key = null;
                sql = "select column_name as primary_key from information_schema.key_column_usage " +
                        "where constraint_name = 'primary' " +
                        "and table_name = '" + table_name + "' " +
                        "and constraint_schema = '" + db_name + "'";
                ps = conn.prepareStatement(sql);
                ResultSet zj_rs = ps.executeQuery(sql);
                while(zj_rs.next()) {
                    primary_key = zj_rs.getString("primary_key");
                }

                // 获取字段名 字段注释
                sql = "select column_name,column_comment from information_schema.columns " +
                        " where table_name = '" + table_name + "' " +
                        " and table_schema = '" + db_name + "'";
                ps = conn.prepareStatement(sql);
                ResultSet column_rs = ps.executeQuery(sql);
                while(column_rs.next()) {
                    String column_name  = column_rs.getString("column_name");
                    String column_comment = column_rs.getString("column_comment");

                    //查询每个字段的distinct数据量,非空数据量，有值率，重复率
                    sql = "select distinct_num" +
                            " ,no_null_num " +
                            " ,no_null_num/total as valuable_rate" +
                            " ,if(no_null_num = 0,0,(no_null_num-distinct_num)/no_null_num) as repeat_rate" +
                            " from (" +
                            "   select count(*) as total" +
                            "   ,count(distinct "+ column_name + ") as distinct_num" +
                            "   ,ifnull(sum(case when " + column_name + " is null then 0 else 1 end),0) as no_null_num" +
                            " from " + table_name + ") t";
                    ps = conn.prepareStatement(sql);
                    ResultSet detail_rs = ps.executeQuery(sql);
                    while (detail_rs.next()) {
                        String distinct_num = detail_rs.getString("distinct_num");
                        String no_null_num = detail_rs.getString("no_null_num");
                        String valuable_rate = getPercentage(detail_rs.getFloat("valuable_rate"));
                        String repeat_rate = getPercentage(detail_rs.getFloat("repeat_rate"));

                        // 组合 Vo
                        ExplorationVo ev = new ExplorationVo(
                                db_name
                                ,table_name
                                ,table_comment
                                ,column_name
                                ,column_comment
                                ,primary_key
                                ,table_num
                                ,distinct_num
                                ,no_null_num
                                ,valuable_rate
                                ,repeat_rate);
                        list.add(ev);
                        // 打印探查的对象
                        //log.info(ev);
                    }
                }
                log.info("探查已完成表：" + table_name);
            }

            //将探查结果插入到数据库表：quality_exploration
            sql = "insert into quality_exploration values(?,?,?,?,?,?,?,?,?,?,?,?);";
            ps = conn.prepareStatement(sql);
            //遍历探查结果
            for(ExplorationVo ev : list) {
                ps.setString(1,getSqlCurrentTime());
                ps.setString(2,ev.getDb_name());
                ps.setString(3,ev.getTable_name());
                ps.setString(4,ev.getTable_comment());
                ps.setString(5,ev.getColumn_name());
                ps.setString(6,ev.getColumn_comment());
                ps.setString(7,ev.getPrimary_key());
                ps.setString(8,ev.getTable_num());
                ps.setString(9,ev.getDistinct_num());
                ps.setString(10,ev.getNo_null_num());
                ps.setString(11,ev.getValuable_rate());
                ps.setString(12,ev.getRepeat_rate());
                ps.addBatch();
            }
            log.info("探查结果插入数据库表......quality_exploration");
            ps.executeBatch();
            // 完成后关闭
            rs.close();
            ps.close();
            conn.close();
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }finally{
            // 关闭资源
            try{
                if(ps != null)
                    ps.close();
            }catch(SQLException se2){
            }
            try{
                if(conn != null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
       log.info("数据探查结束......拜拜");
    }

    /**
     * 获取当前时间的字符串
     * @return String
     */
    public static String getSqlCurrentTime() {
        Date date = new Date();
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }

    /**
     * 四舍五入转换成百分数
     * @return
     */
    public static String getPercentage(Float number) {
        int int_num = Math.round(number * 100);
        return int_num + "%";
    }
}
