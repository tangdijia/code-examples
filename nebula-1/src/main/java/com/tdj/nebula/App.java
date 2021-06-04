package com.tdj.nebula;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RestController;

import java.net.URL;

@RestController
@EnableAutoConfiguration
@ComponentScan("com.tdj.nebula")
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);

        URL classPath = Thread.currentThread().getContextClassLoader().getResource("");
        String proFilePath = classPath.toString();
        System.out.println(proFilePath);

    }
}
