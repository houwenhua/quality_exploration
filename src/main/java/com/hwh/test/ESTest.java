package com.hwh.test;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

public class ESTest {
    private JSONArray hits = null;
    private int idx = 0;
    String[] fields = new String[]{"sgtxm","cyrq","jcpc","jclx","xb","rcjclx","lxfs","hsjclx","xm","yblx","last_modify_time","zjhm","xzqh","cyry","jcjgou","cydd","idcard_hash","wyj","lrr","jcrq","jcjg","jcjglrsj","hyfs","zjlx","ssrq","type","etl_time"};



    public static void main(String[] args) {
        new ESTest().loadData();
    }
    private void loadData() {
        try {
            //String end_time = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))+"T12:00:00";
            //String start_time= LocalDate.now().plusDays(-1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))+"T12:00:00";

            String start_time = "2022-07-17T21:00:00";
            String end_time   = "2022-07-17T21:10:01";


            String json = "{\"track_total_hits\": true,\"query\": {\"bool\": {\"must\": [{\"range\": {\"cyrq\": {\"gte\": \""+start_time+"\",\"lt\": \""+end_time+"\"}}}," +
                    "{\"term\": {\"hsjclx\": \"1\"}}],\"must_not\": [],\"should\": []}},  \"from\": 0,  \"size\": 2000000}";


            JSONObject body = JSONUtil.parseObj(json);
            HttpRequest request = HttpUtil.createPost("http://172.34.47.198:9200/fact_cyxx/_search");
            request.basicAuth("elastic", "Zhrc@2021");
            request.body(body);
            HttpResponse response = request.execute();
            String responseBody = response.body();
            JSONObject object = JSONUtil.parseObj(responseBody);
            hits = (JSONArray)object.getByPath("$.hits.hits");
            System.out.println(hits);
        } catch (Exception e) {
            System.out.println( "接口调用异常 " + e.getMessage() );
        }
    }

}
