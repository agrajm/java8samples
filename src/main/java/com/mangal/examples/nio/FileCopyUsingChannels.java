package com.mangal.examples.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by amangal on 3/1/17.
 */
public class FileCopyUsingChannels {

    private static File getResourceFile(String name) throws IOException {

        return new File(ClassLoader.getSystemClassLoader().getResource(name).getFile());
    }

    public static void main(String[] args){

        try {
            FileInputStream inputStream = new FileInputStream(getResourceFile("test.txt"));
            FileOutputStream outputStream = new FileOutputStream(getResourceFile("copy_of_test.txt"));

            FileChannel inChannel = inputStream.getChannel();
            FileChannel outChannel = outputStream.getChannel();

            copyData(inChannel, outChannel);

            inChannel.close();
            outChannel.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private static void copyData(FileChannel inChannel, FileChannel outChannel) throws IOException{

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(16*1024);

        while(inChannel.read(byteBuffer) != -1){

            byteBuffer.flip();

            while (byteBuffer.hasRemaining()){
                outChannel.write(byteBuffer);
            }

            byteBuffer.clear();
        }

    }
}
