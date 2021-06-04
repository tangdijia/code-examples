package com.tdj.nebula.test;

import com.vesoft.nebula.client.graph.data.ResultSet;
import com.vesoft.nebula.client.graph.net.NebulaPool;
import com.vesoft.nebula.client.graph.net.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

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
        for (String gql : gqls) {
            ResultSet resultSet = session.execute(gql);
            if (resultSet.rowsSize() > 0) {
                resultSets.add(resultSet);
            }
        }
        session.release();
        return resultSets;
    }
}
