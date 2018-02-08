package com.app.alien.returntalk;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import static android.content.Context.MODE_PRIVATE;


public class ThirdFragment extends Fragment {
    private Context mContext;
    private EditText et_event_name_f3, et_msg_simple_f3;
    private TextView tv_f3_confirm;

    public ThirdFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.fragment_third, container, false);
        return layout;
    }

    @Override
    public void onStart() {
        super.onStart();
        mContext = getActivity();
        initView();
        initListener();
        initVal();

    }

    private void initView() {
        et_event_name_f3 = (EditText) getView().findViewById(R.id.et_event_name_f3);
        et_msg_simple_f3 = (EditText) getView().findViewById(R.id.et_msg_simple_f3);
        tv_f3_confirm = (TextView) getView().findViewById(R.id.tv_f3_confirm);
    }

    private void initListener() {
        tv_f3_confirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                Log.i("returntalk", "이벤트 제목 :" + et_event_name_f3.getText().toString() + "/ 메세지" +
                        et_msg_simple_f3.getText().toString());


                if (et_msg_simple_f3.getText().toString().equals("") || et_event_name_f3.getText().toString().equals("") ||
                        et_msg_simple_f3.getText().toString() == null || et_event_name_f3.getText().toString() == null) {

                    Toast.makeText(mContext, "설정에 실패했습니다. 제목과 내용을 입력하세요", Toast.LENGTH_SHORT).show();
                    MainActivity.start_fragment(0);
                } else {

                    //프레퍼런스불러오기
                    SharedPreferences prefs = getActivity().getSharedPreferences("pref", MODE_PRIVATE);

                    //문장 프레퍼런스 저장
                    SharedPreferences.Editor editor = prefs.edit();
                    //editor.putString("str_simple", et_msg_simple.getText().toString());

                    editor.putString("str_simple", et_msg_simple_f3.getText().toString());
                    editor.putString("name_event", et_event_name_f3.getText().toString());
                    editor.putInt("state_launcher", 2);
                    editor.commit();
                    MainActivity.start_fragment(0);
                    Toast.makeText(mContext, "문자설정 성공!~", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    private void initVal() {

    }

}


