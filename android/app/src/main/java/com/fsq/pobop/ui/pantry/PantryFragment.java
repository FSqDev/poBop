package com.fsq.pobop.ui.pantry;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fsq.pobop.R;
import com.fsq.pobop.api.Api;
import com.fsq.pobop.data.DateConverter;
import com.fsq.pobop.entity.ingredient.Ingredient;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class PantryFragment extends Fragment implements IngredientAdapter.OnItemClickListener {

    private boolean working = false;

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
        } else if (id == R.id.pantry_menu_sync) {
            if (!working) {
                working = true;
                sync();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void sync() {
        Executors.newSingleThreadExecutor().execute(() -> {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("auth", Context.MODE_PRIVATE);
            String id = sharedPreferences.getString("id", null);
            List<Ingredient> dirtyIngredients = viewModel.findAllDirty();

            if (id != null) {
                RequestQueue queue = Volley.newRequestQueue(root.getContext());
                boolean putDelivered = false;
                if (dirtyIngredients.size() > 0) {
                    JSONObject putJson = new JSONObject();
                    try {
                        putJson.put("id", id);
                        putJson.put("products", alleluia(dirtyIngredients));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    JsonObjectRequest jsonObjectPut = new JsonObjectRequest(Request.Method.PUT, Api.BASE + "users/products", putJson, response -> {
                    }, error -> {
                    });
                    queue.add(jsonObjectPut);
                    while (!putDelivered) {
                        putDelivered = jsonObjectPut.hasHadResponseDelivered();
                    }
                } else {
                    putDelivered = true;
                }

                boolean getDelivered = false;
                if (putDelivered) {
                    JsonObjectRequest jsonObjectGet = new JsonObjectRequest(Request.Method.GET, Api.BASE + "users/products?id=" + id, null, response -> {
                        try {
                            viewModel.add(jsonToIngredients(response.getJSONArray("products")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }, error -> {
                        Log.d("SYNC ERROR", error.getMessage());
                    });

                    queue.add(jsonObjectGet);
                    while(!getDelivered) {
                        getDelivered = jsonObjectGet.hasHadResponseDelivered();
                    }
                    working = false;
                }
            }
        });
    }

    private List<Ingredient> jsonToIngredients(JSONArray array) {
        List<Ingredient> ingredients = new ArrayList<>();
        DateConverter dateConverter = new DateConverter();
        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject object = array.getJSONObject(i);
                Ingredient ingredient = new Ingredient();
                ingredient.setId(object.getString("_id"));
                ingredient.setProductName(object.getString("name"));
                ingredient.setProductType(object.getString("product_type"));
                ingredient.setImageUrl(object.getString("image_url"));
                ingredient.setExpiryDate(dateConverter.stringToDate(object.getString("expiry_date")));
                ingredient.setDirty(0);
                ingredients.add(ingredient);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return ingredients;
    }

    private JSONArray alleluia(List<Ingredient> ingredientList) {
        JSONArray jsonArray = new JSONArray();
        for (Ingredient ingredient : ingredientList) {
            jsonArray.put(ingredient.toJson());
        }
        return jsonArray;
    }
}