package com.app.alien.returntalk;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Created by alien on 2018-01-02.
 */

public class PresentationActivity extends Activity {

    private TextView tv_pn;
    private int delayTime =3000;
    private String[] arr_pn ;
    static int i = 0;
    private Handler handler;
    private Handler dshandler;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_pt_pn);
        tv_pn = (TextView)findViewById(R.id.tv_pn);



        String myNumber = null;
        TelephonyManager mgr = (TelephonyManager) getSystemService(getBaseContext().TELEPHONY_SERVICE);
        try{

            myNumber = mgr.getLine1Number();
            myNumber = myNumber.replace("+82", "0");
            arr_pn = new String[4];
            arr_pn[0] = myNumber.substring(0,3);
            arr_pn[1] = myNumber.substring(3,7);
            arr_pn[2] = myNumber.substring(7);
            arr_pn[3] = "문자";
           Log.i("returntalk",myNumber.substring(0,3));
            Log.i("returntalk",myNumber.substring(3,7));
            Log.i("returntalk",myNumber.substring(7));


        }catch(Exception e){}
        tv_pn.setText("문자");

        recursive_handler();




    }

    public void recursive_handler(){

        dshandler = new Handler(Looper.getMainLooper());
        dshandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                tv_pn.setText(arr_pn[i++]);
                if(i==4){i=0;}
                recursive_handler();
            }
        }, delayTime);

    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
