package com.app.alien.returntalk;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import static android.content.Context.MODE_PRIVATE;


public class FirstFragment extends Fragment
{
    private Switch switch_launcher;
    private TextView tv_debug;
    private EditText et_msg_simple;
    private int MY_PERMISSIONS_REQUEST_SMS_RECEIVE = 10;
    private Button btn_pt_pn;
    private EditText et_input;


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

        tv_debug = (TextView)  getView().findViewById(R.id.tv_debug);
        //et_msg_simple = (EditText) getView().findViewById(R.id.et_msg_simple);
        Button btn_inner_sim = (Button)getView().findViewById(R.id.btn_inner_sim);
        Button btn_inner_ser = (Button)getView().findViewById(R.id.btn_inner_ser);

        et_input = (EditText)getView().findViewById(R.id.et_input);
        btn_inner_sim.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                et_input.setText("aa");
            }
        });

        btn_inner_ser.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                et_input.setText("bb");

            }
        });



        switch_launcher= (Switch) getView().findViewById(R.id.switch_launcher);
        final BroadcastReceiver myReceiver = new Broadcast();
        btn_pt_pn =(Button)  getView().findViewById(R.id.btn_pt_pn);
        btn_pt_pn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                Intent intent = new Intent(getActivity(), PresentationActivity.class);
                startActivity(intent);

            }
        });


        switch_launcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton cb, boolean on){
                if(on)
                {
                    //Do something when Switch button is on/checked
                    tv_debug.setText("Switch is on.....");


                    // 브로드캐스트 리시버 등록
                    //IntentFilter intentFilter = new IntentFilter();

                    IntentFilter intentFilter = new IntentFilter(Intent.ACTION_SCREEN_ON);
                    intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
                    intentFilter.addAction(Intent.ACTION_BOOT_COMPLETED);
                    intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
                    getActivity().getBaseContext().registerReceiver(myReceiver, intentFilter);

                    //문장 프레퍼런스 저장
                    SharedPreferences prefs = getActivity().getSharedPreferences("pref", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    //editor.putString("str_simple", et_msg_simple.getText().toString());
                    editor.putString("str_simple", et_input.getText().toString());
                    editor.putBoolean("state_launcher",true);
                    editor.commit();


                    //SMS퍼미션 따로추가
                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.RECEIVE_SMS},MY_PERMISSIONS_REQUEST_SMS_RECEIVE);



                    Log.i("returntalk","registerReceiver");


                }
                else
                {
                    //Do something when Switch is off/unchecked
                    tv_debug.setText("Switch is off.....");

                    //문장 프레퍼런스 저장
                    SharedPreferences prefs = getActivity().getSharedPreferences("pref", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("state_launcher",false);
                    editor.commit();

                    //getActivity().getBaseContext().unregisterReceiver(myReceiver);

                    Log.i("returntalk","unregisterReceiver");

                }
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
