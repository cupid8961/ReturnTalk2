package com.app.alien.returntalk;

import android.content.Context;
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


public class SecondFragment extends Fragment
{
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

        int img[] = {
                R.drawable.img_mail,R.drawable.img_mail,R.drawable.img_mail,R.drawable.img_mail,R.drawable.img_mail
        };
/*
        // 커스텀 아답타 생성
        MyAdapter adapter = new MyAdapter ( getActivity(),   R.layout.row_event,   img);

        final TextView tv = (TextView)getView().findViewById(R.id.tv_sec_debug);

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
                tv.setText("position : " + position);
            }
        });
*/
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

