package com.app.alien.returntalk;


import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class Broadcast extends BroadcastReceiver {

    private Context mContext;
    private String str_simple;
    private boolean state_launcher;


    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        Log.i("returntalk","intent.getAction : "+intent.getAction());
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
            Log.i("returntalk","bootcompleted");
        }
        if (Intent.ACTION_SCREEN_ON == intent.getAction()) {
            Log.i("returntalk","screen ON");
        }
        if (Intent.ACTION_SCREEN_OFF == intent.getAction()) {
            Log.i("returntalk","screen OFF");
        }
        if ("android.provider.Telephony.SMS_RECEIVED".equals(intent.getAction())) {
            Log.i("returntalk","sms get!");

            // SMS
            Bundle bundle = intent.getExtras();
            Object messages[] = (Object[])bundle.get("pdus");
            SmsMessage smsMessage[] = new SmsMessage[messages.length];

            for(int i = 0; i < messages.length; i++) {
                smsMessage[i] = SmsMessage.createFromPdu((byte[])messages[i]);
            }

            // SMS
            Date curDate = new Date(smsMessage[0].getTimestampMillis());
            Log.i("returntalk", curDate.toString());

            // SMS phone_num
            String sendNumber = smsMessage[0].getOriginatingAddress();


            // SMS
            String message = smsMessage[0].getMessageBody().toString();
            Log.i("returntalk", "number : "+ sendNumber +",msg : " + message);


            //preference load
            SharedPreferences prefs = context.getSharedPreferences("pref", MODE_PRIVATE);
            str_simple = prefs.getString("str_simple", "error");
            state_launcher = prefs.getBoolean("state_launcher", false);


            //자신의 전화번호와 비교하기
            String hardwareNumber = null; //기계의 전화번호
            TelephonyManager mgr = (TelephonyManager) mContext.getSystemService(mContext.TELEPHONY_SERVICE);
            try{
                hardwareNumber = mgr.getLine1Number();
                hardwareNumber = hardwareNumber.replace("+82", "0");
                Log.i("returntalk","hardwareNumber : "+hardwareNumber);


            }catch(Exception e){}



            if (state_launcher){
                //if (sendNumber.equals(hardwareNumber) ){//전송된문자의 번호와 기계의 전화번호를 비교 ->나중에 주석풀고 릴리즈@
                if (message.contains("msg") ){// 디버깅용@ 나중엔 삭제해야함.
                    Log.i("returntalk","나에게 보냈음 /sendNumber:"+sendNumber+" / hardwareNumber:"+hardwareNumber);
                }else{
                    sendSMS(sendNumber,"msg :"+ str_simple );
                    Log.i("returntalk","남에게서옴 /sendNumber:"+sendNumber+" / hardwareNumber:"+hardwareNumber);
                }
            }


        }
    }
    public void setMsg(String str){
        str_simple = str;
    }
    public void sendSMS(String smsNumber, String smsText){
        PendingIntent sentIntent = PendingIntent.getBroadcast(mContext, 0, new Intent("SMS_SENT_ACTION"), 0);
        PendingIntent deliveredIntent = PendingIntent.getBroadcast(mContext, 0, new Intent("SMS_DELIVERED_ACTION"), 0);

//        registerReceiver(new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                switch(getResultCode()){
//                    case Activity.RESULT_OK:
//                        // 전송 성공
//                      Toast.makeText(mContext, "전송 완료", Toast.LENGTH_SHORT).show();
//                        break;
//                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
//                        // 전송 실패
//                      Toast.makeText(mContext, "전송 실패", Toast.LENGTH_SHORT).show();
//                        break;
//                    case SmsManager.RESULT_ERROR_NO_SERVICE:
//                        // 서비스 지역 아님
//                      Toast.makeText(mContext, "서비스 지역이 아닙니다", Toast.LENGTH_SHORT).show();
//                        break;
//                    case SmsManager.RESULT_ERROR_RADIO_OFF:
//                        // 무선 꺼짐
//                      Toast.makeText(mContext, "무선(Radio)가 꺼져있습니다", Toast.LENGTH_SHORT).show();
//                        break;
//                    case SmsManager.RESULT_ERROR_NULL_PDU:
//                        // PDU 실패
//                      Toast.makeText(mContext, "PDU Null", Toast.LENGTH_SHORT).show();
//                        break;
//                }
//             }
//        }, new IntentFilter("SMS_SENT_ACTION"));

//        registerReceiver(new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                switch (getResultCode()){
//                    case Activity.RESULT_OK:
//                        // 도착 완료
//                      Toast.makeText(mContext, "SMS 도착 완료", Toast.LENGTH_SHORT).show();
//                        break;
//                    case Activity.RESULT_CANCELED:
//                        // 도착 안됨
//                      Toast.makeText(mContext, "SMS 도착 실패", Toast.LENGTH_SHORT).show();
//                        break;
//                }
//            }
//        }, new IntentFilter("SMS_DELIVERED_ACTION"));

        SmsManager mSmsManager = SmsManager.getDefault();
        mSmsManager.sendTextMessage(smsNumber, null, smsText, sentIntent, deliveredIntent);
    }


}
