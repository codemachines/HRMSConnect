package com.example.hrmsconnect;

import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.JsonElement;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimeSheet extends AppCompatActivity {

    EditText edtatid,edtt,edtst,edtet,edtdesc,edtstatus;

    ProgressBar pbtimesheet;

    Button btnadddetails;

    ImageView ivst,ivet;

    SharedPreferences preferences;
    private int  mHour, mMinute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_sheet);

        edtatid=findViewById(R.id.edtatid);
        edtt=findViewById(R.id.edtt);
        edtst=findViewById(R.id.edtst);
        edtet=findViewById(R.id.edtet);
        edtdesc=findViewById(R.id.edtdesc);
        edtstatus=findViewById(R.id.edtstatus);

        pbtimesheet=findViewById(R.id.pbtimesheet);

        ivst=findViewById(R.id.ivst);
        ivet=findViewById(R.id.ivet);

        btnadddetails=findViewById(R.id.btnadddetails);

        final Calendar c=Calendar.getInstance();

        ivst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHour=c.get(Calendar.HOUR_OF_DAY);
                mMinute=c.get(Calendar.MINUTE);

                TimePickerDialog dialog=new TimePickerDialog(TimeSheet.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        edtst.setText(hourOfDay + ":" + minute);
                    }
                },mHour,mMinute,false);
                dialog.show();
            }
        });

        ivet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHour=c.get(Calendar.HOUR_OF_DAY);
                mMinute=c.get(Calendar.MINUTE);

                TimePickerDialog dialog=new TimePickerDialog(TimeSheet.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        edtet.setText(hourOfDay + ":" + minute);
                    }
                },mHour,mMinute,false);
                dialog.show();
            }
        });

        btnadddetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pbtimesheet.setVisibility(View.VISIBLE);
                btnadddetails.setVisibility(View.GONE);
                addDetails();
            }
        });

    }

    private void addDetails() {

        String edtaid=edtatid.getText().toString();
        String edttitle=edtt.getText().toString();
        String edst=edtst.getText().toString();
        String edet=edtet.getText().toString();
        String eddesc=edtdesc.getText().toString();
        String edstatus=edtstatus.getText().toString();

        preferences = this.getApplication().getSharedPreferences(MainActivity.MyPREFERENCES, MODE_PRIVATE);

        ApiInterface apiInterface=ApiClient.getClient().create(ApiInterface.class);
        Call<JsonElement> call=apiInterface.timesheet(preferences.getString("emp_id",""),edtaid,edttitle,edst,edet,eddesc,edstatus);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {

                try {
                    JSONObject object = new JSONObject(response.body().toString());
                    String msg = object.getString("status");

                    if (msg.equals("200")) {
                        Toast.makeText(TimeSheet.this, "Timesheet updated Successfully", Toast.LENGTH_SHORT).show();
                        finish();
                        pbtimesheet.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(TimeSheet.this, "Something went Wrong Please Try Again", Toast.LENGTH_SHORT).show();
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
