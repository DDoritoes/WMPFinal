package com.example.finaltest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.ViewHolder> {
    List<SubjectModel> subjectList;
    Context context;
    String username;
    HomePage home;

    public SubjectAdapter(List<SubjectModel> subjectList, Context context, String username, HomePage home){
        this.subjectList = subjectList;
        this.context = context;
        this.username = username;
        this.home = home;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.subject_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final SubjectModel subject = subjectList.get(position);
        Log.d("SubjectAdapter", "Binding data: " + subject.name + ", Credits: " + subject.credit);
        holder.lblSubject.setText(subject.name);
        holder.lblCredits.setText("Credits: "+Integer.toString(subject.credit));
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeEnrollment(username, subject.name, position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView lblSubject, lblCredits;
        Button btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lblSubject = itemView.findViewById(R.id.lblSubject);
            lblCredits = itemView.findViewById(R.id.lblCredits);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    public void removeEnrollment(String username, String subjectName, int position){
        VolleyLog.DEBUG = true;
        RequestQueue queue = Volley.newRequestQueue(context.getApplicationContext());
        String url = "http://192.168.56.1/finals/enrollment.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    if (response.equals("Success")){
                        home.setCredit(home.totalCredits - subjectList.get(position).credit);
                        subjectList.remove(position);
                        notifyItemRemoved(position);
                    }
                    else
                        Toast.makeText(context.getApplicationContext(), "Failed to cancel enrollment!", Toast.LENGTH_SHORT).show();
                }, error -> Log.e("Error", (error.getLocalizedMessage() == null) ? "Unknown Error" : error.getLocalizedMessage())){
            protected Map<String, String> getParams(){
                Map<String, String> paramV = new HashMap<>();
                paramV.put("action", "unenroll");
                paramV.put("username", username);
                paramV.put("subject", subjectName);
                return paramV;
            }
        };
        queue.add(stringRequest);
    }
}
