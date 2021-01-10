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

    @Query("SELECT * FROM ingredient")
    LiveData<List<Ingredient>> findAll();

    @Query("SELECT * FROM ingredient WHERE dirty <> 0")
    List<Ingredient> findAllDirty();

}
