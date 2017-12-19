package com.jackzhang.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.junit.Test;

/**
 * 一、使用NIO完成网络通信的三个核心
 * 1.通首（Channel）：负责连接
 * 	java.nio.channels.Channel接口：
 * 		SelectableChannel(抽象类)
 * 			SocketChannel
 * 			ServerSocketChannel
 * 			DatagramChannel
 *
 * 			Pipe.SinkChannel
 * 			Pipe.SourceChannel
 * 2.缓冲区(Buffer)：负责数据的存取
 * 3.选择器(Selector)：是SelectableChannel的多路复用器，用于
 * 监控SelectableChannel的IO状况
 */
public class TestBlocking2 {

	@Test
	public void testClient() throws IOException{
		//1.获取通道
		SocketChannel socketChannel=SocketChannel.open(new InetSocketAddress("127.0.0.1", 8899));

		FileChannel inChannel=FileChannel.open(Paths.get("1.jpg"), StandardOpenOption.READ);
		//2.分配指定大小的缓冲区
		ByteBuffer buffer=ByteBuffer.allocate(1024);

		//3.读取本地文件，并发送到服务端
		while (inChannel.read(buffer)!=-1) {
			buffer.flip();
			socketChannel.write(buffer);
			buffer.clear();
		}
		//阻塞式，服务端不知数据已经传输完成，需要客户端通知
		socketChannel.shutdownOutput();

		//4.读取服务端反馈
		int len=0;
		while ((len=socketChannel.read(buffer))!=-1) {
			buffer.flip();
			System.out.println(new String(buffer.array(), 0,len));
			buffer.clear();
		}
		inChannel.close();
		socketChannel.close();
	}
	@Test
	public void testServer() throws IOException{
		//1.获取通道
		ServerSocketChannel serverSocketChannel=ServerSocketChannel.open();

		//2.绑定连接
		serverSocketChannel.bind(new InetSocketAddress(8899));

		//3.获取客户端连接的通道
		FileChannel fileChannel=FileChannel.open(Paths.get("4.jpg"), StandardOpenOption.CREATE,StandardOpenOption.WRITE);
		SocketChannel sChannel=serverSocketChannel.accept();

		//4.分配指定大小的缓冲区
		ByteBuffer buffer=ByteBuffer.allocate(1024);

		//5.接收客户端的数据，并保存到本地
		while (sChannel.read(buffer)!=-1) {
			buffer.flip();
			fileChannel.write(buffer);
			buffer.clear();
		}
		//发送反馈给客户端
		buffer.put("accept data successfully".getBytes());
		buffer.flip();
		sChannel.write(buffer);
		buffer.clear();

		//6.关闭
		sChannel.close();
		fileChannel.close();
		serverSocketChannel.close();
	}

}
