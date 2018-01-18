package com.app.alien.returntalk;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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


public class SecondFragment extends Fragment
{
    //private    ArrayList<Message> arrayList_SMS ;
    private    TextView tv_debug;

    public SecondFragment()
    {
    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);




    }
//sms문자내역 가져오는 코드입니다.
/*
    public int readSMSMessage() {
        Uri allMessage = Uri.parse("content://sms");
        ContentResolver cr = getActivity().getContentResolver();
        Cursor c = cr.query(allMessage,
                new String[]{"_id", "thread_id", "address", "person", "date", "body"},
                null, null,
                "date DESC");

        while (c.moveToNext()) {
            Message msg = new Message(); // 따로 저는 클래스를 만들어서 담아오도록 했습니다.

            long messageId = c.getLong(0);
            msg.setMessageId(String.valueOf(messageId));

            long threadId = c.getLong(1);
            msg.setThreadId(String.valueOf(threadId));

            String address = c.getString(2);
            msg.setAddress(address);

            long contactId = c.getLong(3);
            msg.setContactId(String.valueOf(contactId));

            String contactId_string = String.valueOf(contactId);
            msg.setContactId_string(contactId_string);

            long timestamp = c.getLong(4);
            msg.setTimestamp(String.valueOf(timestamp));

            String body = c.getString(5);
            msg.setBody(body);

            arrayList_SMS.add(msg); //이부분은 제가 arraylist에 담으려고 하기떄문에 추가된부분이며 수정가능합니다.

        }
        return 0;
    }
    */

    @Override
    public void onStart() {
        super.onStart();

        tv_debug= (TextView)getView().findViewById(R.id.tv_sec_debug);


        long time = System.currentTimeMillis();
        SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String date_now = dayTime.format(new Date(time));


        tv_debug.setText("now time : "+ date_now);

        int img[] = {
                R.drawable.img_mail,R.drawable.img_mail,R.drawable.img_mail,R.drawable.img_mail,R.drawable.img_mail,R.drawable.img_mail,R.drawable.img_mail,R.drawable.img_mail,R.drawable.img_mail,R.drawable.img_mail
        };

        // 커스텀 아답타 생성
        MyAdapter adapter = new MyAdapter ( getActivity(),   R.layout.row_event,   img);

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
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.fragment_second, container, false);
        return layout;
    }
    class MyAdapter extends BaseAdapter {
        Context context;
        int layout;
        int img[];
        LayoutInflater inf;

        public MyAdapter(Context context, int layout, int[] img) {
            this.context = context;
            this.layout = layout;
            this.img = img;
            inf = (LayoutInflater) context.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return img.length;
        }

        @Override
        public Object getItem(int position) {
            return img[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView==null)
                convertView = inf.inflate(layout, null);
            ImageView iv = (ImageView)convertView.findViewById(R.id.imageView1);
            iv.setImageResource(img[position]);

            return convertView;
        }
    }

}

