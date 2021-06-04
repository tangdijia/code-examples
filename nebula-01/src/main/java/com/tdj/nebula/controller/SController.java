package com.tdj.nebula.controller;


import com.vesoft.nebula.client.graph.NebulaPoolConfig;
import com.vesoft.nebula.client.graph.data.HostAddress;
import com.vesoft.nebula.client.graph.data.ResultSet;
import com.vesoft.nebula.client.graph.net.NebulaPool;
import com.vesoft.nebula.client.graph.net.Session;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class SController {

    @RequestMapping(value = "/s")
    public void single() {
        List<String> vertexs = new ArrayList<>();
        for (int i = 10000000; i < 10020000; i++) {
            vertexs.add(i + "");
        }
        long maxTime = 0L;
        long minTime = Long.MAX_VALUE;
        NebulaPoolConfig nebulaPoolConfig = new NebulaPoolConfig();
        nebulaPoolConfig.setMaxConnSize(10);
        List<HostAddress> addresses = Arrays.asList(
                new HostAddress("10.100.2.243", 9669),
                new HostAddress("10.100.2.244", 9669),
                new HostAddress("10.100.2.245", 9669));
        NebulaPool pool = new NebulaPool();
        Session session = null;
        try {
            pool.init(addresses, nebulaPoolConfig);
            session = pool.getSession("root", "nebula", false);
            long startTime = System.currentTimeMillis();
            for (String vid : vertexs) {
                String gql = "use transaction_space;" +
                        "MATCH (v) --> (v2) where id(v) == '" + vid + "' RETURN id(v2);";
                long s = System.currentTimeMillis();
                ResultSet resultSet = session.execute(gql);
                long c = System.currentTimeMillis() - s;
                if (maxTime < c) {
                    maxTime = c;
                }
                if (minTime > c) {
                    minTime = c;
                }
            }
            long endTime = System.currentTimeMillis();
            long costTime = endTime - startTime;
            long size = vertexs.size();
            System.out.println("count: " + size);
            System.out.println("cost: " + costTime);
            System.out.println("avgTime: " + costTime / size);
            System.out.println("maxTime: " + maxTime);
            System.out.println("minTime: " + minTime);
            System.out.println("tps: " + size / (costTime / 1000));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.release();
            }
            pool.close();
        }
    }
}