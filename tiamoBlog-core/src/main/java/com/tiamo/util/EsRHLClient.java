package com.tiamo.util;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;



/**
 * Es 高阶连接类
 * Created by wangjian on 2019/3/10.
 */
public class EsRHLClient {

    // 集群名
//    private static String clusterName = "es_66";
    private static String clusterName = "es6-4";
    // ip
//    private static String ip = "192.168.134.130";
    private static String ip = "192.168.158.129";
    // 端口
    private static String port = "9200";

    private static RestHighLevelClient client;

    public static RestHighLevelClient getEsClient() {
        if (client == null) {
            RestHighLevelClient restClient = new RestHighLevelClient(RestClient.builder(new HttpHost(ip, Integer.parseInt(port))));
           client = restClient;

        }

        return client;
    }
}
