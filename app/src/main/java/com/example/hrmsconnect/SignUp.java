package com.example.hrmsconnect;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.JsonElement;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    TextView tvsignin;
    Button btnsignup;
    TextInputEditText edteid, edtname, edtemail, edtpwd, edtcpwd, edtmobile, edtdesignation, edtaddress, edtcaddress;
    String picurl="";
    RadioGroup gender;
    RadioButton male, female;
    String email;
    TextInputLayout tpwd;
    GoogleApiClient apiClient;
    ProgressBar signuppb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        tvsignin = findViewById(R.id.tvsignin);
        edteid = findViewById(R.id.edteid);
        edtname = findViewById(R.id.edtname);
        edtemail = findViewById(R.id.edtemail);
        edtpwd = findViewById(R.id.edtpwd);
        edtcpwd = findViewById(R.id.edtcpwd);
        edtmobile = findViewById(R.id.edtmobile);
        edtdesignation = findViewById(R.id.edtdesignation);
        edtaddress = findViewById(R.id.edtaddress);
        edtcaddress = findViewById(R.id.edtcaddress);
        gender = findViewById(R.id.gender);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        tpwd=findViewById(R.id.tpwd);
        signuppb=findViewById(R.id.signuppb);




        GoogleSignInOptions sign=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
         apiClient=new GoogleApiClient.Builder(SignUp.this)
                 .addConnectionCallbacks(this)
                 .addOnConnectionFailedListener(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,sign)
                .build();
         apiClient.connect();
        Intent i=Auth.GoogleSignInApi.getSignInIntent(apiClient);
        startActivityForResult(i,000);



        tvsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUp.this, Splashscreen.class);
                startActivity(i);
            }
        });


        btnsignup = findViewById(R.id.btnsignup);

        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edtname.getText().toString().trim().isEmpty()) {
                    edtname.setError("Enter Your Name");
                } else if (!isValidEmaillId(edtemail.getText().toString().trim())) {
                    edtemail.setError("Enter valid Email");
                } else if (edtpwd.getText().toString().length() > 16) {
                    edtpwd.setError("Password length should not exist more than 16 character");
                } else if (edtpwd.getText().toString().trim().isEmpty() && edtcpwd.getText().toString().trim().isEmpty()) {
                    edtpwd.setError("Enter Password");
                    edtcpwd.setError("Enter password");
                    tpwd.setPasswordVisibilityToggleDrawable(View.INVISIBLE);
                } else if (!edtpwd.getText().toString().equals(edtcpwd.getText().toString())) {
                    Toast.makeText(SignUp.this, "Password Mismatch Error", Toast.LENGTH_SHORT).show();
                } else if (edtmobile.getText().toString().trim().isEmpty()) {
                    edtmobile.setError("Enter Mobile No.");
                } else if (edtmobile.getText().length() < 10) {
                    edtmobile.setError("Invalid Mobile No.");
                } else if (edtaddress.getText().toString().trim().isEmpty()) {
                    edtaddress.setError("Address Field should not remain empty");
                } else if (edtcaddress.getText().toString().trim().isEmpty()) {
                    edtcaddress.setError("Company Address field Should not remain empty");
                } else {
                    signuppb.setVisibility(View.VISIBLE);
                    DataPost();
                }
            }
        });
    }


    private void DataPost() {

//        final String eid = edteid.getText().toString().trim();
        final String ename = edtname.getText().toString().trim();
        final String eemail = edtemail.getText().toString().trim();
        final String epwd = edtpwd.getText().toString().trim();
        String ecpwd = edtcpwd.getText().toString().trim();
        final String emobile = edtmobile.getText().toString().trim();
        final String edesignation = edtdesignation.getText().toString().trim();
        int sid = gender.getCheckedRadioButtonId();
        RadioButton empgender = findViewById(sid);
        final String egender = empgender.getText().toString().trim();
        final String eaddress = edtaddress.getText().toString().trim();
        final String ecaddress = edtcaddress.getText().toString().trim();

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {

                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();



                        // Log and toast


                        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
                        Call<JsonElement> callapi = api.employeeregister(ename, eemail, epwd, emobile, edesignation, egender,picurl, eaddress, ecaddress,token);
                        callapi.enqueue(new Callback<JsonElement>() {
                            @Override
                            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                                Log.e("Responsee>>", response.body().toString());
                                try {
                                    JSONObject obj = new JSONObject(response.body().toString());
                                    String st = obj.getString("status");
                                    if (st.equals("200")) {

                                        getAnotherMethod(eemail);

                                    } else {

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
                });





    }

    private void getAnotherMethod(String eemail) {

        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonElement> callapi = api.employeetoken(eemail);
        callapi.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {

                try {
                    JSONObject obj = new JSONObject(response.body().toString());
                    Log.e("Response",response.body().toString());
                    String msg = obj.getString("status");

//                    if (msg.equals("200")){
                        Toast.makeText(SignUp.this, "Successful", Toast.LENGTH_SHORT).show();
                        Intent i=new Intent(SignUp.this,MainActivity.class);
                        signuppb.setVisibility(View.GONE);
                        startActivity(i);
                        finish();
//                    }

                }catch (JSONException j){
                    j.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

            }
        });


    }

    private boolean isValidEmaillId(String email) {
        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==000){

            GoogleSignInResult result=Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//            GoogleSignInResult signin=Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            getDetails(result);
        }
    }

    private void getDetails(GoogleSignInResult result) {

        GoogleSignInAccount account=result.getSignInAccount();

//        if (account != null){
            String ename=account.getDisplayName();
            String email=account.getEmail();

            try {
                 picurl=account.getPhotoUrl().toString();
            }catch (Exception e){

            }


            Log.e("url==",picurl);


            edtname.setText(ename);
            edtemail.setText(email);
            Log.e("Respons>>",ename + " " + email);
//        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Auth.GoogleSignInApi.signOut(apiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {

                    }
                });

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
