package com.jackzhang.netty.heartbeat;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;


/**
 * 超时处理Handler(心跳)
 * Created by Jack on 2017/4/21.
 */
public class NettyIdleStateClientHandler extends ChannelHandlerAdapter {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
            throws Exception {

        if (evt instanceof IdleStateEvent) {

            IdleStateEvent event=(IdleStateEvent) evt;

            if (event.state().equals(IdleState.READER_IDLE)) {
                //心跳包内容
                String message=HeartBeat.HEARTBEAT_REQUEST_CLIENT;

                byte[] req = message.getBytes();
                ByteBuf heartBeatMsg = Unpooled.buffer();
                heartBeatMsg.writeBytes(req);

                ctx.writeAndFlush(heartBeatMsg);

                System.out.println("client READER_IDLE，sent heartbeat");

            } else if (event.state().equals(IdleState.WRITER_IDLE)) {
                System.out.println("client WRITER_IDLE");
            } else if (event.state().equals(IdleState.ALL_IDLE)) {
                System.out.println("client ALL_IDLE");
            }

            super.userEventTriggered(ctx, evt);
        }
    }
}  