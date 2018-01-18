package com.app.alien.component;

import java.util.Date;

/**
 * Created by alien on 2018-01-04.
 */

public class reply {
    private int no_reply;
    private int no_event;
    private String name_event; //필요는 없음.
    private Date time_receive;
    private String phone_num;
    private String msg_client;
    private String msg_server; //필요는 없음
    private String state; //0- 시작전 디폴트 , 1- 문자옴, 2 답장함,

    public reply(int no_reply, int no_event, String name_event, Date time_receive, String phone_num, String msg_client, String msg_server, String state) {
        this.no_reply = no_reply;
        this.no_event = no_event;
        this.name_event = name_event;
        this.time_receive = time_receive;
        this.phone_num = phone_num;
        this.msg_client = msg_client;
        this.msg_server = msg_server;
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

    public Date getTime_receive() {
        return time_receive;
    }

    public void setTime_receive(Date time_receive) {
        this.time_receive = time_receive;
    }

    public String getPhone_num() {
        return phone_num;
    }

    public void setPhone_num(String phone_num) {
        this.phone_num = phone_num;
    }

    public String getMsg_client() {
        return msg_client;
    }

    public void setMsg_client(String msg_client) {
        this.msg_client = msg_client;
    }

    public String getMsg_server() {
        return msg_server;
    }

    public void setMsg_server(String msg_server) {
        this.msg_server = msg_server;
    }
}




