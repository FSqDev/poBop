package com.fsq.pobop.data;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class BaseRepository<T, D extends BaseDao<T>> {

    protected D dao;

    private Executor executor;

    public BaseRepository(D dao) {
        this.dao = dao;
        this.executor = Executors.newSingleThreadExecutor();
    }

    public void insert(T entity) {
        D localDao = dao;
        executeOffMainThread(() -> {
            localDao.insert(entity);
        });
    }

    protected void executeOffMainThread(Runnable command) {
        if (executor != null) {
            executor.execute(command);
        } else {
            command.run();
        }
    }
}
