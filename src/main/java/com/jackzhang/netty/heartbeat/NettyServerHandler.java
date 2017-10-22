package com.jackzhang.netty.heartbeat;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;  
import io.netty.channel.socket.SocketChannel;

import java.io.UnsupportedEncodingException;

/**
 * Created by Jack on 2017/4/21.
 */
public class NettyServerHandler extends ChannelHandlerAdapter {  

    @Override  
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {  
        super.channelInactive(ctx);  
    }  
  
    @Override  
    public void channelRead(ChannelHandlerContext ctx, Object msg) {  
          
        ByteBuf buf = (ByteBuf) msg;
        String recieved = getMessage(buf);

        System.out.println("server receive msg：" + recieved);

        String replyMsg="normal server reply";
        if (HeartBeat.HEARTBEAT_REQUEST_CLIENT.equals(recieved)){
            System.out.println("server receive heartbeat msg!!!" );
            //服务端回复心跳
            replyMsg=HeartBeat.HEARTBEAT_RESPONSE_SERVER;
        }
        try {

            byte[] mes = replyMsg.getBytes();
            ByteBuf replyBufMsg= Unpooled.buffer();
            replyBufMsg.writeBytes(mes);

            ctx.writeAndFlush(replyBufMsg);
              
        } catch (Exception e) {  
            e.printStackTrace();
            return;
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

      
    @Override  
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)  
            throws Exception {  
    }  
}  