package com.app.alien.component;

import java.util.Date;

/**
 * Created by alien on 2018-01-03.
 * @ class event
 - 고유순번(a) : int / no_event
 - 제목 : String / name_event
 - 간단/문자별 여부 : int / type // 0-simple , 1-multiple

 - 시작 날짜/시간 :date/ time_start
 - 종료 날짜/시간 :date/  time_end
 - 총 문자수 : int / cnt_reply
 - 중복을 뺀 문자수 = 참여자수 : int / cnt_participant
 - 그때의 메세지 : String / msg_event
 - 설정) 응답시간 : int / o_reply_term // default - 0
 - 설정) 모르는 전화번호만 응답하기 : boolean/  o_isavailable_stanger //default - off
 - 설정) 중복참여가능 : boolean/  o_isavailable_multiple //default- on
 - 저장여부 : boolean/  o_iskeep
 - 이미지주소 :String / image_url
 */

public class Event {
    private int no_event;
    private String name_event;
    private int type;
    private Date time_start;
    private Date time_end;
    private int cnt_reply;
    private int cnt_participant;
    private String msg_event;
    private int o_reply_term;
    private boolean o_isavailable_stranger;
    private boolean o_isavailable_multiple;
    private boolean o_iskeep;
    private String image_url;

    public Event(int no_event, String name_event, int type, Date time_start, Date time_end, int cnt_reply, int cnt_participant, String msg_event, int o_reply_term, boolean o_isavailable_stranger, boolean o_isavailable_multiple, boolean o_iskeep, String image_url) {
        this.no_event = no_event;
        this.name_event = name_event;
        this.type = type;
        this.time_start = time_start;
        this.time_end = time_end;
        this.cnt_reply = cnt_reply;
        this.cnt_participant = cnt_participant;
        this.msg_event = msg_event;
        this.o_reply_term = o_reply_term;
        this.o_isavailable_stranger = o_isavailable_stranger;
        this.o_isavailable_multiple = o_isavailable_multiple;
        this.o_iskeep = o_iskeep;
        this.image_url = image_url;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Date getTime_start() {
        return time_start;
    }

    public void setTime_start(Date time_start) {
        this.time_start = time_start;
    }

    public Date getTime_end() {
        return time_end;
    }

    public void setTime_end(Date time_end) {
        this.time_end = time_end;
    }

    public int getCnt_reply() {
        return cnt_reply;
    }

    public void setCnt_reply(int cnt_reply) {
        this.cnt_reply = cnt_reply;
    }

    public int getCnt_participant() {
        return cnt_participant;
    }

    public void setCnt_participant(int cnt_participant) {
        this.cnt_participant = cnt_participant;
    }

    public String getMsg_event() {
        return msg_event;
    }

    public void setMsg_event(String msg_event) {
        this.msg_event = msg_event;
    }

    public int getO_reply_term() {
        return o_reply_term;
    }

    public void setO_reply_term(int o_reply_term) {
        this.o_reply_term = o_reply_term;
    }

    public boolean isO_isavailable_stranger() {
        return o_isavailable_stranger;
    }

    public void setO_isavailable_stranger(boolean o_isavailable_stranger) {
        this.o_isavailable_stranger = o_isavailable_stranger;
    }

    public boolean isO_isavailable_multiple() {
        return o_isavailable_multiple;
    }

    public void setO_isavailable_multiple(boolean o_isavailable_multiple) {
        this.o_isavailable_multiple = o_isavailable_multiple;
    }

    public boolean isO_iskeep() {
        return o_iskeep;
    }

    public void setO_iskeep(boolean o_iskeep) {
        this.o_iskeep = o_iskeep;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
