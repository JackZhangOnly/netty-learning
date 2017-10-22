package com.jackzhang.netty.chat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Created by Jack on 2017/4/21.
 */
public class NettyServerBootstrap {
    private int port;
    public NettyServerBootstrap(int port){
        this.port=port;
    }

    public void run(){
        EventLoopGroup boss=new NioEventLoopGroup();
        EventLoopGroup worker=new NioEventLoopGroup();

        try{
            ServerBootstrap boot=new ServerBootstrap();
            boot.group(boss,worker);
            boot.channel(NioServerSocketChannel.class);
            boot.option(ChannelOption.SO_BACKLOG, 1024); //连接数
            boot.option(ChannelOption.TCP_NODELAY, true);  //不延迟，消息立即发送
            boot.childOption(ChannelOption.SO_KEEPALIVE, true); //长连接
            boot.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel)
                        throws Exception {
                    ChannelPipeline p = socketChannel.pipeline();
                    p.addLast(new NettyServerHandler());
                }
            });
            ChannelFuture f = boot.bind(port).sync();
            if (f.isSuccess()) {
                System.out.println("启动Netty服务成功，端口号：" + this.port);
            }
            //阻塞待待
            f.channel().closeFuture().sync();

        }catch (Exception e){
            System.out.println("异常："+e.getMessage());
        }finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }

    }

    public static void main(String[] args) {
        new NettyServerBootstrap(8899).run();
    }
}
