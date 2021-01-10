package com.fsq.pobop.data;

import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import java.util.List;

public interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(T entity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<T> entities);
}
