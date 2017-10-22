package com.jackzhang.netty.heartbeat;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;  
import io.netty.handler.timeout.IdleState;  
import io.netty.handler.timeout.IdleStateEvent;

/**
 * Created by Jack on 2017/4/21.
 */
public class NettyIdleStateServerHandler extends ChannelHandlerAdapter {

    @Override  
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt)  
            throws Exception {  
  
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event=(IdleStateEvent) evt;
            if (event.state().equals(IdleState.READER_IDLE)) {  
                System.out.println("server " + ctx.channel().id() + " READER_IDLE");
            } else if (event.state().equals(IdleState.WRITER_IDLE)) {  
                System.out.println("server " + ctx.channel().id() + " WRITER_IDLE");
            } else if (event.state().equals(IdleState.ALL_IDLE)) {
                System.out.println("server " + ctx.channel().id()
                        + " ALL_IDLE");
            }
            super.userEventTriggered(ctx, evt);  
        }  
    }  
  
}  