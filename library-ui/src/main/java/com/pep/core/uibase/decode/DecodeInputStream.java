package com.pep.core.uibase.decode;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by yangzy on 2017/4/5.
 * 流式解密
 */

public class DecodeInputStream extends BufferedInputStream {
    private String  TAG         ="DecodeInputStream";
    private int     size        = 0;
    private boolean findHeader  =false;
    private boolean hasEncode   =false;
    private boolean hasSZGS     =false;
    private int     encode_type =-1;
    public int getHeaderLength() {
        return headerLength+9;
    }
    private boolean readHeader=true;
    private int headerLength=0;
    FileOutputStream fileOutputStream;
    AESCrypt         aesCrypt=new AESCrypt();
    public DecodeInputStream(InputStream encrypted) {
        super(encrypted);
        if(pos==0) {
            byte[] header = new byte[4];
            byte[] length = new byte[4];
            byte[] type=new byte[1];
            try {
                read(header);
                String tt =new String(header);
                if(tt.equalsIgnoreCase("rjsz")) {
                    hasSZGS=true;
                    read(type);
                    encode_type=type[0];
                    if(encode_type==11||encode_type==12)
                    {
                        int size = read(length, 0, 4);
                        headerLength = byteArrayToInt(length);
                        byte[] info=new byte[headerLength];
                        read(info);
                        String ddd =new String(info,"UTF-8");
                        Log.d("ttt",ddd);
                    }
                }else
                {
                    skip(0);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            readHeader=false;
        }
    }

    @Override
    public synchronized long skip(long byteCount) throws IOException {
        long index= super.skip(byteCount);
        pos=(int)index;
        return index;
    }


    @Override
    public int read(@NonNull byte[] buffer, int byteOffset, int byteCount) throws IOException {
        int size=0;
        byte [] temp=null;
        if(readHeader)
        {
            synchronized (TAG) {
                size = super.read(buffer, byteOffset, byteCount);
            }
            return  size;
        }
        if(encode_type==11) {
            if (hasSZGS && !hasEncode && byteCount > 100) {
                synchronized (TAG) {
                    size = super.read(buffer, byteOffset, 1024);
                }
                try {
                    temp = aesCrypt.decrypt(buffer);
                    if (temp != null) {
                        System.arraycopy(temp,0,buffer,0,1024);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                hasEncode = true;
            } else {
                synchronized (TAG) {
                    size = super.read(buffer, byteOffset, byteCount);
                }
            }
        }
        else if(encode_type==12)
        {
            synchronized (TAG) {
                size = super.read(buffer, byteOffset, 1024);
            }
            if(size==1024) {
                try {
                    temp = aesCrypt.decrypt(buffer);
                    if (temp != null) {
                        System.arraycopy(temp,0,buffer,0,1024);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        else
        {
            synchronized (TAG) {
                size = super.read(buffer, byteOffset, byteCount);
            }
        }
        return size;
    }
    //byte 数组与 int 的相互转换
    public static int byteArrayToInt(byte[] b) {
        return   b[0] & 0xFF |
                (b[1] & 0xFF) << 8 |
                (b[2] & 0xFF) << 16 |
                (b[3] & 0xFF) << 24;
    }
    //整型转字节数组
    public static byte[] intToByteArray(int a) {
        return new byte[] {
                (byte) ((a >> 24) & 0xFF),
                (byte) ((a >> 16) & 0xFF),
                (byte) ((a >> 8) & 0xFF),
                (byte) (a & 0xFF)
        };
    }
}
