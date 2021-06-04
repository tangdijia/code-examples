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

    Logger logger = LogManager.getLogger("Test");

    @RequestMapping("/")
    String home_1() {
        Event event = new Event("hello world");
        long s = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            log.info("{},{},{},{}", i, event.getClass().getName(), event.getTraceTime(), event.getTraceID());
        }
        logger.info("cost: {}", System.currentTimeMillis() - s);
        return "Hello World!";
    }

    public static void main(String[] args) {
        SpringApplication.run(Example.class, args);

        URL classPath = Thread.currentThread().getContextClassLoader().getResource("");
        String proFilePath = classPath.toString();
        System.out.println(proFilePath);
    }
}
