package com.jackzhang.nio;

import java.nio.ByteBuffer;

import org.junit.Test;
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
 * mark ：标记，表示记录当前position的位置，可以通过reset()恢复到mark的位置
 *
 * position<=limit<=capacity
 *
 * 四、非直接缓冲区、直接缓冲区
 * 非直接缓冲区：通过allocate()方法分配缓冲区，将缓冲区建立在JVM存中
 * 直接缓冲区：通过allocateDirect()方法分配缓冲区，将缓冲区分配在物理内存中，提高效率
 * </p>
 * @author 张杰
 * @version 1.0
 */
public class TestBuffer {
	@Test
	public void test3(){
		//分配直接缓冲区
		ByteBuffer buf=ByteBuffer.allocateDirect(1024);

		//isDirect() 判断是否是直接缓冲区
	}
	@Test
	public void test2(){
		String str="abcde";

		ByteBuffer buf=ByteBuffer.allocate(1024);

		buf.put(str.getBytes());

		buf.flip();
		byte[] dst=new byte[buf.limit()];
		buf.get(dst,0,2);
		System.out.println("读取的数据---"+new String(dst,0,dst.length));
		//mark()：标记
		buf.mark();

		buf.get(dst,2,2);
		System.out.println("读取的数据---"+new String(dst,2,2));
		System.out.println(buf.position());

		//reset()：恢复到mark的位置
		buf.reset();
		System.out.println(buf.position());

		//hasRemaining：判断缓冲区中可以操作的数据
		if(buf.hasRemaining()){
			System.out.println(buf.remaining());
		}

	}

	@Test
	public void test(){
		String str="abcde";
		//1.分配一个固定大小的缓冲区
		ByteBuffer buf=ByteBuffer.allocate(1024);

		System.out.println("------------allocate-------------");
		System.out.println(buf.position());
		System.out.println(buf.limit());
		System.out.println(buf.capacity());

		//2.利用put()存入数据到缓冲区中
		buf.put(str.getBytes());
		System.out.println("------------put-------------");
		System.out.println(buf.position());
		System.out.println(buf.limit());
		System.out.println(buf.capacity());

		//3.切换到读取数据模式
		buf.flip();
		System.out.println("------------buf-------------");
		System.out.println(buf.position());
		System.out.println(buf.limit());
		System.out.println(buf.capacity());

		byte[] dst=new byte[buf.limit()];
		buf.get(dst);
		System.out.println("读取的数据---"+new String(dst,0,dst.length));

		System.out.println("------------get after-------------");
		System.out.println(buf.position());
		System.out.println(buf.limit());
		System.out.println(buf.capacity());

		//5.rewind：可重复读数据

		//6.clear:清空缓冲区，但是缓冲区中的数据仍存在，处于“遗忘”状态（位置、界限乱）
	}
}
