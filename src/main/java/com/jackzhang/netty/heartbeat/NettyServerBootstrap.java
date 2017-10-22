package com.jackzhang.netty.heartbeat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;  
import io.netty.channel.ChannelInitializer;  
import io.netty.channel.ChannelOption;  
import io.netty.channel.ChannelPipeline;  
import io.netty.channel.EventLoopGroup;  
import io.netty.channel.nio.NioEventLoopGroup;  
import io.netty.channel.socket.SocketChannel;  
import io.netty.channel.socket.nio.NioServerSocketChannel;  
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;  
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * Created by Jack on 2017/4/21.
 */
public class NettyServerBootstrap {
    /*
     * 服务器端口号
     */
    private int port;  
  
    public NettyServerBootstrap(int port) {  
        this.port = port;  
    }
    private void run() {
        EventLoopGroup boss = new NioEventLoopGroup();  
        EventLoopGroup worker = new NioEventLoopGroup();  
  
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss, worker);  
            bootstrap.channel(NioServerSocketChannel.class);  
            bootstrap.option(ChannelOption.SO_BACKLOG, 1024);  
            bootstrap.option(ChannelOption.TCP_NODELAY, true);  
            bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);  
            bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {  
                @Override  
                protected void initChannel(SocketChannel socketChannel)  
                        throws Exception {  
                    ChannelPipeline p = socketChannel.pipeline();
                    // 超时处理：参数分别为读超时时间、写超时时间、读和写都超时时间、时间单位  
                    p.addLast(new IdleStateHandler(15, 30, 30, TimeUnit.SECONDS));
                    p.addLast(new NettyIdleStateServerHandler());
                    p.addLast(new NettyServerHandler());  
                }  
            });  
            ChannelFuture f = bootstrap.bind(port).sync();  
            if (f.isSuccess()) {  
                System.out.println("启动Netty服务成功，端口号：" + this.port);
            }  
            // 关闭连接  
            f.channel().closeFuture().sync();  
  
        } catch (Exception e) {
            System.out.println("启动Netty服务异常，异常信息：" + e.getMessage());
            e.printStackTrace();
        } finally {  
            boss.shutdownGracefully();  
            worker.shutdownGracefully();  
        }  
    }
    public static void main(String[] args) {
        new NettyServerBootstrap(9989).run();
    }
  
}  