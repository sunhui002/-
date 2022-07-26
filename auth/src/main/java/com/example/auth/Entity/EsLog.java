package com.example.auth.Entity;

public class EsLog {

    public String logname;

    public int loglevelint;

    public String loglevel;

    public String time;

    public String msg;

    public EsLog() {
    }

    public String getLogname() {
        return logname;
    }

    public void setLogname(String logname) {
        this.logname = logname;
    }

    public int getLoglevelint() {
        return loglevelint;
    }

    public void setLoglevelint(int loglevelint) {
        this.loglevelint = loglevelint;
    }

    public String getLoglevel() {
        return loglevel;
    }

    public void setLoglevel(String loglevel) {
        this.loglevel = loglevel;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
