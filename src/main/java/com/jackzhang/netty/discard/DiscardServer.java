package com.jackzhang.netty.discard;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Discard服务（丢弃数据）
 * Created by Jack on 2017/4/21.
 */
public class DiscardServer {

    private int port;
    public DiscardServer(int port){
        this.port=port;
    }
    public void run() throws Exception {
        EventLoopGroup bossGroup=new NioEventLoopGroup();
        EventLoopGroup workerGroup=new NioEventLoopGroup();

        ServerBootstrap serverBoot=new ServerBootstrap();

        try{
            serverBoot.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class) //用来创建Channel实例（这里是NioServerSocketChannel）
                    .childHandler(new ChannelInitializer<SocketChannel>() { //配置ChannelHandler
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new DiscardServerHandler());
                        }
                    }).option(ChannelOption.SO_BACKLOG,128)
                    .childOption(ChannelOption.SO_KEEPALIVE,true);
            //绑定端口
            ChannelFuture channelFuture=serverBoot.bind(port).sync();
            //阻塞等待
            channelFuture.channel().closeFuture().sync();
        }finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }

    }

    public static void main(String[] args) throws Exception {
        int port=8899;
        new DiscardServer(port).run();
    }
}
