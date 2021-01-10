package com.fsq.pobop.ui.qr;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.fsq.pobop.entity.ingredient.Ingredient;
import com.fsq.pobop.entity.ingredient.IngredientRepository;

public class QRViewModel extends AndroidViewModel {

    private IngredientRepository ingredientRepository;

    public QRViewModel(@NonNull Application application) {
        this(application, new IngredientRepository(application));
    }

    public QRViewModel(@NonNull Application application, IngredientRepository ingredientRepository) {
        super(application);
        this.ingredientRepository = ingredientRepository;
    }

    public void add(Ingredient ingredient){
        ingredientRepository.insert(ingredient);
    }
}
