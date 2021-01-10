package com.fsq.pobop.ui.recipe;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fsq.pobop.R;
import com.fsq.pobop.ui.pantry.IngredientAdapter;

import java.util.ArrayList;
import java.util.List;

public class RecipeRecViewAdapter extends RecyclerView.Adapter<RecipeRecViewAdapter.RecipeHolder> {

    private List<Recipe> recipeList = new ArrayList<>();
    private IngredientAdapter.OnItemClickListener itemClickListener;

    RecipeRecViewAdapter(IngredientAdapter.OnItemClickListener onItemClickListener) {
        this.itemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public RecipeRecViewAdapter.RecipeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_card, parent, false);
        return new RecipeHolder(itemView, (OnItemClickListener) itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeRecViewAdapter.RecipeHolder holder, int position) {
        Recipe currentRecipe = recipeList.get(position);
        holder.name.setText(currentRecipe.getName());
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    void setProjectListItems(List<Recipe> recipes) {
        this.recipeList = recipes;
        notifyDataSetChanged();
    }

    public static class RecipeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView name;
        private ImageView img;
        private RecipeRecViewAdapter.OnItemClickListener onItemClickListener;

        RecipeHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            name = itemView.findViewById(R.id.txtRecipeName);
            img = itemView.findViewById(R.id.imgRecipe);
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
