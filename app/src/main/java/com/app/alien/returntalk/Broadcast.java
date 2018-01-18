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

import com.app.alien.component.Reply;

import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class Broadcast extends BroadcastReceiver {

    private Context mContext;
    private String str_simple;
    private boolean state_launcher;
    private int event_index;

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        Log.i("returntalk","\n------------------------------------------- \nintent.getAction : "+intent.getAction());
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


            event_index = prefs.getInt("event_index",0);

            Log.i("returntalk","event_index : "+event_index);
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
                    Reply myReply = makeReply(sendNumber,str_simple);// 리플객체 생성,

                    //sms_list 에 빨간색으로 올리기
                    listup_sms_red(myReply.getNo_reply());

                    // sms보내기
                    sendSMS(myReply.getNo_reply(),myReply.getPhone_num(),"msg :"+myReply.getMsg_server() );//리플을 날리기

                    //sms_list 에 파란색으로 바꾸기
                    listup_sms_blue(myReply.getNo_reply());

                    //프레퍼런스로 저장하기
                    savePrefReply(myReply);//리플 프레퍼런스 저장
                    Log.i("returntalk","남에게서옴 /sendNumber:"+sendNumber+" / hardwareNumber:"+hardwareNumber);
                }
            }


        }
    }

    private void listup_sms_blue(int no_reply) {
        Log.i("returntalk","listup_sms_blue: start");
        Intent intent_blue = new Intent("fragment01");
        intent_blue.putExtra("no_reply",no_reply);
        intent_blue.putExtra("state",2); //답장함
        mContext.sendBroadcast(intent_blue);
        Log.i("returntalk","listup_sms_blue: end /"+no_reply+" / "+2);
    }

    private void listup_sms_red(int no_reply) {
        Log.i("returntalk","listup_sms_red: start");
        Intent intent_red = new Intent("fragment01");
        intent_red.putExtra("no_reply",no_reply);
        intent_red.putExtra("state",1); //문자옴
        mContext.sendBroadcast(intent_red);
        Log.i("returntalk","listup_sms_red: end /"+no_reply+" / "+1);
    }


    private void savePrefReply(Reply myReply) {
        SharedPreferences prefs = mContext.getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        int no_event = myReply.getNo_event();

        editor.putInt("no_reply", myReply.getNo_reply() );
        editor.putInt("no_event", myReply.getNo_event() );
        editor.putString("name_event_"+no_event, myReply.getName_event() );
        editor.putLong("time_receive_"+no_event, myReply.getTime_receive() );
        editor.putString("phone_num_"+no_event, myReply.getPhone_num());
        editor.putString("msg_client_"+no_event,myReply.getMsg_client());
        editor.putString("msg_server_"+no_event,myReply.getMsg_server());
        editor.putInt("state_"+no_event, myReply.getState());
        editor.commit();
    }
    private Reply makeReply(String phone_num,String msg_client) {
/*
    public Reply(int no_reply, int no_event, String name_event, Date time_receive, String phone_num, String msg_client, String msg_server, String state) {
        this.no_reply = no_reply;
        this.no_event = no_event;
        this.name_event = name_event;
        this.time_receive = time_receive;
        this.phone_num = phone_num;
        this.msg_client = msg_client;
        this.msg_server = msg_server;
        this.state = state;
    }
 */

        //현재시간 가져오기
        Date time_now = new Date();

        //프레퍼런스불러오기
        SharedPreferences prefs = mContext.getSharedPreferences("pref", MODE_PRIVATE);
        int no_reply = prefs.getInt("no_reply", 0); // 처음일수 있음
        int no_event = prefs.getInt("event_index", -1); //처음일 수 없음
        String name_event = prefs.getString("name_event", null); //처음일 수 없음
        String str_simple = prefs.getString("str_simple",null);//처음일 수 없음


        Reply myReply = new Reply(no_reply,no_event,name_event,time_now.getTime(),phone_num,msg_client,str_simple,1);

        //문장 프레퍼런스 저장
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("no_reply", ++no_reply);
        editor.commit();

        return myReply;
    }

    public void setMsg(String str){
        str_simple = str;
    }

    public void sendSMS(int no_reply , String smsNumber, String smsText){
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
