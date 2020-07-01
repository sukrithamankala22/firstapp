package com.example.firstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

import static com.example.firstapp.MainActivity.Key_at;
import static com.example.firstapp.MainActivity.Key_roll;
import static com.example.firstapp.MainActivity.Key_name;


public class DashboardActivity extends AppCompatActivity implements View.OnClickListener{
    Button btnA;
    Button btnB;
    Button btnC;
    Button btnD;
    TextView txtV;
    TextView passlefttxt,  textView9 , num;
    ImageView imgV;
    Button signout;
    String left="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        btnA = (Button) findViewById(R.id.applyforoutpassesbtn);
        btnB = (Button) findViewById(R.id.previousoutpassesbtn);
        btnC = (Button) findViewById(R.id.editprofilebtn);
        btnD = (Button) findViewById(R.id.viewstatusbtn);
        signout=(Button) findViewById(R.id.signout);
        txtV = (TextView) findViewById(R.id.dashboardtext);
        textView9 = (TextView) findViewById(R.id.textView9);
        passlefttxt = (TextView) findViewById(R.id.passeslefttxt);
        num = (TextView) findViewById(R.id.numbern);
        imgV = (ImageView) findViewById(R.id.logoimage);
        btnA.setOnClickListener(this);
        btnB.setOnClickListener(this);
        btnC.setOnClickListener(this);
        btnD.setOnClickListener( this);
        signout.setOnClickListener(this);

        textView9.setText("Welcome, "+ MainActivity.sname);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://outpassapp.herokuapp.com/getpendingnoofpassesleft"+"?srollno="+MainActivity.srollno;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //num.setText(""+ response.substring(15,16));
                        try {
                            JSONArray ja = new JSONArray(response);

                            for(int i=0; i < ja.length(); i++) {

                                JSONObject jsonObject = ja.getJSONObject(i);

                                // int id = Integer.parseInt(jsonObject.optString("id").toString());
                                String oodate = jsonObject.getString("passesleft");

                                left=""+oodate;

                            }
                            num.setText(left);

                        }
                        catch (JSONException e){
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        num.setText("");
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

    @Override
    public void onClick(View v) {

        btnA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String leftpasses=num.getText().toString();
                if(!leftpasses.equals("0")) {
                    openapplyopfn();
                }
                else{
                    Toast t = Toast.makeText(DashboardActivity.this , "You don't have any GatePass Left", Toast.LENGTH_SHORT);
                    t.show();
                }
            }
        });
        btnB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openprevopfn();

            }
        });
        btnC.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                editprofilefn();
            }
        });
        btnD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String leftpasses=num.getText().toString();
                if(!leftpasses.equals("0")) {
                    openviewstatusfn();
                }
                else{
                    Toast t = Toast.makeText(DashboardActivity.this , "You don't have any GatePass Left", Toast.LENGTH_SHORT);
                    t.show();
                }
                //openviewstatusfn();
            }
        });
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openloginpagefn();
            }
        });


    }
    public void openprevopfn() {
        Intent intent = new Intent(this, prevop.class);
        startActivity(intent);

    }
    public void editprofilefn(){
        Intent intent=new Intent(this,Editprofile_Activity.class);
        startActivity(intent);
    }
    public void openviewstatusfn(){
        Intent intent=new Intent(this,viewstatus.class);
        startActivity(intent);
    }
    public void openapplyopfn(){
        Intent intent=new Intent(this, applyop.class);
        startActivity(intent);
    }
    public void openloginpagefn(){
        SharedPreferences preferences =getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }

}