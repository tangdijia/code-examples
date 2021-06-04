package com.tdj.nebula.test;

import com.vesoft.nebula.client.graph.NebulaPoolConfig;
import com.vesoft.nebula.client.graph.data.HostAddress;
import com.vesoft.nebula.client.graph.data.ResultSet;
import com.vesoft.nebula.client.graph.data.ValueWrapper;
import com.vesoft.nebula.client.graph.exception.AuthFailedException;
import com.vesoft.nebula.client.graph.exception.IOErrorException;
import com.vesoft.nebula.client.graph.exception.NotValidConnectionException;
import com.vesoft.nebula.client.graph.net.NebulaPool;
import com.vesoft.nebula.client.graph.net.Session;
import org.apache.commons.collections4.ListUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Demo {

    private List<String> vertexs = new ArrayList<>();

    private ExecutorService executorService;

    @Before
    public void init() {
        executorService = Executors.newFixedThreadPool(10);
        vertexs = new ArrayList<>();
        //10000000
        //10100000
        for (int i = 10000000; i < 10000001; i++) {
            vertexs.add(i + "");
        }
//        ListUtils.partition(vertexs, )
    }


    @Test
    public void test() {
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
                        "MATCH (v) --> (v2) where id(v) == '" + vid + "' RETURN v2;";
                long s = System.currentTimeMillis();
                ResultSet resultSet = session.execute(gql);
                printResult(resultSet);
                long c = System.currentTimeMillis() - s;
                if (maxTime < c) {
                    maxTime = c;
                }
                if (minTime > c) {
                    minTime = c;
                }
//                System.out.println(resultSet.toString());
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


    @Test
    public void test2() {
        long maxTime = 0L;
        long minTime = Long.MAX_VALUE;
        NebulaPoolConfig nebulaPoolConfig = new NebulaPoolConfig();
        nebulaPoolConfig.setMaxConnSize(10);
        List<HostAddress> addresses = Arrays.asList(
                new HostAddress("10.100.2.243", 9669),
                new HostAddress("10.100.2.244", 9669),
                new HostAddress("10.100.2.245", 9669));
        NebulaPool pool = new NebulaPool();
        try {
            pool.init(addresses, nebulaPoolConfig);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        Session session = null;
        try {
            session = pool.getSession("root", "nebula", false);
            long startTime = System.currentTimeMillis();
            for (String vid : vertexs) {
                String gql = "use transaction_space;" +
                        "MATCH (v) --> (v2) where id(v) == '" + vid + "' RETURN v2;";
                long s = System.currentTimeMillis();
                ResultSet resultSet = session.execute(gql);
                long c = System.currentTimeMillis() - s;
                if (maxTime < c) {
                    maxTime = c;
                }
                if (minTime > c) {
                    minTime = c;
                }
//                System.out.println(resultSet.toString());
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

    private static void printResult(ResultSet resultSet) throws UnsupportedEncodingException {
        List<String> colNames = resultSet.keys();
        for (String name : colNames) {
            System.out.printf("%15s |", name);
        }
        System.out.println();
        for (int i = 0; i < resultSet.rowsSize(); i++) {
            ResultSet.Record record = resultSet.rowValues(i);
            for (ValueWrapper value : record.values()) {
                if (value.isLong()) {
                    System.out.printf("%15s |", value.asLong());
                }
                if (value.isBoolean()) {
                    System.out.printf("%15s |", value.asBoolean());
                }
                if (value.isDouble()) {
                    System.out.printf("%15s |", value.asDouble());
                }
                if (value.isString()) {
                    System.out.printf("%15s |", value.asString());
                }
                if (value.isTime()) {
                    System.out.printf("%15s |", value.asTime());
                }
                if (value.isDate()) {
                    System.out.printf("%15s |", value.asDate());
                }
                if (value.isDateTime()) {
                    System.out.printf("%15s |", value.asDateTime());
                }
                if (value.isVertex()) {
                    System.out.printf("%15s |", value.asNode());
                }
                if (value.isEdge()) {
                    System.out.printf("%15s |", value.asRelationship());
                }
                if (value.isPath()) {
                    System.out.printf("%15s |", value.asPath());
                }
                if (value.isList()) {
                    System.out.printf("%15s |", value.asList());
                }
                if (value.isSet()) {
                    System.out.printf("%15s |", value.asSet());
                }
                if (value.isMap()) {
                    System.out.printf("%15s |", value.asMap());
                }
            }
            System.out.println();
        }
    }
}
