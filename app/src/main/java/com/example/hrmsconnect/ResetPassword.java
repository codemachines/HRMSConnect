package com.example.hrmsconnect;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.JsonElement;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPassword extends AppCompatActivity {

    EditText edtresetpwd,edtresetcpwd;
    Button btnreset;
    ProgressBar pb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        edtresetpwd=findViewById(R.id.edtresetpwd);
        edtresetcpwd=findViewById(R.id.edtresetcpwd);
        btnreset=findViewById(R.id.btnreset);
        pb=findViewById(R.id.pb);

        btnreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edtresetpwd.getText().toString().isEmpty()){
                    edtresetpwd.setError("This Field Should not remain Empty");
                }else if (edtresetcpwd.getText().toString().isEmpty())
                {
                    edtresetcpwd.setError("This Field Should not remain Empty");
                }else if (!edtresetcpwd.getText().toString().equals(edtresetpwd.getText().toString())){
                    Toast.makeText(ResetPassword.this, "Password Mismatch", Toast.LENGTH_SHORT).show();
                }else {
                    pb.setVisibility(View.VISIBLE);
                    reset();
                }
            }
        });


    }

    private void reset() {

        String password=edtresetpwd.getText().toString();

        ApiInterface apiInterface=ApiClient.getClient().create(ApiInterface.class);
        Call<JsonElement> call=apiInterface.resetPassword(getIntent().getStringExtra("mobileno"),password);
         call.enqueue(new Callback<JsonElement>() {
             @Override
             public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {

                 try {
                     JSONObject object=new JSONObject(response.body().toString());
                     String msg=object.getString("status");

                     if (msg.equals("200")){

                         Toast.makeText(ResetPassword.this, "Password Changed Successfully", Toast.LENGTH_SHORT).show();
                         startActivity(new Intent(ResetPassword.this,MainActivity.class));
                     }
                     else {
                         Toast.makeText(ResetPassword.this, "Something Went Wrong Please Try Again"+ Log.e("Error: ",response.body().toString()), Toast.LENGTH_SHORT).show();
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
