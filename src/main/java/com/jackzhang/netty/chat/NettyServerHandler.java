package com.jackzhang.netty.chat;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Constant;

/**
 * Created by Jack on 2017/4/21.
 */
public class NettyServerHandler extends ChannelHandlerAdapter{

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf=(ByteBuf)msg;

        //读进字节数组
        byte[] bytes=new byte[buf.readableBytes()];
        buf.readBytes(bytes);

        String receivedMsg=new String(bytes, "UTF-8");
        System.out.println("server receive msg:"+receivedMsg);

        //发消消息给客户端
        byte[] sendMsg="potato".getBytes();
        ByteBuf sendBuf= Unpooled.buffer();
        sendBuf.writeBytes(sendMsg);

        ctx.writeAndFlush(sendBuf);
    }
}
