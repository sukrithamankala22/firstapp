package com.example.firstapp;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.example.firstapp.MainActivity.Key_at;
import static com.example.firstapp.MainActivity.Key_roll;

public class applyop extends AppCompatActivity {
    int SELECT_PHOTO=1;
    Uri uri;
    Button aply,choose;
    ImageView imageView;
    private RequestQueue queue;
    JsonObjectRequest objectRequest;
    static final String Key_time="otime";
    static final String Key_reason="odesc";
    static final String Key_date="odate";
    static final String Key_oid="oid";
    private String otime;
    private String odesc;
    private String srollno;
    JSONObject data;
    EditText time,reason;
    TextView date;
    //Calendar calendar;
    //CalendarView calendar;
    private Calendar calendar;
    private String odate;
    public static String oid;
    DatePicker picker;
    private TimePicker timePicker1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applyop);
        choose= (Button) findViewById(R.id.upload);
        imageView=(ImageView) findViewById(R.id.image_view);
        aply=(Button) findViewById(R.id.applybutton);
        //time = (EditText) findViewById(R.id.time);
        reason = (EditText) findViewById(R.id.reason);
        date = (TextView) findViewById(R.id.date);
        picker=(DatePicker)findViewById(R.id.datePicker1);
        timePicker1 = (TimePicker) findViewById(R.id.timePicker1);



        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,SELECT_PHOTO);
            }
        });
        aply.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                apply();
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,@Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==SELECT_PHOTO && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            uri=data.getData();
            try{
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                imageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void apply() {
        int day= picker.getDayOfMonth();
        int month= picker.getMonth();
        int year= picker.getYear();
        odate= year+":"+month+":"+day;
        Log.e("TAG", odate);

        timePicker1.setIs24HourView(true);

        int hour = timePicker1.getHour();
        int min = timePicker1.getMinute();

        otime=hour+":"+min+":00";
        Log.e("TAG", otime);
        //otime=time.getText().toString().trim();
        odesc=reason.getText().toString().trim();
        String URL = "https://outpassapp.herokuapp.com/outpassapplication"+"?srollno="+MainActivity.srollno;
        data = new JSONObject();
        try{
            data.put(Key_time,otime);
            data.put(Key_reason,odesc);
            data.put(Key_date,odate);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        queue= Volley.newRequestQueue(this);
        objectRequest=new JsonObjectRequest(Request.Method.POST,
                URL,
                data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Toast toast = Toast.makeText(getApplicationContext(),response.getString("message"), Toast.LENGTH_LONG
                            );
                            toast.show();
                            oid = response.getString("oid");
                            gotodashboardfn();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast toast = Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG);
                        toast.show();
                    }
                })
        {
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Key_time,otime);
                params.put(Key_reason,odesc);
                params.put(Key_date,odate);
                return params;
            }
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

        queue.add(objectRequest);

    }
    public void gotodashboardfn(){
        Intent intent=new Intent(this,DashboardActivity.class);
        startActivity(intent);
    }

}