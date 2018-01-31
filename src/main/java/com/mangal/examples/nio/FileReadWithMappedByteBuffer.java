package com.mangal.examples.nio;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by amangal on 2/28/17.
 *
 *  Faster file copy with Mapped byte buffer -- Memory Mapped File
 */
public class FileReadWithMappedByteBuffer {

    public static void main(String[] args){

        try{
            File file = new File(
                    ClassLoader.getSystemClassLoader().getResource("test.txt")
                            .getFile());
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
            FileChannel fileChannel = randomAccessFile.getChannel();

            MappedByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());
            buffer.load();
            for(int i=0; i<buffer.limit(); i++){
                System.out.print((char)buffer.get(i));
            }
            buffer.clear();

            fileChannel.close();
            randomAccessFile.close();

        }catch(IOException i){
            i.printStackTrace();
        }

    }
}
