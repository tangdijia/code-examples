package com.tdj.nebula.controller;


import com.tdj.nebula.thread.ExeThread;
import com.vesoft.nebula.client.graph.NebulaPoolConfig;
import com.vesoft.nebula.client.graph.data.HostAddress;
import com.vesoft.nebula.client.graph.data.ResultSet;
import com.vesoft.nebula.client.graph.data.ValueWrapper;
import com.vesoft.nebula.client.graph.net.NebulaPool;
import com.vesoft.nebula.client.graph.net.Session;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

@Slf4j
@RestController
public class MController {

    @RequestMapping(value = "/m")
    public void single() {
        NebulaPoolConfig nebulaPoolConfig = new NebulaPoolConfig();
        nebulaPoolConfig.setMaxConnSize(10);
        List<HostAddress> addresses = Arrays.asList(
                new HostAddress("10.100.2.243", 9669),
                new HostAddress("10.100.2.244", 9669),
                new HostAddress("10.100.2.245", 9669));
        NebulaPool pool = new NebulaPool();
        try {
            pool.init(addresses, nebulaPoolConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }

        int size = 2000;
        int partitionSize = 250;
        int threadNum = 8;

        ExecutorService executorService = Executors.newFixedThreadPool(threadNum);

        List<String> qglList = new ArrayList<>();
        for (int i = 10000000; i < 10000000 + size; i++) {
            String gql = "use transaction_space;" +
                    "MATCH (v) --> (v2) where id(v) == '" + i + "' RETURN id(v2);";
            qglList.add(gql);
        }
        List<List<String>> plists = ListUtils.partition(qglList, partitionSize);

        if (plists.size() != threadNum) {
            System.out.println(plists.size());
            System.out.println(threadNum);
            return;
        }


        List<ExeThread> threads = new ArrayList<>();
        for (int i = 0; i < threadNum; i++) {
            ExeThread exeThread = new ExeThread(pool, plists.get(i));
            threads.add(exeThread);
        }
        //中转账户
        List<String> transferAccount = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        try {
            List<Future<List<ResultSet>>> futures = executorService.invokeAll(threads);
            for (Future<List<ResultSet>> result : futures) {
                List<ResultSet> resultSets = result.get();
                for (ResultSet resultSet : resultSets) {
                    for (int i = 0; i < resultSet.rowsSize(); i++) {
                        ResultSet.Record record = resultSet.rowValues(i);
                        String v = record.values().get(0).asString();
                        transferAccount.add(v);
                    }
                }
            }
        } catch (InterruptedException | ExecutionException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        log.info("all cost: {}", endTime - startTime);
        pool.close();

        log.info("asdf");
    }
}