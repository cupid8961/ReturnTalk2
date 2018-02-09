package com.app.alien.returntalk;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.alien.component.Message;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


public class SecondFragment extends Fragment
{
    //private    ArrayList<Message> arrayList_SMS ;
    private    TextView tv_debug;
    private Context mContext;

    public SecondFragment()
    {
    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        mContext = getActivity();




    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
        initListener();
        initVal();

    }

    public void initVal() {

        SharedPreferences prefs = getActivity().getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        int state_launcher = prefs.getInt("state_launcher", -1);
        int event_index = prefs.getInt("event_index", -1);
        Log.i("returntalk", "secondFragment/initVal/event_index : "+event_index);

        // 커스텀 아답타 생성
        MyAdapter adapter = new MyAdapter ( getActivity(),   R.layout.row_event,   event_index);

        // GridView gv = (GridView)getView().findViewById(R.id.gv_01);
        ExpandableHeightGridView mAppsGrid = (ExpandableHeightGridView) getView().findViewById(R.id.gv_01);
        mAppsGrid.setExpanded(true);
        mAppsGrid.setAdapter(adapter);  // 커스텀 아답타를 GridView 에 적용


        // GridView 아이템을 클릭하면 상단 텍스트뷰에 position 출력
        // JAVA8 에 등장한 lambda expression 으로 구현했습니다. 코드가 많이 간결해지네요
        mAppsGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                tv_debug.setText("position : " + position);
                Intent intent = new Intent(mContext, EventdetailActivity.class);
                intent.putExtra("data", "Test Popup");
                startActivityForResult(intent, 1);


            }
        });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1){
            if(resultCode==RESULT_OK){
                //데이터 받기
                String result = data.getStringExtra("result");
                tv_debug.setText(result);
            }
        }
    }

    public void initListener() {

    }

    public void initView() {

        tv_debug= (TextView)getView().findViewById(R.id.tv_sec_debug);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.fragment_second, container, false);
        return layout;
    }

    public void refreshVal() {

    }


    class MyAdapter extends BaseAdapter {
        Context context;
        int layout;
        int event_cnt;
        LayoutInflater inf;
        ImageView iv;
        TextView tv_event_name_f2,   tv_reply_cnt_f2;

        public MyAdapter(Context context, int layout, int event_cnt) {
            this.context = context;
            this.layout = layout;
            this.event_cnt = event_cnt;
            inf = (LayoutInflater) context.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return event_cnt;
        }

        @Override
        public Object getItem(int position) {
            return R.drawable.img_mail;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView==null)
                convertView = inf.inflate(layout, null);
            iv = (ImageView)convertView.findViewById(R.id.imageView1);
            tv_event_name_f2= (TextView)convertView.findViewById(R.id.tv_event_name_f2);
            tv_reply_cnt_f2= (TextView)convertView.findViewById(R.id.tv_reply_cnt_f2);





            SharedPreferences prefs = mContext.getSharedPreferences("pref", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();

            String event_name = prefs.getString("name_event_"+position, "error");
            int cnt_reply =  prefs.getInt("cnt_reply_"+position, 0);

            tv_reply_cnt_f2.setText(""+cnt_reply);
            tv_event_name_f2.setText(event_name);
            iv.setImageResource(R.drawable.img_mail);

            return convertView;
        }
    }

}

