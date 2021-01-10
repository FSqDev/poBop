package com.fsq.pobop.ui.pantry;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fsq.pobop.R;
import com.fsq.pobop.entity.ingredient.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientHolder> {
    private List<Ingredient> ingredientList = new ArrayList<>();
    private OnItemClickListener itemClickListener;

    IngredientAdapter(OnItemClickListener onItemClickListener) {
        this.itemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public IngredientHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ingredient, parent, false);
        return new IngredientHolder(itemView, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientHolder holder, int position) {
        Ingredient currentIngredient = ingredientList.get(position);
        holder.textViewIngredientName.setText(currentIngredient.getProductName());
        holder.textViewIngredientExpiry.setText(currentIngredient.getExpiryDate().toString());
    }


    @Override
    public int getItemCount() {
        return ingredientList.size();
    }

    void setProjectListItems(List<Ingredient> ingredients) {
        this.ingredientList = ingredients;
        notifyDataSetChanged();
    }

    public static class IngredientHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textViewIngredientName;
        private TextView textViewIngredientExpiry;
        private OnItemClickListener onItemClickListener;

        IngredientHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            textViewIngredientName = itemView.findViewById(R.id.textViewIngredientName);
            textViewIngredientExpiry = itemView.findViewById(R.id.textViewIngredientExpiry);
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
