package com.techstudio.netty;

import io.netty.channel.Channel;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SessionManager {

    private static final InternalLogger logger = InternalLoggerFactory.getInstance(SessionManager.class);
    private static Map<String,WebSocketSession> sessions = new HashMap<>(512);
    private SessionManager() {}

    public static WebSocketSession createSession(Channel channel) {
        WebSocketSession newSession = new WebSocketSession(channel);
        sessions.put(newSession.getSessionId(), newSession);
        return newSession;
    }

    public static WebSocketSession createSession(Channel channel, String group, String id, String oldSessionId) {
        if(oldSessionId != null && !"".equals(oldSessionId) && sessions.containsKey(oldSessionId)) {
            WebSocketSession oldSession = sessions.get(oldSessionId);
            oldSession.setChannel(channel);
            return oldSession;
        }
        else {
            WebSocketSession newSession = new WebSocketSession(channel);
            newSession.setGroup(group);
            newSession.setId(id);
            sessions.put(newSession.getSessionId(), newSession);
            return newSession;
        }
    }

    public static WebSocketSession createSession(WebSocketSession now,String group,String id, String oldSessionId) {
        if(oldSessionId != null && !"".equals(oldSessionId) && sessions.containsKey(oldSessionId)) {
            WebSocketSession oldSession = sessions.get(oldSessionId);
            oldSession.setChannel(now.getChannel());
            now.setChannel(null);
            sessions.remove(now.getSessionId());
            return oldSession;
        }
        else {
            now.setGroup(group);
            now.setId(id);
            return now;
        }
    }


    public static WebSocketSession getSession(String sessionId) {
        return sessions.get(sessionId);
    }

    public static boolean contains(String sessionId) {
        return sessions.containsKey(sessionId);
    }

    public static boolean contains(WebSocketSession session) {
        return sessions.containsValue(session);
    }

    public static WebSocketSession remove(String sessionId) {
        return sessions.remove(sessionId);
    }

    public static WebSocketSession remove(WebSocketSession webSocketSession) {
        if(webSocketSession != null) {
            return sessions.remove(webSocketSession.getSessionId());
        }
        else {
            return null;
        }
    }

    public static void clear() {
        sessions.clear();
    }

    public static void checkInactiveSession() {
        Iterator<Map.Entry<String,WebSocketSession>> iterator = sessions.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String,WebSocketSession> entry = iterator.next();
            WebSocketSession checkSession = entry.getValue();
            if((checkSession.getChannel() == null && checkSession.getId() == null && checkSession.getGroup() == null)
                    || checkSession.getReadyRemoveCount() >= 3) {
                String sessionId = checkSession.getSessionId();
                iterator.remove();
                logger.info(String.format("[sessionid:%s]  removed",sessionId));
                continue;
            }
            //关闭长时间不活动的（连ping pong消息都没有的）死链接，
            if(checkSession.isInactive()) {
                checkSession.close();
                checkSession.addReadyRemoveCount();
                continue;
            }
            //对已关闭的连接计数3次，准备删除，如果在15分钟内重新连接，则保留session，否则清楚session
            if(!checkSession.isOpen()) {
                checkSession.addReadyRemoveCount();
            }
            else {
                checkSession.resetReadyRemoveCount();
            }
        }
    }

    public static String getInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("sessions size:").append(sessions.size()).append("\r\n");
        for(Map.Entry<String, WebSocketSession> entry : sessions.entrySet()) {
            sb.append(entry.getKey()).append(":");
            WebSocketSession session = entry.getValue();
            Channel channel = session.getChannel();
            sb.append("[")
                    .append("id:").append(session.getId())
                    .append(",group:").append(session.getGroup());
            if (channel != null && channel.attr(OnlineChannels.ATTR_KEY_PLATFORM) != null) {
                sb.append(",platform:").append(channel.attr(OnlineChannels.ATTR_KEY_PLATFORM).get());
            }
            sb.append(",lastTime:").append(new Date(session.getLastActiveTime()));
            if(channel != null) {
                sb.append(",channelid:").append(channel.id())
                        .append(",active:").append(channel.isActive())
                        .append(",open:").append(channel.isOpen());
            }
            sb.append("]\r\n");
        }
        return sb.toString();
    }

}

