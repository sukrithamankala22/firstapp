package com.example.firstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.Toast;

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


public class RegisterActivity extends AppCompatActivity {
    EditText username,number,email,phone,password1,password2,phone2,parentname;
    TextView loginview;
    RadioGroup radioGroup2;
    RadioButton radio_1,radio_2,radio_3,radio_4;
    Button btn_register;
    Spinner Branch2;
    private RequestQueue queue;
    JsonObjectRequest objectRequest;
    static final String Key_roll="srollno";
    static final String Key_Password="spassword";
    static final String Key_name="sname";
    static final String Key_dept="sdept";
    static final String Key_year="syear";
    static final String Key_email="semail";
    static final String Key_phone="sphone";
    static final String Key_pgname="spgname";
    static final String Key_pgphone="spgphone";
    private String srollno;
    private String spassword;
    private String sname;
    private String sdept;
    private String syear;
    private String semail;
    private String sphone;
    private String spgname;
    private String spgphone;


    JSONObject data;

    private static final int MY_PASSWORD_DIALOG_ID = 4;
    boolean isEmpty
            (EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }
    boolean isEmail(EditText text) {
        CharSequence email = text.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    void checkDataEntered(){
        if (isEmpty(username)) {
            username.setError("You must enter your name to register!");
        }
        if (radioGroup2.getCheckedRadioButtonId() == -1)
        {
            Toast t = Toast.makeText(this, "You must select a year!", Toast.LENGTH_SHORT);
            t.show();
        }
        if (isEmpty(number)) {
            number.setError("Roll number is required!");
        }
        if (isEmpty(phone)) {
            phone.setError("Phone number is required!");
        }
        if (isEmpty(phone2)) {
            phone2.setError("Parents number is required!");
        }
        if (isEmpty(parentname)) {
            parentname.setError("Parents Name is required!");
        }
        if (isEmpty(password1)) {
            password1.setError("Password is required!");
        }
        if (isEmpty(password2)) {
            password2.setError("You need to confirm Password!");
        }
        if (isEmail(email) == false) {
            email.setError("Enter valid email!");
        }
        if(validatePassword()==true){
            userregister();
            queue.add(objectRequest);
            //gotodashboardfn();

        }

    }
    public void userregister()
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
        srollno = number.getText().toString().trim();
        spassword = password2.getText().toString().trim();
        sname = username.getText().toString().trim();
        semail = email.getText().toString().trim();
        sphone = phone.getText().toString().trim();
        spgphone = phone2.getText().toString().trim();
        spgname = parentname.getText().toString().trim();
        sdept = Branch2.getSelectedItem().toString().trim();

        String URL = "https://outpassapp.herokuapp.com/studentregister";
        data = new JSONObject();
        try {
            data.put(Key_roll,srollno);
            data.put(Key_Password,spassword);
            data.put(Key_email,semail);
            data.put(Key_name,sname);
            data.put(Key_dept,sdept);
            data.put(Key_year,syear);
            data.put(Key_phone,sphone);
            data.put(Key_pgname,spgname);
            data.put(Key_pgphone,spgphone);
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
                });
        objectRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

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
        String passwordInput = password1.getText().toString().trim();
        if (passwordInput.isEmpty()) {
            password1.setError("Field can't be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            password1.setError("Password too weak");
            return false;
        } else {
            password1.setError(null);
            return true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username = (EditText) findViewById(R.id.username);
        number = (EditText) findViewById(R.id.number);
        email = (EditText) findViewById(R.id.email);
        phone = (EditText) findViewById(R.id.phone);
        phone2 = (EditText) findViewById(R.id.phone2);
        parentname = (EditText) findViewById(R.id.parentname);
        password1 = (EditText) findViewById(R.id.password1);
        password2 = (EditText) findViewById(R.id.password2);
        btn_register = (Button) findViewById(R.id.btn_register);
        loginview = (TextView) findViewById(R.id.loginview);
        radioGroup2 = (RadioGroup) findViewById(R.id.radioGroup2);
        radio_1 = (RadioButton) findViewById(R.id.radio_1);
        radio_2 = (RadioButton) findViewById(R.id.radio_2);
        radio_3 = (RadioButton) findViewById(R.id.radio_3);
        radio_4 = (RadioButton) findViewById(R.id.radio_4);



        Branch2 = (Spinner) findViewById(R.id.Branch2);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(RegisterActivity.this,
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.branch_array));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Branch2.setAdapter(adapter);

        loginview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginfn();
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkDataEntered();


            }
        });

        password2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String passwrd = password1.getText().toString();
                if (editable.length() > 0 && passwrd.length() > 0) {
                    if(!password2 .equals(passwrd)){
                        // give an error that password and confirm password not match
                        Toast.makeText(RegisterActivity.this,"Password Not matching",Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

    }
    public void loginfn(){
        Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
        startActivity(intent);
    }

}