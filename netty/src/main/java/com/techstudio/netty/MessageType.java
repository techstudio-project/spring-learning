package com.techstudio.netty;

import java.util.HashMap;
import java.util.Map;

public class MessageType {

    public static final String TEXT = "text";
    public static final String IMAGE = "image";
    public static final String VOICE = "voice";
    public static final String VIDEO = "video";
    public static final String FILE = "file";
    public static final String ARTICLE = "article";
    public static final String CUSTOM = "custom";

    private static final Map<String, Class> MESSAGE_CLASS_MAP;

    static {
        MESSAGE_CLASS_MAP = new HashMap<>();
        MESSAGE_CLASS_MAP.put(MessageType.TEXT, TextMessage.class);
    }

    private MessageType() {
    }

    public static Message createInstance(String type) throws IllegalAccessException,
            InstantiationException {
        Class t = MESSAGE_CLASS_MAP.get(type);
        if (t != null) {
            try {
                Class.forName(t.getName());
                return (Message) t.newInstance();
            } catch (ClassNotFoundException e) {
                return new Message();
            }
        }
        return new Message();
    }
}
