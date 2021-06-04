package com.tdj.spring;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.web.bind.annotation.*;

import java.net.URL;
import java.net.URLClassLoader;

@RestController
@EnableAutoConfiguration
public class Example {

    @RequestMapping("/")
    String home() {
        return "Hello World!";
    }

    @RequestMapping(value = "/predict/signle/A",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.POST)
    String home1() {
        return "Hello World A!";
    }

    @RequestMapping(value = "/predict/signle/B",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.POST)
    String home2() {
        return "Hello World B!";
    }

    @RequestMapping(value = "/predict/signle/C",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.POST)
    String home3() {
        return "Hello World C!";
    }

    public static void main(String[] args) {
        SpringApplication.run(Example.class, args);

        URL classPath = Thread.currentThread().getContextClassLoader().getResource("");
        String proFilePath = classPath.toString();
        System.out.println(proFilePath);

    }

}
