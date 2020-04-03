package com.techstudio.socket.tcp;

/**
 * @author lj
 * @since 2020/4/1
 */
public interface SocketHandlerCallback {

    /**
     * socket 关闭时的回调函数
     *
     * @param socketHandler SocketHandler
     */
    void onClosed(SocketHandler socketHandler);

    void onMessageReceive(SocketHandler socketHandler, String msg);

}
