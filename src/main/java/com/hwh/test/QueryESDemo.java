package com.hwh.test;


import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class QueryESDemo {
    private int idx = 0;
    private String startDate = null;
    private String endDate = null;

    public static void main(String[] args) {
        JSONArray objects = new QueryESDemo().loadDataByIdCardHash("330afa3a5945d2da1648add37aa36a8c12ae686fa5a109e79bdbc3a820204ab6");
        System.out.println(objects.toString());
    }

    private JSONArray loadDataByIdCardHash(String card_hash) {
        try {
            if(startDate == null || endDate == null ){
                Date currentTime = new Date();
                Calendar c = Calendar.getInstance();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                endDate = sdf.format(currentTime);
                // 前7天的数据
                c.add(Calendar.DAY_OF_MONTH, -15);
                startDate= sdf.format((c.getTime()));
                System.out.println( "gle : "+ startDate+",lt:"+ endDate);
            }

            String json = "{\"query\":{\"bool\":{\"must\":[{\"match\":{\"idcard_hash\":"+card_hash+"}},{\"range\":{\"cyrq\":{\"gte\":"+startDate+",\"lt\":"+endDate+"}}}],\"must_not\":[],\"should\":[]}},\"from\":0,\"size\":1000,\"sort\":[{\"jcrq\":{\"order\":\"desc\"}}],\"aggs\":{}}";
            JSONObject body = JSONUtil.parseObj(json);
            HttpRequest request = HttpUtil.createPost("http://172.34.47.198:9200/fact_cyxx/_search");
            request.basicAuth("elastic", "Zhrc@2021");
            request.header("Accept","application/json, text/javascript, */*; q=0.01");
            request.header("Accept-Encoding", "gzip, deflate");
            request.body(body);
            HttpResponse response = request.execute();
            String responseBody = response.body();
            System.out.println( "数据结果byIdCard: " + responseBody );
            JSONObject object = JSONUtil.parseObj(responseBody);
            JSONArray hits = (JSONArray)object.getByPath("$.hits.hits");
            return hits;
        } catch (Exception e) {
            System.out.println( "loadDataByIdCardHash 接口调用异常 " + e.getMessage() );
        }
        return null;
    }



}
