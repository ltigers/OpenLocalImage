package com.cntysoft.weishop.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Administrator on 2015/3/21.
 */
public class MD5Utils {

    public  static String md5Psw(String psw){
        try {
            // 得到一个信息摘要器
            MessageDigest messageDigest = MessageDigest.getInstance("md5");
            byte[] result = messageDigest.digest(psw.getBytes());
            StringBuffer buffer = new StringBuffer();
            //把每一位做与运算0xff
            for(byte b : result){
               int num = b & 0xff;
               String str = Integer.toHexString(num);

               if(str.length() == 1){
                   buffer.append(0);
               }
               buffer.append(str);
            }
            return buffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }
}
