package com.techstudio.netty;

import java.util.Map;


public interface MessageHandler {

	boolean requireAuthentication(String group, String id, Map params);

    Message messageReceived(WebSocketSession session, Message message);
}
