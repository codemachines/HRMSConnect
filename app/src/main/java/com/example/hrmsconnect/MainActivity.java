package com.example.hrmsconnect;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    TextView tvsignup,tvforgotpwd;
    TextInputEditText edtvemp,edtpwd;

    Button btnsignin;

    ProgressBar mpb;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtvemp =findViewById(R.id.edtvemail);
        edtpwd=findViewById(R.id.edtvpwd);
        tvsignup=findViewById(R.id.tvsignup);
        btnsignin=findViewById(R.id.btnsignin);
        tvforgotpwd=findViewById(R.id.tvforgotpwd);
        mpb=findViewById(R.id.mpb);
        preferences=getSharedPreferences(MyPREFERENCES,MODE_PRIVATE);


        try {
            if (preferences.getString("login","").equals("yes"))
            {
                Intent i=new Intent(MainActivity.this,WelcomeScreen.class);
                startActivity(i);
                finish();
            }
        }
        catch (Exception e)
        {
            Toast.makeText(this, ""+ Log.e("Error:",e.getMessage()), Toast.LENGTH_SHORT).show();
        }


        tvsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,SignUp.class);
                startActivity(i);
            }
        });

        tvforgotpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgotpwd=new Intent(MainActivity.this,ForgotPassword.class);
                startActivity(forgotpwd);
            }
        });

        btnsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edtvemp.getText().toString().trim().isEmpty()){
                    edtvemp.setError("This Field Should not Remain Empty");
                }else if (edtpwd.getText().toString().isEmpty()){
                    edtpwd.setError("This Field Should Not Remain Empty");
                }else {
                    ValidateData();
                }
            }
        });
    }

    private void ValidateData() {

        final String evemp = edtvemp.getText().toString().trim();
        final String evpwd=edtpwd.getText().toString().trim();

        mpb.setVisibility(View.VISIBLE);
        Log.e("response>>",evemp+" "+evpwd);

        ApiInterface apiInterface=ApiClient.getClient().create(ApiInterface.class);
        Call<JsonElement> callapi=apiInterface.employeelogin(evemp,evpwd);

        callapi.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {

                Log.e("response>>>",response.body().toString());
                try {
                    JSONObject object=new JSONObject(response.body().toString());
                    String st=object.getString("status");

                    JSONObject data =object.getJSONObject("data");

                    if (st.equals("200"))
                    {
                        String id=data.getString("id");
                        String emp_id=data.getString("emp_id");
                        String name=data.getString("emp_name");
                        String email=data.getString("emp_email");
                        String designation=data.getString("emp_designation");
                        String mobile=data.getString("emp_mobile");
                        String gender=data.getString("emp_gender");
                        String profile=data.getString("emp_profile");
                        String balance=data.getString("balance");
                        String address=data.getString("emp_address");
                        String coaddress=data.getString("emp_caddress");

                        Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        SharedPreferences.Editor editor=preferences.edit();
                        editor.putString("email",email);
                        editor.putString("password",evpwd);
                        editor.putString("id",id);
                        editor.putString("emp_id",emp_id);
                        editor.putString("name",name);
                        editor.putString("designation",designation);
                        editor.putString("mobile",mobile);
                        editor.putString("gender",gender);
                        editor.putString("profile",profile);
                        editor.putString("balance",balance);
                        editor.putString("address",address);
                        editor.putString("coaddress",coaddress);
                        editor.putString("login","yes");
                        editor.commit();

                        Intent i=new Intent(MainActivity.this,HomeActivity.class);
                        startActivity(i);
                        finish();

                    }else if (st.equals("202"))
                    {Log.e("202>>","yes");
                        mpb.setVisibility(View.GONE);
                        Log.e("Error:",response.body().toString());
                        Toast.makeText(MainActivity.this, ""+Log.e("Error:",response.body().toString()), Toast.LENGTH_SHORT).show();
                        edtvemp.setError("Check Your Mobile/Email");
                        edtpwd.setError("Check Your Password");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

            }
        });


    }
}
