package com.accp.caro2o;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@MapperScan({"com.accp.caro2o.mapper"})
public class DemoApplication {
    private static final Logger logger = LoggerFactory.getLogger(DemoApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
        logger.info("========================启动完毕========================");
    }

}
