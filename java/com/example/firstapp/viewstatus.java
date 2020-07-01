
package com.example.firstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class viewstatus extends AppCompatActivity {
    TextView status, remark;
    String ostatus="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewstatus);

        status = (TextView) findViewById(R.id.textView4);
        remark = (TextView) findViewById(R.id.textView5);


        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://outpassapp.herokuapp.com/outpassstatus"+"?oid="+applyop.oid;



        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        status.setText(""+response);
                        try {
                            JSONArray ja = new JSONArray(response);

                            for(int i=0; i < ja.length(); i++) {

                                JSONObject jsonObject = ja.getJSONObject(i);

                                // int id = Integer.parseInt(jsonObject.optString("id").toString());
                                String oostatus = jsonObject.getString("ostatus");
                                //String url = jsonObject.getString("URL");

                                ostatus =""+ oostatus;
                            }
                            status.setText(ostatus);

                        }
                        catch (JSONException e){
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Invalid!", Toast.LENGTH_LONG);
                        toast.show();
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                // Basic Authentication
                //String auth = "Basic " + Base64.encodeToString(CONSUMER_KEY_AND_SECRET.getBytes(), Base64.NO_WRAP);

                headers.put("Authorization", "Bearer " + MainActivity.access_token);
                return headers;
            }
        };

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}