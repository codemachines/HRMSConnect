package com.example.hrmsconnect;

import android.arch.core.executor.TaskExecutor;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPassword extends AppCompatActivity {

    Button btnresetpwd,btnsendotp;
    ProgressBar progressBar;
    EditText edtforgotpwd,edtotp;
    TextInputLayout tiotp;

    String mobileno;
    String ccode="+91";

    String mVerificationId;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        auth=FirebaseAuth.getInstance();

        btnresetpwd=findViewById(R.id.btnresetpwd);
        btnsendotp=findViewById(R.id.btnsendotp);

        progressBar=findViewById(R.id.progressbar);

        edtforgotpwd=findViewById(R.id.edtforgotpwd);
        edtotp=findViewById(R.id.edtotp);
        tiotp=findViewById(R.id.tiotp);

        mobileno=edtforgotpwd.getText().toString();

        btnsendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
                Call<JsonElement> call=api.mobileVerification(mobileno);

                call.enqueue(new Callback<JsonElement>() {
                    @Override
                    public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {

                        try {
                            JSONObject object=new JSONObject(response.body().toString());
                            String msg=object.getString("status");

                            if (msg.equals("200")){
                                mobileno=edtforgotpwd.getText().toString();
                                String code=ccode + mobileno;
                                sendVerificationcode(code);
                                tiotp.setVisibility(View.VISIBLE);
                                btnsendotp.setVisibility(View.INVISIBLE);
                                btnresetpwd.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.VISIBLE);

                            }
                            else {
                                Toast.makeText(ForgotPassword.this, "Mobile No. Is not Registered", Toast.LENGTH_SHORT).show();
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

        btnresetpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp=edtotp.getText().toString();

                mobileno=edtforgotpwd.getText().toString();
                Intent i=new Intent(ForgotPassword.this,ResetPassword.class);
                i.putExtra("mobileno",mobileno);

                verifyVerificationcode(otp);
            }
        });

    }

    private void sendVerificationcode(String code) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                code,60, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD,mCallbacks
        );
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            mVerificationId=s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code=phoneAuthCredential.getSmsCode();
            Log.e("Code>>",code+"");
            if (code!=null){
                edtotp.setText(code);
                verifyVerificationcode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(ForgotPassword.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    private void verifyVerificationcode(String otp) {

        PhoneAuthCredential credential= PhoneAuthProvider.getCredential(mVerificationId,otp);
        SigninwithphoneAuthCredential(credential);
    }

    private void SigninwithphoneAuthCredential(PhoneAuthCredential credential) {

        auth.signInWithCredential(credential).addOnCompleteListener(ForgotPassword.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    Intent i=new Intent(ForgotPassword.this,ResetPassword.class);
                    i.setFlags(i.FLAG_ACTIVITY_NEW_TASK | i.FLAG_ACTIVITY_CLEAR_TASK);
                    i.putExtra("mobileno",mobileno);
                    startActivity(i);
                }else {
                    String message = "Something went Wrong,we will fix it soon";

                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                        message="Invalid Code Entered";
                    }

                    Snackbar snackbar=Snackbar.make(findViewById(R.id.parent),message,Snackbar.LENGTH_LONG);
                    snackbar.setAction("Dismiss", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                    snackbar.show();
                }
            }
        });
    }
}
