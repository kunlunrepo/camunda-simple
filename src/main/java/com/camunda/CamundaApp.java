package com.camunda;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class CamundaApp {
    public static void main(String[] args) {
        log.info("【CamundaApp】启动中。。。");
        SpringApplication.run(CamundaApp.class, args);
        log.info("【CamundaApp】启动完成。。。");
    }
}