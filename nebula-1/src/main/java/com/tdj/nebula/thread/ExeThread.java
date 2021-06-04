package com.tdj.nebula.thread;

import com.vesoft.nebula.client.graph.data.ResultSet;
import com.vesoft.nebula.client.graph.net.NebulaPool;
import com.vesoft.nebula.client.graph.net.Session;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@Slf4j
public class ExeThread implements Callable<List<ResultSet>> {

    private NebulaPool pool;

    private List<String> gqls;

    public ExeThread(NebulaPool pool, List<String> gqls) {
        this.pool = pool;
        this.gqls = gqls;
    }

    @Override
    public List<ResultSet> call() throws Exception {
        List<ResultSet> resultSets = new ArrayList<>();
        Session session = pool.getSession("root", "nebula", false);
        long startTime = System.currentTimeMillis();
        for (String gql : gqls) {
            ResultSet resultSet = session.execute(gql);
            if (resultSet.rowsSize() > 0) {
                resultSets.add(resultSet);
            }
        }
        long endTime = System.currentTimeMillis();
        log.info("cost: {}", endTime - startTime);
        session.release();
        return resultSets;
    }
}
