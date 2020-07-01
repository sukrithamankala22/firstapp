package com.example.firstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.os.Bundle;
import android.widget.Toast;

import android.os.AsyncTask;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    EditText Roll_No,password;
    TextView textView;
    Button btn_login;
    private RequestQueue queue;
    JsonObjectRequest objectRequest;
    static final String Key_roll="srollno";
    static final String Key_Password="spassword";
    static final String Key_name="sname";
    static final String Key_at="access_token";
    static public String srollno;
    static public String spassword;
    static public String sname;
    static public String access_token;
    JSONObject data;
    boolean isEmpty
            (EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }
    void checkDataEntered() {
        if (isEmpty(Roll_No)) {
            password.setError("");
            Toast t = Toast.makeText(this, "You must enter your Roll Number!", Toast.LENGTH_SHORT);
            t.show();
        }
        if (isEmpty(password)) {
            password.setError("Password is required!");
        }
        else{
            userLogin();
            queue.add(objectRequest);
        }
    }

    public SharedPreferences.Editor loginPrefsEditor;
    public  SharedPreferences loginPreferences;
    private Boolean saveLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Roll_No = (EditText) findViewById(R.id.Roll_No);
        password = (EditText) findViewById(R.id.password);
        btn_login = (Button) findViewById(R.id.btn_login);
        textView = (TextView) findViewById(R.id.textView);
        btn_login.setOnClickListener(this);
        textView.setOnClickListener(this);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            Roll_No.setText(loginPreferences.getString("userroll", ""));
            password.setText(loginPreferences.getString("password", ""));
        }
    }
    public void onClick(View v) {
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkDataEntered();
                //opendashboardfn();
                loginPrefsEditor.putBoolean("saveLogin", true);
                loginPrefsEditor.putString("userroll", srollno);
                loginPrefsEditor.putString("password", spassword);
                loginPrefsEditor.commit();
                //new myAsyncTask().execute();

            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MainActivity.this,
                        RegisterActivity.class);
                startActivity(i);

            }
        });
    }
    public void userLogin()
    {
        srollno = Roll_No.getText().toString().trim();
        spassword = password.getText().toString().trim();
        String URL = "https://outpassapp.herokuapp.com/studentlogin";
        data = new JSONObject();
        try {
            data.put(Key_roll,srollno);
            data.put(Key_Password,spassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Log.e("qwert",data.toString());
        queue = Volley.newRequestQueue(this);
        objectRequest = new JsonObjectRequest(Request.Method.POST,
                URL,
                data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Toast toast = Toast.makeText(getApplicationContext(), "Welcome!", Toast.LENGTH_LONG);
                            toast.show();
                            sname = response.getString("sname");
                            access_token = response.getString("access_token");
                            toast.show();
                            opendashboardfn();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override

                    public void onErrorResponse(VolleyError error) {

                        Toast toast = Toast.makeText(getApplicationContext(), "Invalid Credentials!", Toast.LENGTH_LONG);
                        toast.show();
                    }
                })
        {

            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Key_roll,srollno);
                params.put(Key_Password,spassword);
                return params;
            }
            @Override
            public String getBodyContentType() {
                return "application/json";
            }

        };

    }
    public void opendashboardfn(){
        Intent intent = new Intent(this, DashboardActivity.class);
        intent.putExtra(Key_roll,srollno);
        intent.putExtra(Key_name,sname);
        intent.putExtra(Key_at,access_token);
        startActivity(intent);
    }

}