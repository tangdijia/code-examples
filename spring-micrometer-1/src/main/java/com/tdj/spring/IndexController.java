package com.tdj.spring;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class IndexController {

    @Autowired
    MeterRegistry registry;

    @Autowired
    private Counter indexCounter;

    @RequestMapping(value = "/index")
    public Object index() {
        try {
            indexCounter.increment();
        } catch (Exception e) {
            return e;
        }
        return indexCounter.count();
    }
}