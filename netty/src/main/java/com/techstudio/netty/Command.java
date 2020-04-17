package com.techstudio.netty;

/**
 * @author lj
 * @since 2020/4/7
 */
public enum Command {
    READY,
    REQUEST,
    RESPONSE,
    PING,
    PONG,
    ACK,
    BROADCAST
}
