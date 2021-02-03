package com.example.hrmsconnect;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WelcomeScreen extends AppCompatActivity {

    ViewPager welcome;
    LinearLayout lldots;
    Integer []layouts={R.layout.welcome_layout_1,R.layout.welcome_layout_2,R.layout.welcome_layout_3};
    Button btnnext;
    MyViewPagerAdapter mvpadapter;
    TextView[] dots;
    int currentpage=0;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        welcome=findViewById(R.id.welcome);
        lldots=findViewById(R.id.lldots);
        btnnext=findViewById(R.id.btnnext);

        preferences=getSharedPreferences(MainActivity.MyPREFERENCES,MODE_PRIVATE);
        try {
            if (preferences.getString("viewpager","").equals("yes")){
                Intent i=new Intent(WelcomeScreen.this,HomeActivity.class);
                startActivity(i);
                finish();
            }
        }
        catch (Exception e)
        {
        }

        getViewPager();



        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor=preferences.edit();
                editor.putString("viewpager","yes");
                editor.commit();
                Intent i=new Intent(WelcomeScreen.this,HomeActivity.class);
                startActivity(i);
                finishActivity(R.layout.activity_welcome_screen);
            }
        });
    }

    private void getViewPager() {

        addots(0);

        mvpadapter=new MyViewPagerAdapter();
        welcome.setAdapter(mvpadapter);
        welcome.addOnPageChangeListener(listener);
    }

    ViewPager.OnPageChangeListener listener=new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            addots(i);
            currentpage=i;

            if (i==layouts.length-1){
                btnnext.setVisibility(View.VISIBLE);
            }
            else {
                btnnext.setVisibility(View.GONE);
            }

        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

    private void addots(int i) {

        dots=new TextView[layouts.length];

        lldots.removeAllViews();
        for (int c=0;c<dots.length;c++){
            dots[c]=new TextView(this);
            dots[c].setText(Html.fromHtml("&#8226;"));
            dots[c].setTextSize(35);
            dots[c].setTextColor(Color.parseColor("#EEEEEE"));
        }

        if (dots.length>0){
            dots[currentpage].setTextColor(Color.parseColor("#000000"));
        }
    }

    private class MyViewPagerAdapter extends PagerAdapter {

        LayoutInflater inflater;
        @Override
        public int getCount() {
            return layouts.length;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {

            inflater= (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View view=inflater.inflate(layouts[position],container,false);
            container.addView(view);

            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

            View view= (View) object;
            container.removeView(view);
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view==o;
        }
    }
}
