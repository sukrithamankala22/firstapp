package com.example.firstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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
import java.util.regex.Pattern;


public class Editprofile_Activity extends AppCompatActivity implements View.OnClickListener {
    Button savebtn;
    RadioGroup radioGroup2;
    EditText fullname;
    RadioButton radio1;
    RadioButton radio2;
    RadioButton radio3;
    RadioButton radio4;
    TextView rollnumber;
    EditText mailid;
    EditText phonenumber;
    EditText parent;
    EditText parentnum;
    EditText pass;
    EditText confirmpass;
    private RequestQueue queue;
    JsonObjectRequest objectRequest;

    static final String Key_Password="soldpassword";
    static final String Key_Passwordnew="snewpassword";
    static final String Key_name="sname";
    static final String Key_year="syear";
    static final String Key_email="semail";
    static final String Key_phone="sphone";
    static final String Key_pgname="spgname";
    static final String Key_pgphone="spgphone";
    private String soldpassword;
    private String snewpassword;
    private String sname;
    private String syear;
    private String semail;
    private String sphone;
    private String spgname;
    private String spgphone;
    JSONObject data;

    boolean isEmpty
            (EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }
    boolean isEmail(EditText text) {
        CharSequence email = text.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    void checkDataEntered() {

        if (isEmpty(pass)) {
            pass.setError("Password is required!");
        }

        if(validatePassword()==true){
            useredit();
            queue.add(objectRequest);
            //gotodashboardfn();

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile_);

        savebtn=(Button)findViewById(R.id.btn_edit);
        fullname=(EditText) findViewById(R.id.username);
        radio1=(RadioButton) findViewById(R.id.radio_1);
        radio2=(RadioButton) findViewById(R.id.radio_2);
        radio3=(RadioButton) findViewById(R.id.radio_3);
        radio4=(RadioButton) findViewById(R.id.radio_4);
        mailid=(EditText)findViewById(R.id.email);
        phonenumber=(EditText)findViewById(R.id.phone);
        parent=(EditText)findViewById(R.id.parentname);
        parentnum=(EditText)findViewById(R.id.phone2);
        pass=(EditText)findViewById(R.id.passwordo);
        confirmpass=(EditText)findViewById(R.id.passwordn);
        savebtn.setOnClickListener(this);
        radioGroup2 = (RadioGroup) findViewById(R.id.radioGroup2);
        rollnumber=(TextView) findViewById(R.id.number);

        rollnumber.setText(""+ MainActivity.srollno);

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkDataEntered();


            }
        });


    }
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    //"(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{8,}" +               //at least 8 characters
                    "$");
    private boolean validatePassword() {
        String passwordInput = confirmpass.getText().toString().trim();
        if (passwordInput.isEmpty()) {
            confirmpass.setError("Field can't be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            confirmpass.setError("Password too weak");
            return false;
        } else {
            pass.setError(null);
            return true;
        }
    }

    public void useredit()
    {
        int radioButtonID = radioGroup2.getCheckedRadioButtonId();
        switch(radioButtonID){
            case R.id.radio_1:
                syear="1";
                break;
            case R.id.radio_2:
                syear="2";
                break;
            case R.id.radio_3:
                syear="3";
                break;
            case R.id.radio_4:
                syear="4";
                break;
        }
        sname=fullname.getText().toString().trim();
        semail=mailid.getText().toString().trim();
        sphone=phonenumber.getText().toString().trim();
        spgname=parent.getText().toString().trim();
        spgphone=parentnum.getText().toString().trim();
        soldpassword=pass.getText().toString().trim();
        snewpassword=confirmpass.getText().toString().trim();



        String URL = "https://outpassapp.herokuapp.com/editstudentdetails"+"?srollno="+MainActivity.srollno;

        data = new JSONObject();
        try {
            data.put(Key_name,sname);
            data.put(Key_year,syear);
            data.put(Key_email,semail);
            data.put(Key_pgphone,spgphone);
            data.put(Key_pgname,spgname);
            data.put(Key_phone,sphone);
            data.put(Key_Password,soldpassword);
            data.put(Key_Passwordnew,snewpassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        queue = Volley.newRequestQueue(this);
        objectRequest = new JsonObjectRequest(Request.Method.POST,
                URL,
                data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Toast toast = Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_LONG);
                            toast.show();
                            loginfn();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast toast = Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG);
                        toast.show();
                    }
                }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                // Basic Authentication
                //String auth = "Basic " + Base64.encodeToString(CONSUMER_KEY_AND_SECRET.getBytes(), Base64.NO_WRAP);

                headers.put("Authorization", "Bearer " + MainActivity.access_token);
                return headers;
            }
        };
        objectRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

    }

    public void onClick(View v) {
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginfn();

            }
        });
    }

    public void loginfn() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}