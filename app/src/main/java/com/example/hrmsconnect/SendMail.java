package com.example.hrmsconnect;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendMail extends AppCompatActivity {

    EditText mailto,mailbody,mailsubject;
    Button btnsendmail;
    ImageView attachment,ivattach;
    SharedPreferences preferences;
    String imgString;
    ProgressBar pbsendmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_mail);

        mailto=findViewById(R.id.mailto);
        mailsubject=findViewById(R.id.mailsubject);
        mailbody=findViewById(R.id.mailbody);

        attachment=findViewById(R.id.attachment);
        ivattach=findViewById(R.id.ivattach);

        btnsendmail=findViewById(R.id.btnsendmail);

        pbsendmail=findViewById(R.id.pbsendmail);

        preferences=getSharedPreferences(MainActivity.MyPREFERENCES, MODE_PRIVATE);



        btnsendmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isValidEmaillId(mailto.getText().toString().trim())){
                    mailto.setError("Invalid Email ID");
                }else{
                    pbsendmail.setVisibility(View.VISIBLE);
                    sendmail();
                }
            }
        });

        ivattach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),000);

            }
        });
    }

    private void sendmail() {

        final String to=mailto.getText().toString();
        String body=mailbody.getText().toString();
        String subject=mailsubject.getText().toString();
        String documnet=imgString;

        ApiInterface apiInterface=ApiClient.getClient().create(ApiInterface.class);
        Call<JsonElement> call= apiInterface.mailsend(preferences.getString("email",""),to,subject,body,documnet);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                Log.e("Response>>>>",response.body().toString());

                try {
                    JSONObject object=new JSONObject(response.body().toString());
                    String msg=object.getString("status");

                    if (msg.equals("204")) {
                        Toast.makeText(SendMail.this, "Mail Sent Successfully", Toast.LENGTH_SHORT).show();


                        anothermethod();



                    } else {
                        Toast.makeText(SendMail.this, "Mail not sent", Toast.LENGTH_SHORT).show();
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

    private void anothermethod() {

        ApiInterface apiInterface=ApiClient.getClient().create(ApiInterface.class);
        Call<JsonElement> call=apiInterface.mailnotification(mailto.getText().toString());
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                Log.e("Response:",response.body().toString());

                try {
                    JSONObject object1=new JSONObject(response.body().toString());
                    String st=object1.getString("status");
                    finish();

                } catch (JSONException e) {
                    e.printStackTrace();
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

        try {
            if (resultCode == RESULT_OK) {
                if (requestCode == 000) {
                    Uri imageuri = data.getData();
                    Bitmap image = MediaStore.Images.Media.getBitmap(getContentResolver(), imageuri);
                    //imageView.setImageURI(imageuri);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] byteFormat = stream.toByteArray();
                    // get the base 64 string
                    imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
                    Log.e("Image>>>",imgString);
                    attachment.setImageBitmap(image);
                }

            }
        } catch (Exception e) {
            Log.e("File Selector Activity", "File Select Error", e);
        }
    }

}
