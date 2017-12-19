package com.jackzhang.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * <p>Title: GetChannel</p>
 * <p>Description: 旧IO库中三类被修改用以获得FileChannel
 * FileInputStream、FileOutputStream、RandomAcessFile
 * </p>
 */
public class GetChannel {

	private static final int BSIZE=1024;
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		try {
			//write a file
			FileChannel fileChannel=new FileOutputStream("data.txt").getChannel();
			fileChannel.write(ByteBuffer.wrap("Some text".getBytes()));
			fileChannel.close();

			//add to the end of the file
			fileChannel=new RandomAccessFile("data.txt", "rw").getChannel();
			fileChannel.position(fileChannel.size());
			fileChannel.write(ByteBuffer.wrap("\nSome more".getBytes()));
			fileChannel.close();

			//read the file
			fileChannel=new FileInputStream("data.txt").getChannel();
			ByteBuffer buff=ByteBuffer.allocate(BSIZE);
			fileChannel.read(buff);
			//将缓存字节数组的指针设置为数组的开始序列即数组下标0
			buff.flip();
			while(buff.hasRemaining()){
				//buff.getChar()每次获取两个字节，当只有一个时报java.nio.BufferUnderflowException
				//System.out.print(buff.getChar());
				System.out.print((char)buff.get());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
