package com.example.hrmsconnect;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import fragments.LeaveFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeaveForm extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    final Calendar calendar = Calendar.getInstance();
    Spinner spinner;
    EditText edtsdate, edtedate, edtreason, edtapplyto, edtccto;
    TextView tvdays, tvbalance;
    ImageView ivdatefrom, ivdateto;
    Button btnlsubmit;
    ProgressBar pbleave;
    SharedPreferences preferences;
    String[] leavetype = {"Full Day", "Half Day"};
    SimpleDateFormat dateformat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_form);

        dateformat = new SimpleDateFormat("MM/dd/yyyy");
        final SimpleDateFormat check = new SimpleDateFormat("yyyy-MM-dd");

        spinner = findViewById(R.id.spinner);

        edtsdate = findViewById(R.id.edtsdate);
        edtedate = findViewById(R.id.edtedate);
        edtreason = findViewById(R.id.edtreason);
        edtapplyto = findViewById(R.id.edtapplyto);
        edtccto = findViewById(R.id.edtccto);

        edtapplyto.setText("hrmsproject6@gmail.com");

        ivdatefrom = findViewById(R.id.ivdatefrom);
        ivdateto = findViewById(R.id.ivdateto);

        tvdays = findViewById(R.id.tvday);
        tvbalance = findViewById(R.id.tvbalance);

        pbleave=findViewById(R.id.pbleave);

        btnlsubmit = findViewById(R.id.btnlsubmit);

        preferences = getSharedPreferences(MainActivity.MyPREFERENCES, MODE_PRIVATE);

        tvbalance.setText(preferences.getString("balance",""));

        spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) LeaveForm.this);


        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                setDate();

            }
        };

        final DatePickerDialog.OnDateSetListener dateto = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                setDate2();

            }
        };

        ivdatefrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(LeaveForm.this, date,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        ivdateto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(LeaveForm.this, dateto,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        ArrayAdapter<String> aa = new ArrayAdapter<>(LeaveForm.this, android.R.layout.simple_spinner_item, leavetype);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(aa);


        btnlsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int days = Integer.parseInt(tvdays.getText().toString());
                int balance = Integer.parseInt(tvbalance.getText().toString());

                pbleave.setVisibility(View.VISIBLE);

                if (edtsdate.getText().toString().isEmpty()) {
                    edtsdate.setError("Please Select Date");
                } else if (edtedate.getText().toString().isEmpty()) {
                    edtedate.setError("Please Select Date");
                } else if (edtsdate.getText().toString().equals(check)) {
                    edtsdate.setError("Invalid Date Format");
                } else if (edtedate.getText().toString().equals(check)) {
                    edtedate.setError("Invalid Date Format");
                } else if (edtreason.getText().toString().isEmpty()) {
                    edtreason.setError("Please Enter Detail Description reason for Leave");
                } else if (edtapplyto.getText().toString().isEmpty()) {
                    edtapplyto.setError("Please enter Email");
                } else if (edtccto.getText().toString().isEmpty()) {
                    edtccto.setError("Please enter Email");
                } else if (!isValidEmaillId(edtapplyto.getText().toString().trim())) {
                    edtapplyto.setError("Enter Valid Email ID");
                } else if (!isValidEmaillId(edtccto.getText().toString().trim())) {
                    edtccto.setError("Enter Valid Email ID");
                } else if (tvbalance.getText().toString().equals("0")) {
                    tvbalance.setText("You Don't have enough Leave Balance Please contact H.R. Admin");
                } else if (days > balance) {
                    Toast.makeText(LeaveForm.this, "You Don't have Enough Leave Balance Please Contact H.R. Admin", Toast.LENGTH_SHORT).show();
                } else {
                    postLeaveData();
                }


            }
        });

    }

    private void postLeaveData() {

        preferences = getSharedPreferences(MainActivity.MyPREFERENCES, MODE_PRIVATE);


        String ltype = spinner.getSelectedItem().toString();
        Log.e("ltype===",ltype);
        String sdate = edtsdate.getText().toString();
        String edate = edtedate.getText().toString();
        String days = tvdays.getText().toString();
        String reason = edtreason.getText().toString();
        String applyto = edtapplyto.getText().toString();
        String ccto = edtccto.getText().toString();

        String daysdif = tvdays.getText().toString();
        String balance = tvbalance.getText().toString();

        long daysdiff = Long.parseLong(daysdif);
        long bal = Long.parseLong(balance);

        long balleft = bal - daysdiff;

        String leftbal = Long.toString(balleft);

        Log.e("Balance====",leftbal);


        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonElement> call = api.empleave(preferences.getString("emp_id", ""), ltype, sdate, edate, reason, days, leftbal, applyto, ccto);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {

                Log.e("Response>>>>",response.body().toString());
                try {
                    JSONObject object = new JSONObject(response.body().toString());
                    String msg = object.getString("status");

                    if (msg.equals("200")) {
                        Toast.makeText(LeaveForm.this, "Leave Request Has Been Sent", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(LeaveForm.this, "Something went Wrong Please Try Again", Toast.LENGTH_SHORT).show();
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
        edtsdate.setText(dateformat.format(calendar.getTime()));
    }

    private void setDate2() {
        edtedate.setText(dateformat.format(calendar.getTime()));

        String fromd = edtsdate.getText().toString();
        String tod = edtedate.getText().toString();

        try {
            Date date1 = dateformat.parse(fromd);
            Date date2 = dateformat.parse(tod);

            long dayscount = Math.abs(date2.getTime() - date1.getTime());
            long daysdiff = dayscount / (24 * 60 * 60 * 1000)+1;

            String daysdif = Long.toString(daysdiff);

            tvdays.setText(daysdif);


            Log.e("Response>>", daysdif);

        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    private boolean isValidEmaillId(String email) {
        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

    /*@Override
    public void onBackPressed() {
        super.onBackPressed();
        //startActivity(new Intent(LeaveForm.this, LeaveFragment.class));
        //finish();
    }*/

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String ltype=leavetype[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
