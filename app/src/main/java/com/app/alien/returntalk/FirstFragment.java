package com.app.alien.returntalk;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.Context.MODE_PRIVATE;


public class FirstFragment extends Fragment
{
    private Context mContext;
    //private Switch switch_launcher;
    private TextView tv_on , tv_off;
    private TextView tv_debug;
    private EditText et_msg_simple;
    private int MY_PERMISSIONS_REQUEST_SMS_RECEIVE = 10;
    private ImageButton btn_pt_pn, btn_option;
    private EditText et_simple;
    public static final String STRCOLOR_BLUE = "#5285c4";
    public static final String STRCOLOR_GRAY = "#AAAAAA";
    public static final String STRCOLOR_RED = "#FF6C6C";

    public FirstFragment()
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);





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
                case 0:
                    return new FirstFragment();
                case 1:
                    return new SecondFragment();
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
    public void onStart() {
        super.onStart();

        mContext = getActivity();
        tv_on = (TextView) getView().findViewById(R.id.tv_on);
        tv_off = (TextView) getView().findViewById(R.id.tv_off);

        tv_debug = (TextView)  getView().findViewById(R.id.tv_debug);
        //et_msg_simple = (EditText) getView().findViewById(R.id.et_msg_simple);

        et_simple = (EditText)getView().findViewById(R.id.et_simple);

        btn_option =(ImageButton)  getView().findViewById(R.id.btn_option);
        btn_option.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Toast.makeText(mContext, "구현중입니다.", Toast.LENGTH_SHORT).show();
                /*
                Intent intent = new Intent(getActivity(), PresentationActivity.class);
                startActivity(intent);
                */

            }
        });


        final BroadcastReceiver myReceiver = new Broadcast();
        btn_pt_pn =(ImageButton)  getView().findViewById(R.id.btn_pt_pn);
        btn_pt_pn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                Intent intent = new Intent(getActivity(), PresentationActivity.class);
                startActivity(intent);

            }
        });

        tv_on.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                //Do something when Switch button is on/checked
                tv_debug.setText("Switch is on.....");

                Toast.makeText(mContext, "문자자동응답이 시작되었습니다.", Toast.LENGTH_SHORT).show();

                // 브로드캐스트 리시버 등록
                //IntentFilter intentFilter = new IntentFilter();

                IntentFilter intentFilter = new IntentFilter(Intent.ACTION_SCREEN_ON);
                intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
                intentFilter.addAction(Intent.ACTION_BOOT_COMPLETED);
                intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
                getActivity().getBaseContext().registerReceiver(myReceiver, intentFilter);


                //프레퍼런스불러오기
                SharedPreferences prefs = getActivity().getSharedPreferences("pref", MODE_PRIVATE);
                int event_index = prefs.getInt("event_index", 0);





                //문장 프레퍼런스 저장
                SharedPreferences.Editor editor = prefs.edit();
                //editor.putString("str_simple", et_msg_simple.getText().toString());

                editor.putInt("event_index", event_index);

                Log.i("returntalk","현재저장되는 event_index : "+event_index);
                editor.putString("str_simple_"+event_index, et_simple.getText().toString());
                editor.putBoolean("state_launcher",true);
                editor.commit();


                //SMS퍼미션 따로추가
                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.RECEIVE_SMS},MY_PERMISSIONS_REQUEST_SMS_RECEIVE);

                tv_on.setTextColor(Color.parseColor(STRCOLOR_BLUE));
                tv_off.setTextColor(Color.parseColor(STRCOLOR_GRAY));
                Log.i("returntalk","registerReceiver");
            }
        });


        tv_off.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                //Do something when Switch is off/unchecked
                tv_debug.setText("Switch is off.....");
                Toast.makeText(mContext, "문자자동응답이 종료되었습니다..", Toast.LENGTH_SHORT).show();
                SharedPreferences prefs = getActivity().getSharedPreferences("pref", MODE_PRIVATE);
                int event_index = prefs.getInt("event_index", 0);


                //문장 프레퍼런스 저장
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("state_launcher",false);
                editor.putInt("event_index", ++event_index);
                editor.commit();

                //getActivity().getBaseContext().unregisterReceiver(myReceiver);
                tv_off.setTextColor(Color.parseColor(STRCOLOR_BLUE));
                tv_on.setTextColor(Color.parseColor(STRCOLOR_GRAY));
                Log.i("returntalk","unregisterReceiver");

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.fragment_first, container, false);
        return layout;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_SMS_RECEIVE) {
            // YES!!
            Log.i("returntalk", "MY_PERMISSIONS_REQUEST_SMS_RECEIVE --> YES");
        }
    }
}
