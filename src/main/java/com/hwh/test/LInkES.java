package com.hwh.test;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.*;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.client.RequestOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class LInkES {
    public static RestHighLevelClient client = new RestHighLevelClient(
            RestClient.builder(
                    new HttpHost("192.168.1.101", 9200, "http")));


    public static void main(String[] args) throws IOException {
//        String index = "student";
//        int from = 0;
//        int to = 5;
        testSearch();
    }


    public static void testSearch() throws IOException {
        SearchRequest searchRequest = new SearchRequest("student");
        // 构建搜索条件
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.highlighter();
        // 查询条件，我们可以使用 QueryBuilders 工具来实现
        // QueryBuilders.termQuery 精确
        // QueryBuilders.matchAllQuery() 匹配所有
        //TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("stu_name", "张三");
        //匹配多个值
        TermsQueryBuilder termsQueryBuilder = QueryBuilders.termsQuery("stu_name", "张三", "李四");
        //匹配所有数据
        //MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();


        sourceBuilder.query(termsQueryBuilder);
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println(JSON.toJSONString(searchResponse.getHits()));
        System.out.println("=================================");
        for (SearchHit documentFields : searchResponse.getHits().getHits()) {
            System.out.println(documentFields.getSourceAsMap());
        }
        client.close();
    }

    public static List<Map<String,Object>> readIndex(QueryBuilder query, String index, int from, int size) {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(query).from((from - 1) * size).size(size);
        SearchRequest searchRequest = new SearchRequest(index).source(sourceBuilder);
        SearchResponse response = null;
        try {
            response = client.search(searchRequest, RequestOptions.DEFAULT);
            List<Map<String, Object>> data = new ArrayList<>();
            for(SearchHit hit : response.getHits().getHits()){
                Map<String, Object> map = hit.getSourceAsMap();
                data.add(map);
            }
            return data;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
