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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class RegisterPage extends AppCompatActivity {

    TextView txtUsername, txtPassword;
    Button btnRegister, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnCancel = findViewById(R.id.btnCancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openLogin = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(openLogin);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Register();
            }
        });
    }

    public void Register(){
        VolleyLog.DEBUG = true;
        String username = txtUsername.getText().toString();
        String password = txtPassword.getText().toString();
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://192.168.56.1/finals/register.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    if (response.equals("Success")){
                        Toast.makeText(RegisterPage.this, "Successfully Registered!", Toast.LENGTH_SHORT).show();
                        Intent openLogin = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(openLogin);
                    }
                    else
                        Toast.makeText(RegisterPage.this, "Failed to Register", Toast.LENGTH_SHORT).show();
                }, error -> Log.e("Error", (error.getLocalizedMessage() == null) ? "Unknown Error" : error.getLocalizedMessage())){
            protected Map<String, String> getParams(){
                Map<String, String> paramV = new HashMap<>();
                paramV.put("username", username);
                paramV.put("password", password);
                return paramV;
            }
        };
        queue.add(stringRequest);
    }
}