package com.fsq.pobop.ui.recipe;

import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fsq.pobop.R;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import java.util.List;
import java.util.concurrent.Executors;
import android.os.Handler;

import android.util.Log;

public class RecipeRecViewAdapter extends RecyclerView.Adapter<RecipeRecViewAdapter.RecipeHolder> {

    private List<Recipe> recipeList = new ArrayList<>();
    private OnItemClickListener itemClickListener;

    RecipeRecViewAdapter(OnItemClickListener onItemClickListener) {
        this.itemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public RecipeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_card, parent, false);
        return new RecipeHolder(itemView, (OnItemClickListener) itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeHolder holder, int position) {
        Recipe currentRecipe = recipeList.get(position);
        holder.name.setText(currentRecipe.getName());
        holder.rating.setText("Likes: " + currentRecipe.getLikes());
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                URL url = new URL(currentRecipe.getImageUrl());
                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        currentRecipe.setBmp(bmp);
                        holder.img.setImageBitmap(bmp);
                    }
                });

            } catch (IOException e) {
                Log.d("recipe", "Couldn't fetch recipe photo: " + e.getMessage());
            }

        });
    }

    public Recipe getRecipeAt(int position) {
        return this.recipeList.get(position);
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public void setProjectListItems(List<Recipe> recipes) {
        this.recipeList = recipes;
        notifyDataSetChanged();
    }

    public static class RecipeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView name;
        private ImageView img;
        private TextView rating;
        private OnItemClickListener onItemClickListener;

        RecipeHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            name = itemView.findViewById(R.id.stepNum);
            img = itemView.findViewById(R.id.imgRecipe);
            rating = itemView.findViewById(R.id.stepContent);
            this.onItemClickListener = onItemClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(getAdapterPosition());
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
