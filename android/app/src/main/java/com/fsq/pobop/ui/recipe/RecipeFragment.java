package com.fsq.pobop.ui.recipe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fsq.pobop.R;

public class RecipeFragment extends Fragment implements RecipeRecViewAdapter.OnItemClickListener {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.recipe_fragment, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.recyclerViewRecipes);
        final RecipeRecViewAdapter adapter = new RecipeRecViewAdapter(this);
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new GridLayoutManager(root.getContext(), 2));

        return root;
    }

    @Override
    public void onItemClick(int position) {
        //
    }
}
