package com.techstudio.netty;

import com.techstudio.netty.util.MD5Utils;
import io.netty.channel.Channel;

import java.util.HashMap;

public class WebSocketSession {

    private static final int INACTIVE_TIME = 6000 * 20;//不活动连接20分钟

    private String sessionId;

    private String id;

    private String group;

    /**
     * 准备删除该session的计数，检查指定次数连接都是关闭状态，则删除
     */
    private int readyRemoveCount;

    /**
     * 连接最后的活动时间
     */
    private long lastActiveTime;

    private Channel channel;

    /**
     * 用来存储当前连接活动时可用的session数据
     */
    private HashMap<String, Object> sessionData = new HashMap<>();

    public WebSocketSession(Channel c) {
        this.channel = c;
        this.sessionId = MD5Utils.encrypt(c.id().asLongText());
        this.lastActiveTime = System.currentTimeMillis();
    }

    public WebSocketSession(Channel c, String sessionId) {
        this.channel = c;
        this.sessionId = sessionId;
        this.lastActiveTime = System.currentTimeMillis();
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getGroup() {
        return group;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public long getLastActiveTime() {
        return lastActiveTime;
    }

    public void setLastActiveTime(long lastActiveTime) {
        this.lastActiveTime = lastActiveTime;
    }

    public void close() {
        if (this.channel != null && this.channel.isOpen()) {
            this.channel.close();
        }
        this.channel = null;
    }

    public boolean isActive() {
        if (this.channel != null) {
            return this.channel.isActive();
        } else {
            return false;
        }
    }

    public boolean isOpen() {
        if (this.channel != null) {
            return this.channel.isOpen();
        } else {
            return false;
        }
    }

    public boolean isWritable() {
        if (this.channel != null) {
            return this.channel.isWritable();
        } else {
            return false;
        }
    }

    public boolean isRegistered() {
        if (this.channel != null) {
            return this.channel.isRegistered();
        } else {
            return false;
        }
    }

    /**
     * 从WebSocketSession取出数据
     *
     * @param key
     * @return
     */
    public Object get(String key) {
        return sessionData.get(key);
    }

    /**
     * 向WebSocketSession存入数据（是线程安全的）
     *
     * @param key
     * @param value
     * @return
     */
    public Object set(String key, Object value) {
        return sessionData.put(key, value);
    }

    /**
     * 从WebSocketSession删除指定数据（是线程安全的）
     *
     * @param key
     * @return
     */
    public Object remove(String key) {
        return sessionData.remove(key);
    }

    /**
     * WebSocketSession是否包含有指定key
     *
     * @param key
     * @return
     */
    public boolean contains(String key) {
        return sessionData.containsKey(key);
    }

    /**
     * WebSocketSession是否包含指定对象
     *
     * @param value
     * @return
     */
    public boolean containsValue(Object value) {
        return sessionData.containsValue(value);
    }


    int getReadyRemoveCount() {
        return this.readyRemoveCount;
    }

    void setReadyRemoveCount(int count) {
        this.readyRemoveCount = count;
    }

    void addReadyRemoveCount() {
        ++this.readyRemoveCount;
    }

    void resetReadyRemoveCount() {
        this.readyRemoveCount = 0;
    }

    /**
     * 更新活动时间
     */
    public void refreshActiveTime() {
        this.lastActiveTime = System.currentTimeMillis();
    }

    /**
     * 检测连接是否长时间不活动
     *
     * @return
     */
    public boolean isInactive() {
        return System.currentTimeMillis() - this.lastActiveTime > INACTIVE_TIME;
    }

}
