package com.fsq.pobop.ui.pantry;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fsq.pobop.R;

import org.jetbrains.annotations.NotNull;

public class PantryFragment extends Fragment implements IngredientAdapter.OnItemClickListener {

    private PantryViewModel viewModel;
    View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_pantry, container, false);
        setHasOptionsMenu(true);
        viewModel = new ViewModelProvider(this).get(PantryViewModel.class);

        RecyclerView recyclerView = root.findViewById(R.id.recyclerViewIngredients);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setHasFixedSize(true);
        final IngredientAdapter adapter = new IngredientAdapter(this);
        recyclerView.setAdapter(adapter);

        viewModel.findAll().observe(getViewLifecycleOwner(), adapter::setProjectListItems);

        return root;
    }

    @Override
    public void onItemClick(int position) {
        // nav to details
    }

    @Override
    public void onCreateOptionsMenu(@NotNull Menu menu, @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        requireActivity().getMenuInflater().inflate(R.menu.menu_pantry, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.pantry_menu_add) {
            Navigation.findNavController(root).navigate(PantryFragmentDirections.actionNavPantryToNavAddIngredient());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}