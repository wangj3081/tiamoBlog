package com.tiamo;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Auther: wangjian
 * @Date: 2019-03-12 15:10:13
 */
@SpringBootApplication
//@EnableSwagger2  // 加载Swagger
@EnableSwagger2Doc
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }
}
