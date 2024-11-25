package com.qiu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * @author qiu
 * @version 1.0
 */
@SpringBootApplication
@ConfigurationPropertiesScan("com.qiu.mapper")
@MapperScan("com.qiu.mapper")

public class BlogAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(BlogAdminApplication.class,args);
    }
}
