package com.jackzhang.netty.heartbeat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;  
import io.netty.channel.ChannelInitializer;  
import io.netty.channel.ChannelOption;  
import io.netty.channel.EventLoopGroup;  
import io.netty.channel.nio.NioEventLoopGroup;  
import io.netty.channel.socket.SocketChannel;  
import io.netty.channel.socket.nio.NioSocketChannel;  
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;
/**
 * 心跳客户端
 * Created by Jack on 2017/4/21.
 */
public class NettyClientBootstrap {  
  
    /* 
     * 服务器端口号 
     */  
    private int port;  
  
    /* 
     * 服务器IP 
     */  
    private String host;  

    public NettyClientBootstrap(int port, String host)  
            throws InterruptedException {  
        this.port = port;  
        this.host = host;  
    }
    private void run() throws InterruptedException {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();  
            bootstrap.channel(NioSocketChannel.class);  
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);  
            bootstrap.group(eventLoopGroup);  
            bootstrap.remoteAddress(host, port);  
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {  
                @Override  
                protected void initChannel(SocketChannel socketChannel)  
                        throws Exception {  
                      
                    //超时处理：参数分别为读超时时间、写超时时间、读和写都超时时间、时间单位  
                    socketChannel.pipeline().addLast(new IdleStateHandler(3, 8, 0, TimeUnit.SECONDS));
                    socketChannel.pipeline().addLast(new NettyIdleStateClientHandler());
                    socketChannel.pipeline().addLast(new NettyClientHandler());  
                }  
            });  
            ChannelFuture future = bootstrap.connect(host, port).sync();  
            if (future.isSuccess()) {  
                System.out.println("----------------connect server success----------------");
            }  
            future.channel().closeFuture().sync();  
        } finally {  
            eventLoopGroup.shutdownGracefully();  
        }  
    }  
  
    public static void main(String[] args) throws InterruptedException {  
        new NettyClientBootstrap(9989,
                "127.0.0.1").run();
  
    }  
}  
