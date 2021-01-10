package com.fsq.pobop;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fsq.pobop.api.Api;

import org.json.JSONException;
import org.json.JSONObject;

public class RegistrationActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        sharedPreferences = getSharedPreferences("auth", Context.MODE_PRIVATE);

        EditText email = findViewById(R.id.edtTxtEmail);
        EditText password = findViewById(R.id.editTextPassword);
        Button register = findViewById(R.id.btnRegister);

        register.setOnClickListener(v -> register(email.getText().toString(), password.getText().toString()));

    }

    private void register(String email, String password){
        RequestQueue queue = Volley.newRequestQueue(this);
        JSONObject json = new JSONObject();
        try {
            json.put("email", email);
            json.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Api.BASE + "users/register", json, response -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            try {
                editor.putString("id", response.getString("id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            editor.apply();
            finish();
        }, error -> {
            Log.d( "authenticate: ", String.valueOf(error.networkResponse.statusCode));
        });

        queue.add(jsonObjectRequest);
    }
}
