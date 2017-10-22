package com.jackzhang.netty.chat;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.io.UnsupportedEncodingException;

/**
 * Created by Jack on 2017/4/21.
 */
public class NettyClientHandler extends ChannelHandlerAdapter {
  
    private ByteBuf firstMessage;
      
    @Override  
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
          
        byte[] data = "give me a potato".getBytes();
          
        firstMessage= Unpooled.buffer();
        firstMessage.writeBytes(data);  
          
        ctx.writeAndFlush(firstMessage);  
    }  
       
    @Override  
    public void channelRead(ChannelHandlerContext ctx, Object msg)  
            throws Exception {  
          
        ByteBuf buf = (ByteBuf) msg;  

        String rev = getMessage(buf);
        System.out.println("client receive data from server:" + rev);
    }  
       
    private String getMessage(ByteBuf buf) {
        byte[] con = new byte[buf.readableBytes()];  
        buf.readBytes(con);  
        try {  
            return new String(con, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();  
            return null;  
        }  
    }  
}  