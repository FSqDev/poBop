package com.fsq.pobop.ui.pantry;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.fsq.pobop.R;

public class PantryFragment extends Fragment implements IngredientAdapter.OnItemClickListener {

    private PantryViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pantry, container, false);
        viewModel = new ViewModelProvider(this).get(PantryViewModel.class);

        RecyclerView recyclerView = root.findViewById(R.id.recyclerViewIngredients);
        final IngredientAdapter adapter = new IngredientAdapter(this);
        recyclerView.setAdapter(adapter);

        viewModel.findAll().observe(getViewLifecycleOwner(), adapter::setProjectListItems);

        return root;
    }

    @Override
    public void onItemClick(int position) {
        // nav to details
    }
}