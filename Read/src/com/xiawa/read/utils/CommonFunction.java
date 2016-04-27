package com.xiawa.read.utils;

/*
 * 获取MD5函数
 * @author zhengwei
 */

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CommonFunction {
	
	public static String getMD5(String val) throws NoSuchAlgorithmException{          //计算字符串的Md5值  
        MessageDigest md5 = MessageDigest.getInstance("MD5");  
        try {
			md5.update(val.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {}  
        byte[] m = md5.digest();  
        return byte2hex(m);  
    }  

    static String byte2hex(byte[] b) {                                         //将字节的值用十六进制数表示
		final char HEX_DIGITS[] = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f' }; 
	    StringBuilder sb = new StringBuilder(b.length * 2);   
	    for (int i = 0; i < b.length; i++) {   
	        sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);   
	        sb.append(HEX_DIGITS[b[i] & 0x0f]);   
	    } 
        return sb.toString();   
	}

}
