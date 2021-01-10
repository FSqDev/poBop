package com.fsq.pobop.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Looper;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.fsq.pobop.R;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.Request;

import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import com.fsq.pobop.api.Api;
import android.util.Log;
import android.os.Handler;

import org.bson.json.JsonParseException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public class RecipeDetailsActivity extends AppCompatActivity {

//    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        Bundle bundle = getIntent().getExtras();
        String title = bundle.getString("title");
//        getRecipeSummary(bundle.getInt("id"));
        Executors.newSingleThreadExecutor().execute(() -> {
            RequestQueue queue = Volley.newRequestQueue(this);
//            TextView summaryText = findViewById(R.id.detailsSummary);
            String requestUrl = Api.BASE + "recipes/instructions/" + bundle.getInt("id");
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, requestUrl, null,
                    response -> {
                        Log.d("recipe", "initiate fetching instructions boioiii");
                        runOnUiThread(() -> {
//                            @Override
//                            public void run() {
////                                try {
                                Log.d("recipe", response.toString());
                                makeInstructions(response);
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

        TextView titleText = findViewById(R.id.detailsTitle);

        ImageView img = findViewById(R.id.image);
        img.setImageBitmap(bmp);
        titleText.setText(title);

    }

//    private void formatStep(TextView view, JSONObject steps)

    private void makeInstructions(JSONObject response) {
        LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup root = (ViewGroup)findViewById((R.id.stepLayout));
        try {
            JSONArray arr = response.getJSONArray("steps");
            for (int i = 0; i < arr.length(); i++) {
                View v = vi.inflate(R.layout.recipe_instructions_card, root);
                JSONObject obj = arr.getJSONObject(i);
                TextView stepNum = v.findViewById(R.id.stepNum);
                stepNum.setText(String.valueOf(obj.getInt("number")));
                TextView stepContent = v.findViewById(R.id.stepContent);
                stepContent.setText(obj.getString("step"));

                root.addView(v);
            }
        } catch (JSONException e) {
            Log.e("recipe", "baddy bad json uh oh: " + e.getMessage());
        }


    }
}