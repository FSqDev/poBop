package com.fsq.pobop.ui.recipe.details;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.fsq.pobop.R;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.Request;

import java.util.concurrent.Executors;

import com.fsq.pobop.api.Api;
import android.util.Log;

import org.json.JSONException;

import android.view.LayoutInflater;
import android.view.View;

public class RecipeDetailsFragment extends Fragment {

//    private TextView mTextView;

    private View root;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_recipe_details, container, false);
        Bundle bundle = requireArguments();
        String title = bundle.getString("title");
//        getRecipeSummary(bundle.getInt("id"));

        RecyclerView recyclerView = root.findViewById(R.id.recyclerViewRecipeDetail);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        RecipeDetailAdapter adapter = new RecipeDetailAdapter();
        recyclerView.setAdapter(adapter);

        Executors.newSingleThreadExecutor().execute(() -> {
            RequestQueue queue = Volley.newRequestQueue(root.getContext());
//            TextView summaryText = findViewById(R.id.detailsSummary);
            String requestUrl = Api.BASE + "recipes/instructions/" + bundle.getInt("id");
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, requestUrl, null,
                    response -> {
                        Log.d("recipe", "initiate fetching instructions boioiii");
                        getActivity().runOnUiThread(() -> {
//                            @Override
//                            public void run() {
////                                try {
                                Log.d("recipe", response.toString());
                            try {
                                adapter.setRecipeDetails(response.getJSONArray("steps"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
//                                    summaryText.setText(response.getString("summary"));
//                                } catch (JSONException e) {
//                                    Log.d("recipesum",  "uh oooooooh: " + e.getMessage());
//                                }
//                            }
                        });
                    },
                    error -> {
                        Log.d("recipe", "noooooooo: " + error.getMessage());
                    });
            queue.add(request);
        });
        Bitmap bmp = (Bitmap)bundle.get("image");

        TextView titleText = root.findViewById(R.id.detailsTitle);

        ImageView img = root.findViewById(R.id.image);
        img.setImageBitmap(bmp);
        titleText.setText(title);
        return root;
    }

//    private void formatStep(TextView view, JSONObject steps)

//    private void makeInstructions(JSONObject response) {
//        LayoutInflater vi = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        ViewGroup viewGroup = (ViewGroup)root.findViewById((R.id.stepLayout));
//        try {
//            JSONArray arr = response.getJSONArray("steps");
//            for (int i = 0; i < arr.length(); i++) {
//                View v = vi.inflate(R.layout.recipe_instructions_card, viewGroup);
//                JSONObject obj = arr.getJSONObject(i);
//                TextView stepNum = v.findViewById(R.id.stepNum);
//                stepNum.setText(String.valueOf(obj.getInt("number")));
//                TextView stepContent = v.findViewById(R.id.stepContent);
//                stepContent.setText(obj.getString("step"));
//
//                viewGroup.addView(v);
//            }
//        } catch (JSONException e) {
//            Log.e("recipe", "baddy bad json uh oh: " + e.getMessage());
//        }
//
//
//    }
}