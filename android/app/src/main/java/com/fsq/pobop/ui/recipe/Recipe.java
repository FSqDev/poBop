package com.fsq.pobop.ui.recipe;
import android.graphics.Bitmap;

import com.fsq.pobop.entity.ingredient.Ingredient;
import java.util.List;

public class Recipe {

    private String name;
    private String imageUrl;
    private int likes;
    private List<Ingredient> missingIngredients;
    private int missingIngredientCount;
    private String summary;
    private int recipeId;
    private Bitmap bmp;

    public Recipe(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public Recipe(
            String name,
            String imageUrl,
            int likes,
            int missingIngredientCount,
            int recipeId

    ) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.likes = likes;
        this.missingIngredientCount = missingIngredientCount;
        this.recipeId = recipeId;
    }

    public Bitmap getBmp() {
        return bmp;
    }

    public void setBmp(Bitmap bmp) {
        this.bmp = bmp;
    }


    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public List<Ingredient> getMissingIngredients() {
        return missingIngredients;
    }

    public void setMissingIngredients(List<Ingredient> missingIngredients) {
        this.missingIngredients = missingIngredients;
    }

    public int getMissingIngredientCount() {
        return missingIngredientCount;
    }

    public void setMissingIngredientCount(int missingIngredientCount) {
        this.missingIngredientCount = missingIngredientCount;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "name='" + name + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
