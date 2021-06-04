package com.tdj.spring;

import com.tdj.spring.util.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class AppTest {

    @Test
    public void testHello() {
        try {
            RedisUtil.lock("测试21618383834001", "10.100.2.507079", 300000);
        } catch (Throwable e) {
            e.printStackTrace();
        }

//        RedisUtil.unlock("测试21618383834001", "10.100.2.507079");
    }
}
