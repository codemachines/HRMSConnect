package com.example.hrmsconnect;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.JsonElement;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddTask extends AppCompatActivity {

    EditText edtt,edtdeadline,edtgb,edtdesc;

    Button btntask;

    ProgressBar pbemptask;

    ImageView ivdeadline;

    SharedPreferences preferences;

    SimpleDateFormat dateformat;

    final Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        edtt=findViewById(R.id.edtt);
        edtdeadline=findViewById(R.id.edtdeadline);
        edtgb=findViewById(R.id.edtgb);
        edtdesc=findViewById(R.id.edtdesc);

        btntask=findViewById(R.id.btntask);

        pbemptask=findViewById(R.id.pbemptask);

        ivdeadline=findViewById(R.id.ivdeadline);

        dateformat = new SimpleDateFormat("MM/dd/yyyy");

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                setDate();

            }
        };

        ivdeadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddTask.this, date,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btntask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskData();
            }
        });

    }

    private void taskData() {
        String tt=edtt.getText().toString();
        String tdeadline=edtdeadline.getText().toString();
        String tgb=edtgb.getText().toString();
        String tdesc=edtdesc.getText().toString();

        preferences = this.getApplication().getSharedPreferences(MainActivity.MyPREFERENCES, MODE_PRIVATE);

        ApiInterface apiInterface=ApiClient.getClient().create(ApiInterface.class);
        Call<JsonElement> call=apiInterface.emptask(preferences.getString("emp_id",""),tt,tdeadline,tgb,tdesc);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {

                try {
                    JSONObject object = new JSONObject(response.body().toString());
                    String msg = object.getString("status");

                    if (msg.equals("200")) {
                        Toast.makeText(AddTask.this, "Task Saved Successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(AddTask.this, "Something went Wrong Please Try Again", Toast.LENGTH_SHORT).show();
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

    private void setDate() {
        edtdeadline.setText(dateformat.format(calendar.getTime()));
    }
}
