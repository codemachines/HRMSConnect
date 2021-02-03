package com.example.hrmsconnect;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.JsonElement;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URI;

import fragments.ChatFragment;
import fragments.DailyStatusFragment;
import fragments.HomeFragment;
import fragments.LeaveFragment;
import fragments.MailFragment;
import fragments.SalaryFragment;
import fragments.SettingsFragment;
import fragments.SmsFragment;
import fragments.TaskFragment;
import fragments.TodoFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.hrmsconnect.MainActivity.MyPREFERENCES;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FloatingActionButton fab;
    SharedPreferences preferences;
    TextView emp_name;
    TextView emp_email;
    ImageView imageView,ivprofile;
    String imgString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        emp_name=findViewById(R.id.emp_name);
        emp_email=findViewById(R.id.emp_email);

        NavigationView view=findViewById(R.id.nav_view);
        View headerview=view.getHeaderView(0);
        emp_name=headerview.findViewById(R.id.emp_name);
        emp_email=headerview.findViewById(R.id.emp_email);
        imageView=headerview.findViewById(R.id.imageview);

        getFragment(new HomeFragment(),false,"Home");

        preferences=getSharedPreferences(MainActivity.MyPREFERENCES,MODE_PRIVATE);
        emp_name.setText(preferences.getString("name",""));
        emp_email.setText(preferences.getString("email",""));
        final String url=preferences.getString("profile","");
        if (url!=""){
            Glide.with(getApplication()).load(url).into(imageView);
        }


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog dialog=new Dialog(HomeActivity.this);
                dialog.setContentView(R.layout.edit_profile_pic);

                ivprofile=dialog.findViewById(R.id.ivprofile);
                Button btnprofile=dialog.findViewById(R.id.btnprofile);

                if (url != ""){
                    Glide.with(getApplication()).load(url).into(ivprofile);
                }


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

         fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                fab.setVisibility(View.INVISIBLE);
                getFragment(new MailFragment(),true,"mail");
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void getFragment(Fragment fragment, boolean b, String type) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, fragment,type);
        if (b) {
            transaction.addToBackStack(null);
        }
        transaction.commit();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            getFragment(new SettingsFragment(),true,"Settings");
            fab.setVisibility(View.INVISIBLE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("RestrictedApi")
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            getFragment(new HomeFragment(), false, "Home");
            fab.setVisibility(View.VISIBLE);
        } else if (id == R.id.nav_salary) {
            getFragment(new SalaryFragment(), true, "salary");
            fab.setVisibility(View.INVISIBLE);
        } else if (id == R.id.nav_leave) {
            getFragment(new LeaveFragment(), true, "leave");
            fab.setVisibility(View.INVISIBLE);
        } else if (id == R.id.nav_dailystatus){
            getFragment(new DailyStatusFragment(),true,"daily_status");
            fab.setVisibility(View.INVISIBLE);
        }else if (id == R.id.nav_admintask){
            getFragment(new TaskFragment(),true,"admin_task");
            fab.setVisibility(View.INVISIBLE);
        }else if (id == R.id.nav_emptask){
            getFragment(new TodoFragment(),true,"ToDo");
            fab.setVisibility(View.INVISIBLE);
        } else if (id == R.id.nav_setting) {
            getFragment(new SettingsFragment(),true,"Settings");
            fab.setVisibility(View.INVISIBLE);
        }else if (id == R.id.nav_sms) {
            getFragment(new SmsFragment(),true,"SMS");
            fab.setVisibility(View.INVISIBLE);

        }else if (id == R.id.nav_mail) {
            getFragment(new MailFragment(),true,"Mail");

        }else if (id == R.id.nav_logout){
            Intent i=new Intent(HomeActivity.this,MainActivity.class);
            startActivity(i);
            SharedPreferences.Editor editor=preferences.edit();
            editor.clear();
            editor.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode==RESULT_OK){
                if (requestCode==000){
                    Uri imageuri=data.getData();
//                    String path=getPathFromUri(imageuri);
//                    if (path!=null){
//                        File f=new File(path);
//                        imageuri=Uri.fromFile(f);
//                    }
                    Bitmap image=MediaStore.Images.Media.getBitmap(getContentResolver(),imageuri);
                    //imageView.setImageURI(imageuri);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] byteFormat = stream.toByteArray();
                    // get the base 64 string
                    imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);

                    ApiInterface apiInterface=ApiClient.getClient().create(ApiInterface.class);
                    Call<JsonElement> callapi=apiInterface.updateprofilepic(preferences.getString("emp_id",""),imgString);

                    callapi.enqueue(new Callback<JsonElement>() {
                        @Override
                        public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {

                            Log.e("response>>>",response.body().toString());
                            try {
                                JSONObject object=new JSONObject(response.body().toString());
                                String msg=object.getString("status");

                                if (msg.equals("200")) {
//                                    SharedPreferences.Editor editor=preferences.edit();
//                                    editor.putString("profile",imgString);
//                                    editor.commit();
                                    Toast.makeText(HomeActivity.this, "Profile Changed", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(HomeActivity.this, "Something went Wrong Please Try Again", Toast.LENGTH_SHORT).show();
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
                    imageView.setImageBitmap(image);
                    ivprofile.setImageBitmap(image);
                }
            }
        }catch (Exception e){
            Log.e("File Selector Activity","File Select Error",e);
        }
    }

    private String getPathFromUri(Uri imageuri) {
        String res=null;
        String[] proj={MediaStore.Images.Media.DATA};

        Cursor cursor=getContentResolver().query(imageuri,proj,null,null,null);

        if (cursor.moveToFirst()){
            int column_index=cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res=cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }
}
