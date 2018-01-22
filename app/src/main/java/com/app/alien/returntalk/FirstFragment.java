package com.app.alien.returntalk;

import android.Manifest;
import android.app.PendingIntent;
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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.alien.component.CalendarUtils;
import com.app.alien.component.ListViewAdapter;

import java.util.Calendar;
import java.util.Date;

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
    private EditText et_simple ,et_event_name;
    public static final String STRCOLOR_BLUE = "#5285c4";
    public static final String STRCOLOR_GRAY = "#AAAAAA";
    public static final String STRCOLOR_RED = "#FF6C6C";
    private ListView listview_msg;
    private ListViewAdapter adapter_msg;

    private static int index_exam;
    private BroadcastReceiver myReceiver;
    private BroadcastReceiver sms_mReceiver;
    private int  mNo_event;
    private boolean is_tv_on ;

    private static final Boolean ISDEBUG =true;


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
        index_exam = 0;








        sms_mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                SharedPreferences prefs = mContext.getSharedPreferences("pref", MODE_PRIVATE);
                mNo_event = prefs.getInt("event_index", -1);

                //extract our message from intent
                int no_reply = intent.getIntExtra("no_reply",-1);
                int state = intent.getIntExtra("state",-1);
                //log our message value
                Log.i("returntalk","fragment01)br receive : no_reply : "+no_reply+"/state : "+state);

                if(state ==1){
                    addMsgItem(mNo_event,no_reply);

                }else if(state==2){
                    changeSMS_Blue(mNo_event,no_reply);
                }else{
                    Log.i("returntalk","sms_mReceiver / state error!!~@@");

                }
            }

        };










        // Adapter 생성
        adapter_msg = new ListViewAdapter() ;

        // 리스트뷰 참조 및 Adapter달기
        listview_msg = (ListView) getView().findViewById(R.id.lv_sms);
        listview_msg.setAdapter(adapter_msg);



        tv_on = (TextView) getView().findViewById(R.id.tv_on);
        tv_off = (TextView) getView().findViewById(R.id.tv_off);
        is_tv_on = false;

        tv_debug = (TextView)  getView().findViewById(R.id.tv_debug);
        //et_msg_simple = (EditText) getView().findViewById(R.id.et_msg_simple);

        et_simple = (EditText)getView().findViewById(R.id.et_simple);
        if(ISDEBUG) et_simple.setText("자동 응답앱 개발 테스트중..");

         et_event_name = (EditText)getView().findViewById(R.id.et_event_name);

        btn_option =(ImageButton)  getView().findViewById(R.id.btn_option);
        btn_option.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Toast.makeText(mContext, "구현중입니다.", Toast.LENGTH_SHORT).show();
            }
        });


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
                if (is_tv_on){

                    Toast.makeText(mContext, "이미 시작되었습니다.", Toast.LENGTH_SHORT).show();
                    return ;
                }else {
                    is_tv_on = true;
                    tv_debug.setText("Switch is on.....");

                    Toast.makeText(mContext, "문자자동응답이 시작되었습니다.", Toast.LENGTH_SHORT).show();


                    unregist_all_Receiver();


                    //인텐트받기
                    IntentFilter intentFilter_f01 = new IntentFilter(
                            "fragment01");
                    //registering our receiver
                    getActivity().getBaseContext().registerReceiver(sms_mReceiver, intentFilter_f01);


                    // 브로드캐스트 리시버 등록
                    myReceiver = new Broadcast();
                    IntentFilter intentFilter = new IntentFilter(Intent.ACTION_SCREEN_ON);
                    intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
                    intentFilter.addAction(Intent.ACTION_BOOT_COMPLETED);
                    intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
                    getActivity().getBaseContext().registerReceiver(myReceiver, intentFilter);


                    //프레퍼런스불러오기
                    SharedPreferences prefs = getActivity().getSharedPreferences("pref", MODE_PRIVATE);
                    int event_index = prefs.getInt("event_index", 0);


                    //현재시간 가져오기
                    Date time_now = new Date();


                    //문장 프레퍼런스 저장
                    SharedPreferences.Editor editor = prefs.edit();
                    //editor.putString("str_simple", et_msg_simple.getText().toString());

                    editor.putInt("event_index", event_index);
                    editor.putInt("no_reply_index", 0);
                    editor.putString("str_simple", et_simple.getText().toString());
                    editor.putBoolean("state_launcher", true);
                    editor.putLong("time_start", time_now.getTime());
                    editor.putString("name_event", et_event_name.getText().toString());

                    Log.i("returntalk", "현재저장되는 event_index : " + event_index);
                    editor.commit();


                    //SMS퍼미션 따로추가
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECEIVE_SMS}, MY_PERMISSIONS_REQUEST_SMS_RECEIVE);

                    tv_on.setTextColor(Color.parseColor(STRCOLOR_BLUE));
                    tv_off.setTextColor(Color.parseColor(STRCOLOR_GRAY));


                    Log.i("returntalk", "registerReceiver");
                }
            }
        });







        tv_off.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                //Do something when Switch is off/unchecked

                if (!is_tv_on){

                    Toast.makeText(mContext, "이미 정지되어 있습니다.", Toast.LENGTH_SHORT).show();
                    return ;
                }
                else {
                    is_tv_on = false;
                    tv_debug.setText("Switch is off.....");
                    Toast.makeText(mContext, "문자자동응답이 종료되었습니다..", Toast.LENGTH_SHORT).show();
                    SharedPreferences prefs = getActivity().getSharedPreferences("pref", MODE_PRIVATE);
                    int event_index = prefs.getInt("event_index", 0);


                    //현재시간 가져오기
                    Date time_now = new Date();

                    //문장 프레퍼런스 저장
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("state_launcher", false);
                    editor.putLong("time_end", time_now.getTime());
                    editor.putInt("event_index", ++event_index);
                    editor.putInt("no_reply_index", 0);
                    editor.commit();


                    tv_off.setTextColor(Color.parseColor(STRCOLOR_BLUE));
                    tv_on.setTextColor(Color.parseColor(STRCOLOR_GRAY));


                    //문자오는지 리시버로 감시해제
                    unregist_all_Receiver();


                    Log.i("returntalk", "tv_off");
                }

            }
        });
    }

    private void unregist_all_Receiver() {
        try  {
            if(sms_mReceiver!=null )
                getActivity().unregisterReceiver(sms_mReceiver);

            if(myReceiver!=null )
                getActivity().unregisterReceiver(myReceiver);
        }
        catch (IllegalArgumentException e) {
            // Check wether we are in debug mode

                e.printStackTrace();
        }

        Log.i("returntalk","unregisterReceiver");


    }

    private void check_BroadcastState() {
        Intent screenIntent = new Intent(getActivity().getBaseContext(), FirstFragment.class);
        PendingIntent screenSender = PendingIntent.getBroadcast(getActivity().getBaseContext(), 0, screenIntent, PendingIntent.FLAG_NO_CREATE);
        if (screenSender == null) {
            Log.i("returntalk","등록안된");
        }else{
            Log.i("returntalk","screenSender.toString() : "+screenSender.toString());
        }


    }

    private void changeSMS_Blue(int no_event, int no_reply) {

        Log.i("returntalk","changeSMS_Blue :"+no_event+" / "+no_reply);
        Log.i("returntalk","no_reply :"+no_reply+" /listview_msg.getCount() "+listview_msg.getCount());
        adapter_msg.setItem(no_reply,null,listview_msg,2);
        adapter_msg.notifyDataSetChanged();
    }

    private void addMsgItem(int no_event, int no_reply) {
        Log.i("returntalk","addMsgItem :"+no_event+" / "+no_reply);

        //preference 정보 가져오기
        SharedPreferences prefs = mContext.getSharedPreferences("pref", MODE_PRIVATE);
        Long time_receive = prefs.getLong("time_receive_"+no_event+"_"+no_reply, -1);

        String phonenum = prefs.getString("phone_num_"+no_event+"_"+no_reply, "");
        String msg_client = prefs.getString("msg_client_"+no_event+"_"+no_reply, "");

        String now_time = CalendarUtils.ConvertMilliSecondsToFormattedDate(time_receive+"");

        // 첫 번째 아이템 추가.
        adapter_msg.addItem(""+ no_reply,now_time,phonenum,msg_client);
        adapter_msg.setItem(no_reply,null,listview_msg,1); //빨강 변형.
        adapter_msg.notifyDataSetChanged();
        setListViewHeightBasedOnChildren(listview_msg);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.fragment_first, container, false);
        return layout;

    }
    public void setListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null) {

            // pre-condition

            return;

        }



        int totalHeight = 0;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);



        for (int i = 0; i < listAdapter.getCount(); i++) {

            View listItem = listAdapter.getView(i, null, listView);

            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

            totalHeight += listItem.getMeasuredHeight();

        }



        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));

        listView.setLayoutParams(params);

        listView.requestLayout();

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

