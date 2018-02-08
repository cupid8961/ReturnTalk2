package com.app.alien.returntalk;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    static ViewPager vp;
    Button btn_first;
    Button btn_second;
    Context mContext;
    FirstFragment fragment01;
    SecondFragment fragment02;
    ThirdFragment fragment03;
    View v01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i("returntalk", "MainActivity / onCreate");
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);
        mContext = getBaseContext();


        vp = (ViewPager) findViewById(R.id.vp);
        btn_first = (Button) findViewById(R.id.btn_first);
        btn_second = (Button) findViewById(R.id.btn_second);


        vp.setAdapter(new pagerAdapter(getSupportFragmentManager()));
        vp.setCurrentItem(0);


        btn_first.setOnClickListener(movePageListener);
        btn_first.setTag(0);

        if (FirstFragment.ISDEBUG) {
            btn_second.setOnClickListener(movePageListener);
            btn_second.setTag(1);
        } else {
            btn_second.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Perform action on click

                    Toast.makeText(mContext, "리턴함 기능은 구현중입니다..!", Toast.LENGTH_SHORT).show();
                }
            });

        }


    }


    View.OnClickListener movePageListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int tag = (int) v.getTag();
            vp.setCurrentItem(tag);
            changeTextColor(tag);
        }
    };

    private void changeTextColor(int tag) {
        if (tag == 1) {

            btn_first.setTextColor(Color.parseColor(FirstFragment.STRCOLOR_BLUE));
            btn_second.setTextColor(Color.parseColor(FirstFragment.STRCOLOR_GRAY));

        } else {
            btn_first.setTextColor(Color.parseColor(FirstFragment.STRCOLOR_BLUE));
            btn_second.setTextColor(Color.parseColor(FirstFragment.STRCOLOR_GRAY));

        }

    }

    private class pagerAdapter extends FragmentStatePagerAdapter {
        public pagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            switch (position) {
                case 0: {
                    Log.i("returntalk", "MainActivity / pagerAdapter / case : 0");
                    fragment01 = new FirstFragment();

                    return fragment01;
                }
                case 1: {
                    Log.i("returntalk", "MainActivity / pagerAdapter / case : 1");
                    fragment02 = new SecondFragment();
                    return fragment02;

                }

                case 2:
                    Log.i("returntalk", "MainActivity / pagerAdapter / case : 2");
                    fragment03 = new ThirdFragment();
                    return fragment03;

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }


    @Override
    protected void onDestroy() {

        Log.i("returntalk", "MainActivity / onDestroy");
        super.onDestroy();

    }

    @Override
    protected void onPause() {
        Log.i("returntalk", "MainActivity / onPause");
        super.onPause();

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Log.i("returntalk", "MainActivity / onBackPressed");

        // 현재 동작중인지 확인
        SharedPreferences prefs = mContext.getSharedPreferences("pref", MODE_PRIVATE);
        int is_tv_on = prefs.getInt("state_launcher", 2);

        if (is_tv_on == 0) show_exit_dialog(); //동작중일때만 다이얼로그 생성
        else { // 아닐때는 걍꺼짐.
            super.onBackPressed();
        }

    }

    private void show_exit_dialog() {
        final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();

        alertDialog.setTitle("Dialog Button");
        // 제목셋팅
        alertDialog.setTitle("프로그램 종료");
        alertDialog.setMessage("프로그램을 종료하시려면 아래 버튼을 누르세요.");

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "자동응답을 취소하고 종료", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                fragment01.click_tv_off();
                fragment01.change_launcher_state();


                moveTaskToBack(true);
                finish();
                android.os.Process.killProcess(android.os.Process.myPid());


            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "자동응답을 유지한채 종료", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.MAIN");
                intent.addCategory("android.intent.category.HOME");
                intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
                        | Intent.FLAG_ACTIVITY_FORWARD_RESULT
                        | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP
                        | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                startActivity(intent);


            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "돌아가기", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                alertDialog.dismiss();
            }
        });

        // 다이얼로그 보여주기
        if (!MainActivity.this.isFinishing()) {
            alertDialog.show();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    public static void start_fragment(int index){
        vp.setCurrentItem(index);
    }
}
