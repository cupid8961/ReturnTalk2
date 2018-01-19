package com.app.alien.returntalk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class MainActivity extends AppCompatActivity
{
    ViewPager vp;
    Button btn_first;
    Button btn_second;
    Button btn_third;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        mContext = getBaseContext();


        vp = (ViewPager)findViewById(R.id.vp);
         btn_first = (Button)findViewById(R.id.btn_first);
         btn_second = (Button)findViewById(R.id.btn_second);
         //btn_third = (Button)findViewById(R.id.btn_third);

        vp.setAdapter(new pagerAdapter(getSupportFragmentManager()));
        vp.setCurrentItem(0);

        btn_first.setOnClickListener(movePageListener);
        btn_first.setTag(0);
        btn_second.setOnClickListener(movePageListener);
        btn_second.setTag(1);
       // btn_third.setOnClickListener(movePageListener);
        //btn_third.setTag(2);


    }


    View.OnClickListener movePageListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            int tag = (int) v.getTag();
            vp.setCurrentItem(tag);
            changeTextColor(tag);
        }
    };

    private void changeTextColor(int tag) {
        if(tag==1){

            btn_first.setTextColor(Color.parseColor(FirstFragment.STRCOLOR_BLUE));
            btn_second.setTextColor(Color.parseColor(FirstFragment.STRCOLOR_GRAY));

        }else{
            btn_first.setTextColor(Color.parseColor(FirstFragment.STRCOLOR_BLUE));
            btn_second.setTextColor(Color.parseColor(FirstFragment.STRCOLOR_GRAY));

        }

    }

    private class pagerAdapter extends FragmentStatePagerAdapter
    {
        public pagerAdapter(android.support.v4.app.FragmentManager fm)
        {
            super(fm);
        }
        @Override
        public android.support.v4.app.Fragment getItem(int position)
        {
            switch(position)
            {
                case 0: {
                    return new FirstFragment();
                }
                case 1: {
                    return new SecondFragment();
                }

                case 2:
                    return new ThirdFragment();
                default:
                    return null;
            }
        }
        @Override
        public int getCount()
        {
            return 3;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }
}
