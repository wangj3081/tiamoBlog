package com.tiamo.util;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Es 连接类
 * Created by wangjian on 2019/3/10.
 */
public class EsClient {

    // 集群名
    private static String clusterName = "es6.5";
    // ip
    private static String ip = "192.168.158.134";
    // 端口
    private static String port = "9300";

    private static Client client;

    public static Client getEsClient() {
        if (client == null) {

            Settings settings = Settings.builder()
                    .put("cluster.name", clusterName)
                    .put("client.transport.sniff", true).build();
            PreBuiltTransportClient transportClient = new PreBuiltTransportClient(settings);

            try {
//                transportClient.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(ip), Integer.parseInt(port)));
                transportClient.addTransportAddress(new TransportAddress(InetAddress.getByName(ip), Integer.parseInt(port)));
                client = transportClient;
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }

        return client;
    }
}
