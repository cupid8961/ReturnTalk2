package com.app.alien.returntalk;

import java.util.Date;

/**
 * Created by alien on 2018-01-04.
 */

public class reply {
    private int no_reply;
    private int no_event;
    private String name_event;
    private Date time_send;
    private String phone_num;
    private String msg_reply;
    private String msg_event;

    public reply(int no_reply, int no_event, String name_event, Date time_send, String phone_num, String msg_reply, String msg_event) {
        this.no_reply = no_reply;
        this.no_event = no_event;
        this.name_event = name_event;
        this.time_send = time_send;
        this.phone_num = phone_num;
        this.msg_reply = msg_reply;
        this.msg_event = msg_event;
    }

    public int getNo_reply() {
        return no_reply;
    }

    public void setNo_reply(int no_reply) {
        this.no_reply = no_reply;
    }

    public int getNo_event() {
        return no_event;
    }

    public void setNo_event(int no_event) {
        this.no_event = no_event;
    }

    public String getName_event() {
        return name_event;
    }

    public void setName_event(String name_event) {
        this.name_event = name_event;
    }

    public Date getTime_send() {
        return time_send;
    }

    public void setTime_send(Date time_send) {
        this.time_send = time_send;
    }

    public String getPhone_num() {
        return phone_num;
    }

    public void setPhone_num(String phone_num) {
        this.phone_num = phone_num;
    }

    public String getMsg_reply() {
        return msg_reply;
    }

    public void setMsg_reply(String msg_reply) {
        this.msg_reply = msg_reply;
    }

    public String getMsg_event() {
        return msg_event;
    }

    public void setMsg_event(String msg_event) {
        this.msg_event = msg_event;
    }
}




