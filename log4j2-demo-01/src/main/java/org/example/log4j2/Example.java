package org.example.log4j2;

import cn.com.bsfit.sd.core.Event;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@EnableAutoConfiguration
@Log4j2
public class Example {

    Logger logger2 = LogManager.getLogger("DataTraceLog");

    @RequestMapping("/1")
    String home_1() {
        Event event = new Event("hello world");
        long s = System.currentTimeMillis();
        for (int i = 0; i < 5000000; i++) {
            logger2.info("{},{},{}", event.getClass().getName(), event.getTraceTime(), event.getTraceID());
        }
        System.out.println(System.currentTimeMillis() - s);
        return "Hello World!";
    }

    ExecutorService executor = Executors.newSingleThreadExecutor();

    @RequestMapping("/2")
    String home_2() {
        Event event = new Event("hello world");
        long s = System.currentTimeMillis();
        for (int i = 0; i < 5000000; i++) {
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    logger2.info("{},{},{}", event.getClass().getName(), event.getTraceTime(), event.getTraceID());
                }
            });
        }
        System.err.println(System.currentTimeMillis() - s);
        return "Hello World!";
    }

    public static void main(String[] args) {
        SpringApplication.run(Example.class, args);

        URL classPath = Thread.currentThread().getContextClassLoader().getResource("");
        String proFilePath = classPath.toString();
        System.out.println(proFilePath);
    }
}
