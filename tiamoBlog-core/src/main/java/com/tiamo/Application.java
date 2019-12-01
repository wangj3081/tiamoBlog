package com.tiamo;

import com.cxytiandi.elasticjob.annotation.EnableElasticJob;
import com.tiamo.util.EsRHLClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by wangjian on 2019/3/9.
 */
@SpringBootApplication
@EnableElasticJob // 使用 elasticJob
public class Application {


    public static void main(String[] args) {
        SpringApplication.run(Application.class);
        EsRHLClient.getEsClient();
    }

}
