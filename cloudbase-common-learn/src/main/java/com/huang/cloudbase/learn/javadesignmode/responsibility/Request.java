package com.huang.cloudbase.learn.javadesignmode.responsibility;

/**
 * @author huangjunbiao
 */
public class Request {

    private int requestLevel;

    public Request(int requestLevel) {
        this.requestLevel = requestLevel;
    }

    public int getRequestLevel() {
        return requestLevel;
    }

    public void setRequestLevel(int requestLevel) {
        this.requestLevel = requestLevel;
    }
}
