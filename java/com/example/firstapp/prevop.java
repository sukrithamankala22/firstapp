package com.example.firstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.firstapp.MainActivity.Key_at;
import static com.example.firstapp.MainActivity.Key_roll;

public class prevop extends AppCompatActivity {
    TextView text;
    String ostatus="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prevop);


        text = (TextView) findViewById(R.id.tv);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://outpassapp.herokuapp.com/getstudenthistory"+"?srollno="+MainActivity.srollno;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        text.setText(""+ response);
                        try {
                            JSONArray ja = new JSONArray(response);

                            for(int i=0; i < ja.length(); i++) {

                                JSONObject jsonObject = ja.getJSONObject(i);

                                // int id = Integer.parseInt(jsonObject.optString("id").toString());
                                String oodate = jsonObject.getString("odate");
                                String oostatus = jsonObject.getString("ostatus");
                                String oodesc = jsonObject.getString("odesc");


                                ostatus+= (i+1)+")"+ "<b>" + "Date: " + "</b> "+ oodate+"<br>" + "<b>"
                                        +"Reason: "+ "</b> "+ oodesc+"<br>" +"<b>"+" Status: "+"</b>" + oostatus+ "<br> <br>";
                                ostatus = ostatus.replace("come to cabin", "<font color='#f2f53b'>come to cabin</font>");
                                ostatus = ostatus.replace("rejected", "<font color='#cc331f'>rejected</font>");
                                ostatus = ostatus.replace("accepted", "<font color='#30b021'>accepted</font>");
                                ostatus = ostatus.replace("pending", "<font color='#8a8a7f'>pending</font>");


                            }
                            text.setText(Html.fromHtml(ostatus));

                        }
                        catch (JSONException e){
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        text.setText("");
                        Toast toast = Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_LONG);
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