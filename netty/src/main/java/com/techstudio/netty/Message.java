package com.techstudio.netty;

import java.util.Map;

/**
 * @author lj
 * @since 2020/4/7
 */
public class Message {

    private Integer msgcode;

    private String msgstatus;

    private String msgtype;

    private String fromuser;

    private String touser;

    private String toroom;

    private String action;

    private Object data;

    private Map<Object, Object> extra;

    private String sendType;

    public Integer getMsgcode() {
        return msgcode;
    }

    public void setMsgcode(Integer msgcode) {
        this.msgcode = msgcode;
    }

    public String getMsgstatus() {
        return msgstatus;
    }

    public void setMsgstatus(String msgstatus) {
        this.msgstatus = msgstatus;
    }

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public String getFromuser() {
        return fromuser;
    }

    public void setFromuser(String fromuser) {
        this.fromuser = fromuser;
    }

    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getToroom() {
        return toroom;
    }

    public void setToroom(String toroom) {
        this.toroom = toroom;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Map<Object, Object> getExtra() {
        return extra;
    }

    public void setExtra(Map<Object, Object> extra) {
        this.extra = extra;
    }

    public String getSendType() {
        return sendType;
    }

    public void setSendType(String sendType) {
        this.sendType = sendType;
    }
}
