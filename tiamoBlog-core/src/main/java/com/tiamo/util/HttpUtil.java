package com.tiamo.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Http 请求
 */
public class HttpUtil {


  /**
   * get 请求
   * @param url 地址
   * @return
   */
  public static String doGet(String url) {
    HttpClient httpClient = HttpClients.createDefault();
    HttpGet httpGet = new HttpGet(url);
    try {
      HttpResponse execute = httpClient.execute(httpGet);
      if (HttpStatus.SC_OK == execute.getStatusLine().getStatusCode()) {
        HttpEntity entity = execute.getEntity();
        return EntityUtils.toString(entity);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

}
