package fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hrmsconnect.ApiClient;
import com.example.hrmsconnect.ApiInterface;
import com.example.hrmsconnect.HomeActivity;
import com.example.hrmsconnect.MainActivity;
import com.example.hrmsconnect.R;
import com.google.gson.JsonElement;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class SettingsFragment extends Fragment {

    View view;
    TextView tvempname,tvempid,tvempbal;
    EditText edtempmobile,edtempname,edtememail,edtempdesignation,edtempaddress;
    ImageView ivemppic,ivprofile;
    Button btnupdate,btndelete;



    SharedPreferences preferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_settings,null);

        tvempname=view.findViewById(R.id.tvempname);
        tvempid=view.findViewById(R.id.tvempid);
        tvempbal=view.findViewById(R.id.tvempbal);

        edtempmobile=view.findViewById(R.id.edtempmobile);
        edtempname=view.findViewById(R.id.edtempname);
        edtememail=view.findViewById(R.id.edtememail);
        edtempdesignation=view.findViewById(R.id.edtempdesignation);
        edtempaddress=view.findViewById(R.id.edtempaddress);

        ivemppic=view.findViewById(R.id.ivemppic);

        btnupdate=view.findViewById(R.id.btnupdate);
        btndelete=view.findViewById(R.id.btndelete);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }

        preferences=this.getActivity().getSharedPreferences(MainActivity.MyPREFERENCES,MODE_PRIVATE);

        tvempname.setText(preferences.getString("name", "").toUpperCase());

        edtempmobile.setText(preferences.getString("mobile",""));

        edtempname.setText(preferences.getString("name",""));

        edtememail.setText(preferences.getString("email",""));

        edtempdesignation.setText(preferences.getString("designation",""));

        edtempaddress.setText(preferences.getString("address",""));

        tvempid.setText(preferences.getString("emp_id",""));

        tvempbal.setText(preferences.getString("balance",""));

        final String url=preferences.getString("profile","");

        if (url != ""){
            Glide.with(getActivity()).load(url).into(ivemppic);
        }



        ivemppic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog dialog=new Dialog(getContext());
                dialog.setContentView(R.layout.edit_profile_pic);

                 ivprofile=dialog.findViewById(R.id.ivprofile);
                Button btnprofile=dialog.findViewById(R.id.btnprofile);

                Glide.with(getActivity()).load(url).into(ivprofile);

                btnprofile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent,"Select Picture"),000);
                    }
                });
                dialog.show();
            }
        });

        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });

        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setMessage("Are You sure you want to delete this Account?");
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteEmp();
                    }
                });
                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().finish();
                    }
                });
                AlertDialog alertDialog=alertDialogBuilder.create();
                alertDialog.show();

            }
        });

        return view;
    }

    private void deleteEmp() {

        String empid=tvempid.getText().toString();

        ApiInterface apiInterface=ApiClient.getClient().create(ApiInterface.class);
        Call<JsonElement> elementCall=apiInterface.deleteuser(empid);
        elementCall.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {

                try {
                    JSONObject object=new JSONObject(response.body().toString());
                    String msg=object.getString("status");
                    Log.e("Response>>>>>>>>>>>>",msg);

                        if (msg.equals("200")){
                        Toast.makeText(getActivity(), "Employee Data Deleted", Toast.LENGTH_SHORT).show();
                        SharedPreferences.Editor editor=preferences.edit();
                        Intent i=new Intent(getActivity(),MainActivity.class);
                        startActivity(i);
                        editor.clear();
                        editor.commit();
                        getActivity().finish();
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

    private void updateData() {
        final String mobile=edtempmobile.getText().toString();
        final String name=edtempname.getText().toString();
        final String email=edtememail.getText().toString();
        final String designation=edtempdesignation.getText().toString();
        final String address=edtempaddress.getText().toString();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonElement> call = apiInterface.updateprofile(preferences.getString("emp_id", ""),name,mobile,email,designation,address);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {

                try {
                    JSONObject object = new JSONObject(response.body().toString());
                    String msg = object.getString("status");

                    if (msg.equals("200")){
                        Toast.makeText(getActivity(), "Profile Updated", Toast.LENGTH_SHORT).show();
                        SharedPreferences.Editor editor=preferences.edit();
                        editor.putString("email",email);
                        editor.putString("name",name);
                        editor.putString("designation",designation);
                        editor.putString("mobile",mobile);
                        editor.putString("address",address);
                        editor.commit();
                    }
                    else {
                        Toast.makeText(getActivity(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (resultCode==RESULT_OK){
                if (requestCode==000){
                    Uri imageuri=data.getData();
                    Bitmap image= MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),imageuri);
                    //imageView.setImageURI(imageuri);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] byteFormat = stream.toByteArray();
                    // get the base 64 string
                    final String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);

                    ApiInterface apiInterface= ApiClient.getClient().create(ApiInterface.class);
                    Call<JsonElement> callapi=apiInterface.updateprofilepic(preferences.getString("emp_id",""),imgString);

                    callapi.enqueue(new Callback<JsonElement>() {
                        @Override
                        public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {

                            Log.e("response>>>",response.body().toString());
                            try {
                                JSONObject object=new JSONObject(response.body().toString());
                                String msg=object.getString("status");

                                if (msg.equals("200")) {
                                    Toast.makeText(getActivity(), "Profile Changed", Toast.LENGTH_SHORT).show();
//                                    SharedPreferences.Editor editor=preferences.edit();
//                                    editor.putString("profile",imgString);
//                                    editor.commit();

                                } else {
                                    Toast.makeText(getActivity(), "Something went Wrong Please Try Again", Toast.LENGTH_SHORT).show();
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonElement> call, Throwable t) {

                        }
                    });
                    Log.e("Image>>>",imgString);
                    ivemppic.setImageBitmap(image);
                    ivprofile.setImageBitmap(image);
                }
            }
        }catch (Exception e){
            Log.e("File Selector Activity","File Select Error",e);
        }
    }
}
