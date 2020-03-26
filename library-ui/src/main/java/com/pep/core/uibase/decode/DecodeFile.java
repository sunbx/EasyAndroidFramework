package com.pep.core.uibase.decode;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * Created by yangzy on 2017/6/12.
 */

public class DecodeFile {
    private       String     TAG ="DecodeFile";
    public static DecodeFile instance;
    public static DecodeFile getInstance()
    {
        if(instance==null)
        {
            instance=new DecodeFile();
        }
        return  instance;
    }
    public boolean decodeFile(File encodeFile, File decodeFile)
    {
        
        boolean           bl                = false;
        FileInputStream   fileInputStream   =null;
        FileOutputStream  fileOutputStream  =null;
        DecodeInputStream decodeInputStream =null;
     
        try {
            if(!decodeFile.exists()){
                encodeFile.createNewFile();
            }
            fileInputStream=new FileInputStream(encodeFile);
            fileOutputStream=new FileOutputStream(decodeFile);
            decodeInputStream=new DecodeInputStream(fileInputStream);
            int len=0;
            byte[] buffer=new byte[1024];
            while((len=decodeInputStream.read(buffer)) != -1){
                fileOutputStream.write(buffer,0,len);
                fileOutputStream.flush();
            }
            bl=true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            bl=false;
        } catch (IOException e) {
            e.printStackTrace();
            bl=false;
        }finally {
            if(fileInputStream!=null)
            {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(fileOutputStream!=null)
            {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bl;
    }
    public byte[] getFileBuffer(InputStream inputStream) {
        FileInputStream       fileInputStream       = null;
        DecodeInputStream     decodeInputStream     = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        ByteBuffer            byteBuffer            = null;
        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            decodeInputStream = new DecodeInputStream(bufferedInputStream);
            byteArrayOutputStream = new ByteArrayOutputStream(inputStream.available());
            int len = 0;
            byte[] buffer = new byte[1024];
            while ((len = decodeInputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, len);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if(byteArrayOutputStream==null){
            return new byte[1];
        }

        return byteArrayOutputStream.toByteArray();
    }

    public static byte[] getFileBuffer(String path) {
        FileInputStream       fileInputStream       = null;
        DecodeInputStream     decodeInputStream     = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        ByteBuffer            byteBuffer            = null;
        try {
            File file = new File(path);
            fileInputStream = new FileInputStream(file);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            decodeInputStream = new DecodeInputStream(bufferedInputStream);
            //            byteBuffer=ByteBuffer.allocate((int)file.length());
            byteArrayOutputStream = new ByteArrayOutputStream((int) file.length());
            int len = 0;
            byte[] buffer = new byte[1024];
            while ((len = decodeInputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, len);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if(byteArrayOutputStream==null){
            return new byte[1];
        }

        return byteArrayOutputStream.toByteArray();
    }
}
