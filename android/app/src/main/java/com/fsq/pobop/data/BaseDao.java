package com.fsq.pobop.data;

import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

public interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(T entity);
}
