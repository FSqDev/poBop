package com.fsq.pobop;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fsq.pobop.api.Api;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

public class AuthenticationActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();
        sharedPreferences = getSharedPreferences("auth", Context.MODE_PRIVATE);

        TextInputEditText username = findViewById(R.id.editTextUsername);
        TextInputEditText password = findViewById(R.id.editTextPassword);

        TextView loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(v -> {
            authenticate(username.getText().toString(), password.getText().toString());
        });

        TextView registerButton = findViewById(R.id.loginRegisterButton);
        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(AuthenticationActivity.this, RegistrationActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void authenticate(String email, String password){
        RequestQueue queue = Volley.newRequestQueue(this);
        JSONObject json = new JSONObject();
        try {
            json.put("email", email);
            json.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Api.BASE + "users/login", json, response -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            try {
                editor.putString("id", response.getString("id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            editor.apply();
            finish();
        }, error -> {
            if(error != null) {
                Log.d("LOGIN ERROR", error.getMessage());
            }
        });

        queue.add(jsonObjectRequest);
    }
}