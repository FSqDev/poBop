package com.fsq.pobop.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;
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

public class RecipeDetailsActivity extends AppCompatActivity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_recipe_details);
        Bundle bundle = getIntent().getExtras();
        String title = bundle.getString("title");
//        String summary = getRecipeSummary(bundle.getInt("id"));
    }

//    private String getRecipeSummary(int id) {
//        Executors.newSingleThreadExecutor().execute(() -> {
////            String requestUrl = Api.BASE +
//        });
//    }
}