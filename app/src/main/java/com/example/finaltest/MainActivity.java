package com.example.finaltest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TextView txtUsername, txtPassword;
    Button btnLogin, btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openRegister = new Intent(getApplicationContext(), RegisterPage.class);
                startActivity(openRegister);
            }
        });
    }

    public void Login(){
        VolleyLog.DEBUG = true;
        String username = txtUsername.getText().toString();
        String password = txtPassword.getText().toString();
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://192.168.56.1/finals/login.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    if (response.equals("Success")){
                        Intent openHome = new Intent(getApplicationContext(), HomePage.class);
                        openHome.putExtra("username", username);
                        startActivity(openHome);
                    }
                    else
                        Toast.makeText(MainActivity.this, "Failed to login!", Toast.LENGTH_SHORT).show();
                }, error -> Log.e("Error", (error.getLocalizedMessage() == null) ? "Unknown Error" : error.getLocalizedMessage())){
            protected Map<String, String> getParams(){
                Map<String, String> paramV = new HashMap<>();
                paramV.put("username", username);
                paramV.put("password", password);
                return paramV;
            }
        };
        queue.add(stringRequest);
    }}