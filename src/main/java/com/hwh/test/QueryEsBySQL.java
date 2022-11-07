package com.hwh.test;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryEsBySQL {
    public static JSONArray hits = null;
    public static int idx = 0;
    private static String esUrl = "http://192.168.1.101:9200/_sql?format=json";
    public static void main(String[] args) {
       queryEs("select * from student where stu_name in ('张三','王五')");
    }

    public static void queryEs(String sql) {
        Map query = new HashMap<>();
        query.put("query", sql);
        JSONObject response = JSONUtil.parseObj(HttpUtil.post(esUrl, JSONUtil.toJsonStr(query)));
        Object rows = response.get("rows");
        System.out.println(rows.toString());
        System.out.println(response.toString());
    }

    /**
     * esSql查询 sql语句中需要用到参数的地方， 请用#{param} 的形式修饰，#{}符号中间的单词就是第二个参数中的某个键
     * @param sql sql语句
     * @param params 参数 一个key对应一个参数
     * @param className 目标类的class
     * @return
     */
    public static List query(String sql, Map params, Class className) {
        if (StrUtil.isBlank(sql)) {
            return new ArrayList<>();
        }
        System.out.println("es查询sql入参 >>> {}" + sql);
        System.out.println("es查询param入参 >>> {}" + JSONUtil.toJsonStr(params));
        //转换sql参数
        /*if (params != null && params.size() != 0) {
            params.keySet().forEach(key -> {
                sql.replace("#{" + key + "}", params.toString());
            });
        }*/
        Map query = new HashMap<>();
        query.put("query", sql);
        JSONObject response = JSONUtil.parseObj(HttpUtil.post(esUrl, JSONUtil.toJsonStr(query)));
        System.out.println(response.toString());
        /*List columns = JSONUtil.toList(JSONUtil.parseArray(response.getStr("columns")), Columns.class);
        List rows = JSONUtil.toList(JSONUtil.parseArray(response.getStr("rows")), List.class);
       // List rows = JSONObject.parseArray(response.getString("rows"), List.class);
        EsModel esModel = new EsModel(columns, rows);
        return esModelToJavaObject(esModel, className);*/
        return null;

    }
}

