package com.example.videochat;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

/**
 * Created by Zach on 2021/6/11 16:22
 */
public class ByteUtil {

    public static String byte2hex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        String tmp = null;

        for (byte b : bytes) {
            tmp = Integer.toHexString(0xFF & b);
            if (tmp.length() == 1) {
                tmp = "0" + tmp.toUpperCase();
            }
            sb.append(tmp.toUpperCase()).append(" ");
        }
        return sb.toString();
    }

    public static String byteBufferToString(ByteBuffer buffer) {
        CharBuffer charBuffer = null;
        try {
            Charset charset = Charset.forName("UTF-8");
            CharsetDecoder decoder = charset.newDecoder();
            charBuffer = decoder.decode(buffer);
            buffer.flip();
            return charBuffer.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
