package com.fsq.pobop.entity.ingredient;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.fsq.pobop.data.AppDatabase;
import com.fsq.pobop.data.BaseRepository;

import java.util.List;

public class IngredientRepository extends BaseRepository<Ingredient, IngredientDao> {

    public IngredientRepository(Application application) {
        this(AppDatabase.getInstance(application).ingredientDao());
    }

    public IngredientRepository(IngredientDao dao) {
        super(dao);
    }

    public LiveData<Ingredient> findById(int id) {
        return dao.findById(id);
    }

    public LiveData<List<Ingredient>> findAll() {
        return dao.findAll();
    }

    public List<Ingredient> findAllDirty() {
        return dao.findAllDirty();
    }

    public List<Ingredient> findAllIngredients() {
        return dao.findAllIngredients();
    }
}
