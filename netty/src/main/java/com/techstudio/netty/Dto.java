package com.techstudio.netty;

/**
 * @author lj
 * @since 2020/4/7
 */
public class Dto {

    /**
     * gwpus服务器指令 目前只用ready 和request
     */
    private String command;
    /**
     * 时间戳
     */
    private long timestamp;
    /**
     * 客户端发送的数据
     */
    private Object message;

    /**
     * 是否已通知
     */
    private Boolean notified;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public Boolean getNotified() {
        return notified;
    }

    public void setNotified(Boolean notified) {
        this.notified = notified;
    }
}
