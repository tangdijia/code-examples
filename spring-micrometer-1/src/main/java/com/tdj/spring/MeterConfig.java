package com.tdj.spring;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.function.ToDoubleFunction;

@Component
public class MeterConfig {

    @Autowired
    private MeterRegistry registry;

    @Bean(name = "indexCounter")
    public Counter counter02() {
        return registry.counter("app_method_count", "method", "IndexController.index");
    }

    @Bean(name = "gauge01")
    public Number gauge01() {
        return registry.gauge("app_method_random_value", getRandom());
    }

    private Number getRandom() {
        Random random = new Random();
        return random.nextInt();
    }


    @Bean(name = "gauge02")
    public String gauge02() {
        String name = "hello world";
        return registry.gauge("app_method_value", name, new ToDoubleFunction<String>() {

            @Override
            public double applyAsDouble(String value) {
                System.out.println(value);
                return 99.0;
            }
        });
    }

}
