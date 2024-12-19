package com.example.finaltest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class HomePage extends AppCompatActivity {

    Spinner spnSubject;
    Button btnEnroll;
    TextView lblCredits;
    RecyclerView recyclerView;
    String username;
    List<SubjectModel> enrolledSubjects;
    int totalCredits;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        url = "http://192.168.56.1/finals/enrollment.php";
        spnSubject = findViewById(R.id.spnSubject);
        btnEnroll = findViewById(R.id.btnEnroll);
        lblCredits = findViewById(R.id.lblCredits);
        username = getIntent().getStringExtra("username");
        enrolledSubjects =  new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        FillRecyclerView();
        FillSpinner();
        GetSum();

        btnEnroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Enroll();
            }
        });
    }

    public void FillRecyclerView(){
        VolleyLog.DEBUG = true;
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    if (response != "error"){
                        Gson gson = new Gson();
                        Type subjectListType = new TypeToken<List<SubjectModel>>() {}.getType();
                        enrolledSubjects = gson.fromJson(response, subjectListType);
                        if (enrolledSubjects.size() > 0) {
                            SubjectAdapter subjectAdapter = new SubjectAdapter(enrolledSubjects, HomePage.this, username, this);
                            recyclerView.setAdapter(subjectAdapter);
                        } else {
                            Toast.makeText(this, "Enrolled Classes Not Found", Toast.LENGTH_SHORT).show();
                        }

                    }
                    else
                        Toast.makeText(HomePage.this, "Failed to get enrollment!", Toast.LENGTH_SHORT).show();
                }, error -> Log.e("Error", (error.getLocalizedMessage() == null) ? "Unknown Error" : error.getLocalizedMessage())){
            protected Map<String, String> getParams(){
                Map<String, String> paramV = new HashMap<>();
                paramV.put("action", "select");
                paramV.put("username", username);
                return paramV;
            }
        };
        queue.add(stringRequest);
    }

    public void FillSpinner(){
        VolleyLog.DEBUG = true;
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    if (response != "error"){
                        Gson gson = new Gson();
                        Type subjectListType = new TypeToken<List<SubjectModel>>() {}.getType();
                        List<SubjectModel> subjects = gson.fromJson(response, subjectListType);
                        String[] subjectNames = new String[subjects.size()];
                        for(int i = 0; i < subjectNames.length; i++){
                            subjectNames[i] = subjects.get(i).name;
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                                android.R.layout.simple_spinner_item, subjectNames);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spnSubject.setAdapter(adapter);
                    }
                    else
                        Toast.makeText(HomePage.this, "Failed to get enrollment!", Toast.LENGTH_SHORT).show();
                }, error -> Log.e("Error", (error.getLocalizedMessage() == null) ? "Unknown Error" : error.getLocalizedMessage())){
            protected Map<String, String> getParams(){
                Map<String, String> paramV = new HashMap<>();
                paramV.put("action", "subjects");
                return paramV;
            }
        };
        queue.add(stringRequest);
    }

    public void GetSum(){
        VolleyLog.DEBUG = true;
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    if (response != "error"){
                        lblCredits.setText("Total Credits: "+response);
                        try{
                            totalCredits = Integer.parseInt(response);
                        } catch (Exception exception){
                            totalCredits = 0;
                        }
                    }
                    else
                        Toast.makeText(HomePage.this, "Failed to get total credits!", Toast.LENGTH_SHORT).show();
                }, error -> Log.e("Error", (error.getLocalizedMessage() == null) ? "Unknown Error" : error.getLocalizedMessage())){
            protected Map<String, String> getParams(){
                Map<String, String> paramV = new HashMap<>();
                paramV.put("action", "sum");
                paramV.put("username", username);
                return paramV;
            }
        };
        queue.add(stringRequest);
    }

    public void Enroll(){
        String subjectName = spnSubject.getSelectedItem().toString();
        VolleyLog.DEBUG = true;
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    if (response.equals("Success")){
                        Toast.makeText(HomePage.this, "Successfully enrolled!", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(getIntent());
                    } else if(response.equals("overcredit")){
                        Toast.makeText(HomePage.this, "Total credits cannot exceed 24!", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(HomePage.this, "Failed to Enroll!", Toast.LENGTH_SHORT).show();
                }, error -> Log.e("Error", (error.getLocalizedMessage() == null) ? "Unknown Error" : error.getLocalizedMessage())){
            protected Map<String, String> getParams(){
                Map<String, String> paramV = new HashMap<>();
                paramV.put("action", "enroll");
                paramV.put("username", username);
                paramV.put("subject", subjectName);
                paramV.put("sumCredits", ""+totalCredits);
                return paramV;
            }
        };
        queue.add(stringRequest);
    }

    public void setCredit(int credit){
        totalCredits = credit;
        lblCredits.setText("Total Credits: "+totalCredits);
    }
}