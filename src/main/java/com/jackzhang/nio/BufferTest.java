package com.jackzhang.nio;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * <p>Title: BufferTest</p>
 * <p>Description: 
 * 缓冲区本质上是一块可以写入数据，然后可以从中读取数据的内存。
 * 这块内存被包装成NIO Buffer对象，并提供了一组方法，用来方便的访问该块内存。
 * 一、根据数据类型的不同（除boolean外），提供了相应类型的缓冲区
 * ByteBuffer
 * CharBuffer
 * ShortBuffer
 * LongBuffer
 * FloatBuffer
 * DoubleBuffer
 *
 * 上述缓冲区的管理方法几乎一致，通过allocate()获取缓冲区
 * 二、缓冲区存取数据的两个核心方法：
 * put()：存入数据到缓冲区
 * get()：获取数据到缓冲区
 *
 * 三、缓冲区中的四个核心属性:
 * capacity:容量，示缓冲区最大存储容量，一量声明不能改变
 * limit:界限，表示缓冲区中可以操作数据的大小。（limit后数据不能进行读写）
 * position:位置，表示缓冲区中正在操作数据的位置
 *
 * position<=limit<=capacity
 *
 * </p>
 * @author 张杰
 * @version 1.0
 */
public class BufferTest {

	public static void main(String[] args) {
		RandomAccessFile accessFile;
		try {
			accessFile = new RandomAccessFile("data1.txt", "rw");
			FileChannel fileChannel=accessFile.getChannel();
			//create buffer with capacity of 48 bytes
			ByteBuffer buf=ByteBuffer.allocate(48);

			int bytesRead=fileChannel.read(buf);//read into buffer
			while(bytesRead!=-1){
				buf.flip();//make buffer ready for read

				while(buf.hasRemaining()){
					System.out.print((char)buf.get());//read 1 byte a at a time
				}

				buf.clear();//clear to make buffer ready for writing
				bytesRead=fileChannel.read(buf);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
