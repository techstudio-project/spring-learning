package com.techstudio.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

/**
 * @author lj
 * @since 2020/4/6
 */
public class WebSocketChannelInboundHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    private static final InternalLogger logger = InternalLoggerFactory.getInstance(WebSocketChannelInboundHandler.class);

    @Override
    public void channelRead0(ChannelHandlerContext ctx, WebSocketFrame msg) {
        logger.info("[WebSocketChannelInboundHandler]:{}", this.toString());
        handleWebSocketFrame(ctx, msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        logger.info("[WebSocketChannelInboundHandler]:{}", this.toString());
        logger.info(formatLogInfo(ctx, "新建连接", ctx.channel().remoteAddress().toString()));
    }

    private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {

        OnlineChannels.refreshLastActiveTime(ctx.channel());

        if (frame instanceof PingWebSocketFrame) {
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        // 文本消息
        if (frame instanceof TextWebSocketFrame) {
            processTextWebSocketFrame(ctx, (TextWebSocketFrame) frame);
        }

    }

    private void processTextWebSocketFrame(ChannelHandlerContext ctx, TextWebSocketFrame frame) {
        String msg = frame.text();
        logger.info(formatLogInfo(ctx, "收到", msg));
        if (msg == null) {
            return;
        }
        ctx.channel().writeAndFlush(new TextWebSocketFrame("服务端收到你发来的消息"));
    }


    /**
     * 格式化log输出
     */
    private static String formatLogInfo(ChannelHandlerContext ctx, String type, String msg) {
        StringBuilder sb = new StringBuilder();
        sb.append("[id: 0x").append(ctx.channel().id().asShortText()).append("] ");
        if (type != null && !"".equals(type)) {
            sb.append(type).append(": ");
        }
        sb.append(msg);
        return sb.toString();
    }


}
