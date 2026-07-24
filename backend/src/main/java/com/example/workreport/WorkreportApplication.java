package com.example.workreport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class WorkreportApplication {

    public static void main(String[] args) {
        // Đặt headless(false) để hỗ trợ Java Desktop Swing GUI
        System.setProperty("java.awt.headless", "false");
        new SpringApplicationBuilder(WorkreportApplication.class)
                .headless(false)
                .run(args);
    }

}