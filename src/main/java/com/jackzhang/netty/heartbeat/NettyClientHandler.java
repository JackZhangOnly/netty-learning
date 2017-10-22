package com.jackzhang.netty.heartbeat;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;  
import io.netty.channel.ChannelHandlerAdapter;  
import io.netty.channel.ChannelHandlerContext;

import java.io.UnsupportedEncodingException;

/**
 * 消息处理Handler
 * Created by Jack on 2017/4/21.
 */
public class NettyClientHandler extends ChannelHandlerAdapter  {  
      
    @Override  
    public void channelRead(ChannelHandlerContext ctx, Object msg)  
            throws Exception {  
      
        String result = getMessage((ByteBuf) msg);
        System.out.println("client receive msg:" + result);
        if (HeartBeat.HEARTBEAT_RESPONSE_SERVER.equals(result)){
            System.out.println("client receive heartbeat msg!!!");
        }
    }
    /**
     * 从ByteBuf中获取信息 使用UTF-8编码返回
     * @param buf
     * @return
     */
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