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
        Log.i("returntalk", "\n------------------------------------------- \nintent.getAction : " + intent.getAction());
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Log.i("returntalk", "bootcompleted");
        }
        if (Intent.ACTION_SCREEN_ON == intent.getAction()) {
            Log.i("returntalk", "screen ON");
        }
        if (Intent.ACTION_SCREEN_OFF == intent.getAction()) {
            Log.i("returntalk", "screen OFF");
        }
        if ("android.provider.Telephony.SMS_RECEIVED".equals(intent.getAction())) {
            Log.i("returntalk", "sms get!");

            // SMS
            Bundle bundle = intent.getExtras();
            Object messages[] = (Object[]) bundle.get("pdus");
            SmsMessage smsMessage[] = new SmsMessage[messages.length];

            for (int i = 0; i < messages.length; i++) {
                smsMessage[i] = SmsMessage.createFromPdu((byte[]) messages[i]);
            }

            // SMS
            Date curDate = new Date(smsMessage[0].getTimestampMillis());
            Log.i("returntalk", curDate.toString());

            // SMS phone_num
            String sendNumber = smsMessage[0].getOriginatingAddress();


            // SMS
            String message = smsMessage[0].getMessageBody().toString();
            Log.i("returntalk", "number : " + sendNumber + ",msg : " + message);


            //preference load
            SharedPreferences prefs = context.getSharedPreferences("pref", MODE_PRIVATE);


            event_index = prefs.getInt("event_index", -1);

            Log.i("returntalk", "event_index : " + event_index);
            str_simple = prefs.getString("str_simple", "error");
            state_launcher = prefs.getBoolean("state_launcher", false);


            //자신의 전화번호와 비교하기
            String hardwareNumber = null; //기계의 전화번호
            TelephonyManager mgr = (TelephonyManager) mContext.getSystemService(mContext.TELEPHONY_SERVICE);
            try {
                hardwareNumber = mgr.getLine1Number();
                hardwareNumber = hardwareNumber.replace("+82", "0");
                Log.i("returntalk", "hardwareNumber : " + hardwareNumber);


            } catch (Exception e) {
            }


            if (state_launcher) {


                if (FirstFragment.ISDEBUG) { //디버깅_개발자용
                    if (message.contains("msg")) { //개발자가 나에게 보냈는경우
                        Log.i("returntalk", "디버깅)개발자가 나에게 보냈음 /sendNumber:" + sendNumber + " / hardwareNumber:" + hardwareNumber);

                    } else { // 일반인이 개발자에게 보낸경우
                        Reply myReply = makeReply(sendNumber, message);// 리플객체 생성,

                        //sms_list 에 빨간색으로 올리기
                        listup_sms(myReply.getNo_reply(), 1);

                        sendSMS(myReply.getNo_reply(), myReply.getPhone_num(), "msg :" + myReply.getMsg_server());//리플을 날리기
                        //sms_list 에 파란색으로 바꾸기
                        listup_sms(myReply.getNo_reply(), 2);

                        //프레퍼런스로 저장하기
                        savePrefReply(myReply);//리플 프레퍼런스 저장
                        Log.i("returntalk", "디버깅)남에게서옴 /sendNumber:" + sendNumber + " / hardwareNumber:" + hardwareNumber);
                    }

                } else { // 릴리즈_일반인

                    if (sendNumber.equals(hardwareNumber)) { //일반인이 자신에게 보낸경우
                        Log.i("returntalk", "릴리즈)일반인이 자신에게 보낸경우 /sendNumber:" + sendNumber + " / hardwareNumber:" + hardwareNumber);

                    } else {// 타인이 일반인에게 보낸경우
                        Reply myReply = makeReply(sendNumber, message);// 리플객체 생성,

                        //sms_list 에 빨간색으로 올리기
                        listup_sms(myReply.getNo_reply(), 1);
                        // sms보내기
                        sendSMS(myReply.getNo_reply(), myReply.getPhone_num(), myReply.getMsg_server());//리플을 날리기

                        //sms_list 에 파란색으로 바꾸기
                        listup_sms(myReply.getNo_reply(), 2);

                        //프레퍼런스로 저장하기
                        savePrefReply(myReply);//리플 프레퍼런스 저장
                        Log.i("returntalk", "릴리즈) 타인이 일반인에게 보낸경우/sendNumber:" + sendNumber + " / hardwareNumber:" + hardwareNumber);

                    }

                }


            }


        }
    }

    private void listup_sms(int no_reply, int isblue) {
        //Intent intent_blue = new Intent("android.intent.action.MAIN");
        Intent intent_blue = new Intent("fragment01");
        intent_blue.putExtra("no_reply", no_reply);
        if (isblue == 1) { //blue
            intent_blue.putExtra("state", 1); //답장함
        } else {

            intent_blue.putExtra("state", 2); //답장함
        }
        mContext.sendBroadcast(intent_blue);
        Log.i("returntalk", "listup_sms_blue: end /no_reply:" + no_reply + " /state: " + isblue);
    }


    private void savePrefReply(Reply myReply) {
        SharedPreferences prefs = mContext.getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        int no_event = myReply.getNo_event();

        //editor.putInt("no_event", myReply.getNo_event() );
        editor.putString("name_event_" + no_event + "_" + myReply.getNo_reply(), myReply.getName_event());
        editor.putLong("time_receive_" + no_event + "_" + myReply.getNo_reply(), myReply.getTime_receive());
        editor.putString("phone_num_" + no_event + "_" + myReply.getNo_reply(), myReply.getPhone_num());
        editor.putString("msg_client_" + no_event + "_" + myReply.getNo_reply(), myReply.getMsg_client());
        editor.putString("msg_server_" + no_event + "_" + myReply.getNo_reply(), myReply.getMsg_server());
        editor.putInt("state_" + no_event + "_" + myReply.getNo_reply(), myReply.getState());

        editor.putInt("no_reply_index", myReply.getNo_reply() + 1);

        Log.i("returntalk", "msg_client_" + no_event + "_" + myReply.getNo_reply() + " : " + myReply.getMsg_client());

        editor.commit();
    }

    private Reply makeReply(String phone_num, String msg_client) {
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
        int no_reply_index = prefs.getInt("no_reply_index", 0); // 처음일수 있음
        int no_event = prefs.getInt("event_index", -1); //처음일 수 없음
        String name_event = prefs.getString("name_event", null); //처음일 수 없음
        String str_simple = prefs.getString("str_simple", null);//처음일 수 없음


        Log.i("returntalk", "makeReply / event_index:" + no_event);
        Reply myReply = new Reply(no_reply_index, no_event, name_event, time_now.getTime(), phone_num, msg_client, str_simple, 1);

        //문장 프레퍼런스 저장
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("no_reply_index", ++no_reply_index);
        editor.commit();

        return myReply;
    }

    public void setMsg(String str) {
        str_simple = str;
    }

    public void sendSMS(int no_reply, String smsNumber, String smsText) {
        PendingIntent sentIntent = PendingIntent.getBroadcast(mContext, 0, new Intent("SMS_SENT_ACTION"), 0);
        PendingIntent deliveredIntent = PendingIntent.getBroadcast(mContext, 0, new Intent("SMS_DELIVERED_ACTION"), 0);

        SmsManager mSmsManager = SmsManager.getDefault();
        mSmsManager.sendTextMessage(smsNumber, null, smsText, sentIntent, deliveredIntent);
        Log.i("returntalk", "sendTextMessage complete.");


    }


}
