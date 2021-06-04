package org.example.logback;

import cn.com.bsfit.sd.core.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.web.bind.annotation.*;

import java.net.URL;
import java.net.URLClassLoader;

@RestController
@EnableAutoConfiguration
@Slf4j
public class Example {

    @RequestMapping("/")
    String home() {
        Event event = new Event("jaldjflasjdfklajsldfsl");
        log.info(Thread.currentThread().getName());
        long s = System.currentTimeMillis();
        for (int i = 0; i < 1; i++) {
            log.info("{},{},{} Hello World!", event.getClass().getName(), event.getTraceTime(), event.getTraceID());
        }
        System.out.println(System.currentTimeMillis() - s);
        return "Hello World!";
    }

    public static void main(String[] args) {
        SpringApplication.run(Example.class, args);

        URL classPath = Thread.currentThread().getContextClassLoader().getResource("");
        String proFilePath = classPath.toString();
        System.out.println(proFilePath);

    }
}
