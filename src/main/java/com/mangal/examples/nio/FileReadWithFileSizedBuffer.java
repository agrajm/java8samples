package com.mangal.examples.nio;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by amangal on 2/28/17.
 *
 * Read a small file in buffer of file size
 */
public class FileReadWithFileSizedBuffer {

    public static void main(String[] args){

        try {
            ClassLoader classLoader = ClassLoader.getSystemClassLoader();
            File file = new File(classLoader.getResource("test.txt").getFile());
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
            FileChannel inChannel = randomAccessFile.getChannel();
            long fileSize = inChannel.size();
            ByteBuffer byteBuffer = ByteBuffer.allocate((int)fileSize);
            inChannel.read(byteBuffer);

            byteBuffer.flip();
            //byteBuffer.rewind();

            for(int i=0; i<fileSize; i++){
                System.out.print((char)byteBuffer.get(i));
            }

            inChannel.close();
            randomAccessFile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
