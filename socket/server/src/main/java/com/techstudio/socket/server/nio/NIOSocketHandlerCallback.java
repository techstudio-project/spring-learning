package com.techstudio.socket.server.nio;


/**
 * @author lj
 * @since 2020/4/1
 */
public interface NIOSocketHandlerCallback {

    /**
     * socket 关闭时的回调函数
     *
     * @param socketHandler SocketHandler
     */
    void onClosed(NIOSocketHandlerNew socketHandler);

    void onMessageReceive(NIOSocketHandlerNew socketHandler, String msg);

}
