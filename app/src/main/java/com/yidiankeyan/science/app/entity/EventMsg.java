package com.yidiankeyan.science.app.entity;

/**
 * Created by nby on 2016/7/26.
 */
public class EventMsg {
    private int what;
    private Object body;
    private int arg1;
    private int arg2;

    public int getArg1() {
        return arg1;
    }

    public void setArg1(int arg1) {
        this.arg1 = arg1;
    }

    public int getArg2() {
        return arg2;
    }

    public void setArg2(int arg2) {
        this.arg2 = arg2;
    }

    public EventMsg(int what) {
        this.what = what;
    }

    public static EventMsg obtain(int what) {
        return new EventMsg(what);
    }

    public int getWhat() {
        return what;
    }

    public void setWhat(int what) {
        this.what = what;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
}
