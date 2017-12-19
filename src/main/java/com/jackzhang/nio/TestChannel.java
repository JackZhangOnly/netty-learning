package com.jackzhang.nio;

import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Test;

/**
 * 一、通道（Channel）：用于资源节点与目标节点的连接。在Java NIO中负责数据缓冲区中数据的传输。
 * Channel本身不存储数据，因此需要配合缓冲区进行传输
 * 二、通道的主要实现类
 * java.nio.channel.Channel 接口：
 * 		--FileChannel
 * 		--SocketChannel
 * 		--ServerSocketChannel
 * 		--DatagramChannel  (UDP)
 * 三、获取通道
 * 1.Java针对支持通道的类提供了getChannel方法
 * 		本地IO:
 * 		FileInputStream/FileOutpuStream
 * 		RandomAcessFile
 * 		网络IO
 * 		Socket
 * 		ServerSocket
 * 		DatagramSocket
 * 2.在JDK1.7中的nio2针对各个通道提供了静态方法open()
 * 3.---------------Files工具类的newByteChannel()
 *
 * 四、通道之间的数据传输
 * transferFrom()
 * transferTo()
 *
 * 五、分散（Scatter）与聚集（Gather）
 * 分散读取（Scattering Reads）:将通道中的数据分散到多个缓冲区中
 * 聚集写入（Gathering Writes）：将多个缓冲区中的数据聚集到通道中
 *
 * 六、字符集：Charset
 * 编码：字符串-->字节数组
 * 解码：字节数组-->字符串
 *
 */
public class TestChannel {
	@Test
	public void test6() throws Exception{
		//字符集
		Charset charset1=Charset.forName("GBK");

		//获取编码器
		CharsetEncoder charsetEncoder=charset1.newEncoder();
		//获取解码器
		CharsetDecoder charsetDecoder=charset1.newDecoder();

		CharBuffer charBuffer=CharBuffer.allocate(1024);
		charBuffer.put("学习编程啊");
		charBuffer.flip();

		//编码
		ByteBuffer buffer=charsetEncoder.encode(charBuffer);
		for (int i = 0; i < 10; i++) {
			System.out.println(buffer.get());
		}

		//解码
		buffer.flip();
		CharBuffer charBuffer2=charsetDecoder.decode(buffer);
		System.out.println(charBuffer2.toString());

	}
	@Test
	public void test5(){
		Map<String,Charset> map=Charset.availableCharsets();
		Set<Entry<String,Charset>> set=map.entrySet();

		for (Entry<String, Charset> entry:set) {
			System.out.println(entry.getKey()+"===="+entry.getValue());
		}
	}
	@Test
	public void test4(){
		RandomAccessFile accessFile1=null,accessFile2=null;
		FileChannel inChannel=null,outChannel=null;
		try {
			accessFile1=new RandomAccessFile("data1.txt", "rw");
			accessFile2=new RandomAccessFile("data2.txt", "rw");

			inChannel=accessFile1.getChannel();
			outChannel=accessFile2.getChannel();

			ByteBuffer byteBuffer1=ByteBuffer.allocate(100);
			ByteBuffer byteBuffer2=ByteBuffer.allocate(1024);
			ByteBuffer[] dsts=new ByteBuffer[]{byteBuffer1,byteBuffer2};
			inChannel.read(dsts);//分散读到缓冲数组中

			for (ByteBuffer buffer:dsts) {
				buffer.flip();
			}
			System.out.println(new String(dsts[0].array(),0,dsts[0].limit()));
			System.out.println("------------------------");
			System.out.println(new String(dsts[1].array(),0,dsts[1].limit()));

			outChannel.write(dsts);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	public void test3(){
		//利用直接缓冲区完成文件的复制（内存映射文件）
		FileChannel inChannel=null,outChannel=null;
		try {
			inChannel=FileChannel.open(Paths.get("1.jpg"), StandardOpenOption.READ);
			outChannel=FileChannel.open(Paths.get("3.jpg"),StandardOpenOption.READ,StandardOpenOption.WRITE,StandardOpenOption.CREATE);

			inChannel.transferTo(0, inChannel.size(), outChannel);

			close(inChannel);
			close(outChannel);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void test2(){
		//利用直接缓冲区完成文件的复制（内存映射文件）
		FileChannel inChannel=null,outChannel=null;
		try {
			inChannel=FileChannel.open(Paths.get("1.jpg"),StandardOpenOption.READ);
			outChannel=FileChannel.open(Paths.get("3.jpg"),StandardOpenOption.READ,StandardOpenOption.WRITE,StandardOpenOption.CREATE);
			//内存映射文件
			MappedByteBuffer inMapperBuf=inChannel.map(MapMode.READ_ONLY, 0, inChannel.size());
			MappedByteBuffer outMapperBuf=outChannel.map(MapMode.READ_WRITE, 0, inChannel.size());

			//直接对缓冲区进行数据的读写操作
			byte[] dst=new byte[inMapperBuf.limit()];
			inMapperBuf.get(dst);
			outMapperBuf.put(dst);

			close(inChannel);
			close(outChannel);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void test1(){
		//利用通道完成文件复制（非直接缓冲区）
		FileInputStream fis=null;
		FileOutputStream fos=null;
		FileChannel inChannel=null,outChannel=null;
		try {
			fis=new FileInputStream("1.jpg");
			fos=new FileOutputStream("2.jpg");

			inChannel=fis.getChannel();
			outChannel=fos.getChannel();

			//分配指定大小的缓冲区
			ByteBuffer buf=ByteBuffer.allocate(1024);

			//将通道中的数据放入缓冲区
			while (inChannel.read(buf)!=-1) {
				buf.flip();//切换读取数据的模式
				//将缓冲区的数据写入到通道中
				outChannel.write(buf);
				buf.clear();//清空缓冲区
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			close(outChannel);
			close(inChannel);
			close(fos);
			close(fis);
		}

	}
	public void close(Closeable closeable){
		if(closeable!=null){
			try {
				closeable.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
