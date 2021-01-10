package com.fsq.pobop.ui.recipe.details;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fsq.pobop.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RecipeDetailAdapter extends RecyclerView.Adapter<RecipeDetailAdapter.RecipeDetailHolder> {
    private JSONArray recipeDetails = new JSONArray();

    RecipeDetailAdapter() {
    }

    @NonNull
    @Override
    public RecipeDetailHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_instructions_card, parent, false);
        return new RecipeDetailHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeDetailHolder holder, int position) {
        try {
            JSONObject currentRecipeDetail = recipeDetails.getJSONObject(position);
            holder.stepNum.setText("Step Number: " + String.valueOf(currentRecipeDetail.getInt("number")));
            holder.stepContent.setText(currentRecipeDetail.getString("step"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return recipeDetails.length();
    }

    void setRecipeDetails(JSONArray recipeDetails) {
        this.recipeDetails = recipeDetails;
        notifyDataSetChanged();
    }

    public static class RecipeDetailHolder extends RecyclerView.ViewHolder {
        private TextView stepNum;
        private TextView stepContent;

        RecipeDetailHolder(@NonNull View itemView) {
            super(itemView);
            stepNum = itemView.findViewById(R.id.stepNum);
            stepContent = itemView.findViewById(R.id.stepContent);
        }
    }

}
