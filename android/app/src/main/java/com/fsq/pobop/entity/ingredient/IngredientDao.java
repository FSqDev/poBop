package com.fsq.pobop.entity.ingredient;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.fsq.pobop.data.BaseDao;

import java.util.List;

@Dao
public interface IngredientDao extends BaseDao<Ingredient> {

    @Query("SELECT * FROM ingredient WHERE id = :id")
    LiveData<Ingredient> findById(int id);

    @Query("SELECT * FROM ingredient WHERE dirty <> 2 ORDER BY name")
    LiveData<List<Ingredient>> findAll();

    @Query("SELECT * FROM ingredient")
    List<Ingredient> findAllIngredients();

    @Query("SELECT * FROM ingredient WHERE dirty = 1")
    List<Ingredient> findAllDirty();

    @Query("SELECT id FROM ingredient WHERE dirty = 2")
    List<String> findAllDeleted();

    @Query("DELETE FROM ingredient WHERE dirty = 2")
    void deleteAllDeleted();

    @Query("UPDATE ingredient SET dirty = 2 WHERE id = :id")
    void markDeleted(String id);
}
