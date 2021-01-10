package com.fsq.pobop.ui.home;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fsq.pobop.entity.ingredient.Ingredient;
import com.fsq.pobop.entity.ingredient.IngredientRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class HomeViewModel extends AndroidViewModel {

    private IngredientRepository ingredientRepository;

    public HomeViewModel(@NonNull Application application) {
        this(application, new IngredientRepository(application));
    }

    public HomeViewModel(@NonNull Application application, IngredientRepository ingredientRepository) {
        super(application);
        this.ingredientRepository = ingredientRepository;
    }

}