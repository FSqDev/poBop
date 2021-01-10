package com.fsq.pobop.ui.pantry;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.fsq.pobop.entity.ingredient.Ingredient;
import com.fsq.pobop.entity.ingredient.IngredientRepository;

import java.util.List;

public class PantryViewModel extends AndroidViewModel {

    private IngredientRepository ingredientRepository;

    public PantryViewModel(@NonNull Application application) {
        this(application, new IngredientRepository(application));
    }

    public PantryViewModel(@NonNull Application application, IngredientRepository ingredientRepository) {
        super(application);
        this.ingredientRepository = ingredientRepository;
    }

    public LiveData<List<Ingredient>> findAll() {
        return ingredientRepository.findAll();
    }

}