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
 * Read a large file in chunks with fixed size buffer
 */
public class FileReadWithFixSizedBuffer {

    public static void main(String[] args){

        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        File file = new File(classLoader.getResource("test.txt").getFile());

        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
            FileChannel fileChannel = randomAccessFile.getChannel();
            // Fix Sized Buffer
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            while (fileChannel.read(buffer) > 0){
                buffer.flip();
                for(int i=0; i<buffer.limit(); i++){
                    System.out.print((char)buffer.get(i));
                }
                buffer.clear();
            }

            fileChannel.close();
            randomAccessFile.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
