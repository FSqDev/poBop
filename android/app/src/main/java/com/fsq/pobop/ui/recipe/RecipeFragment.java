package com.fsq.pobop.ui.recipe;
import com.fsq.pobop.ui.RecipeDetailsActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.fsq.pobop.entity.ingredient.Ingredient;
import com.fsq.pobop.R;
import com.fsq.pobop.ui.pantry.PantryViewModel;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.Request;
import com.android.volley.DefaultRetryPolicy;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import com.fsq.pobop.ui.recipe.Recipe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.fsq.pobop.api.Api;
import java.util.concurrent.Executor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import android.util.Log;
import android.content.Intent;


public class RecipeFragment extends Fragment implements RecipeRecViewAdapter.OnItemClickListener {

    private RecipeViewModel viewModel;
    private RecipeRecViewAdapter adapter;
    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.recipe_fragment, container, false);

        viewModel = new ViewModelProvider(this).get(RecipeViewModel.class);

        RecyclerView recyclerView = root.findViewById(R.id.recyclerViewRecipes);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setLayoutManager(new GridLayoutManager(root.getContext(), 2));
        adapter = new RecipeRecViewAdapter(this);
        recyclerView.setAdapter(adapter);

        Executors.newSingleThreadExecutor().execute(() -> {
            List<Ingredient> availableIngredients = viewModel.findAllIngredients();
            RequestQueue queue = Volley.newRequestQueue(root.getContext());
            String ingredientString = availableIngredients.stream()
                    .map(item -> item.getNonNullName()).collect(Collectors.joining(","));
//            ingredientString = "tomato,chicken";
            Log.d("DIRTYDOG", ingredientString);
            String requestURL = Api.BASE +  "recipes?ingredients=" + ingredientString;
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, requestURL, null,
                            response -> {
                                Log.d("resp", response.toString());
                                List<Recipe> recipes = new ArrayList<Recipe>();
                                try {
                                    JSONArray jsonArray = response.getJSONArray("recipes");

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject obj = jsonArray.getJSONObject(i);
                                        recipes.add(
                                                new Recipe(
                                                        obj.getString("title"),
                                                        obj.getString("image"),
                                                        obj.getInt("likes"),
                                                        obj.getInt("numMissingIngredients"),
                                                        obj.getInt("id")
                                                )
                                        );
                                    }
                                } catch (Exception e) {
                                    Log.e("recipes", "Fuck: " + e.getMessage());
                                }
                                adapter.setProjectListItems(recipes);
                            },
                            error -> {
                                Log.e("recipes", "Fetching recipes failed: " + error.toString());
                            });
                    queue.add(request);
                });
        return root;
    }

    @Override
    public void onItemClick(int position) {
        Recipe recipe = adapter.getRecipeAt(position);
        Intent intent = new Intent(getActivity(), RecipeDetailsActivity.class);
        intent.putExtra("title", recipe.getName());
        intent.putExtra("id", recipe.getRecipeId());
        intent.putExtra("image", recipe.getBmp());
        startActivity(intent);
    }

    private List<Ingredient> getIngredientsFromJson(JSONArray arr) {
        List<Ingredient> ingredients = new ArrayList<Ingredient>();
        try {
            for (int i = 0; i < arr.length(); i++) {
                ingredients.add(ingredientFromJson(arr.getJSONObject(i)));
            }
        } catch (Exception e) {
            Log.e("recipe", "Still fuck");
        }

        return ingredients;
    }

    private Ingredient ingredientFromJson(JSONObject obj) {
        Ingredient ing = new Ingredient();
        try {
            ing.setProductName(obj.getString("name"));
        } catch(Exception e) {
            Log.e("recipe", "goddamnit");
        }
        return ing;
    }
}
