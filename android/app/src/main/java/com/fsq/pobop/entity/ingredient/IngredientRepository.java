package com.fsq.pobop.entity.ingredient;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.fsq.pobop.data.AppDatabase;
import com.fsq.pobop.data.BaseRepository;

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
}
