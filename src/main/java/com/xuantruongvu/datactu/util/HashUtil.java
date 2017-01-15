package com.xuantruongvu.datactu.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * @author xuantruongvu
 * This class provides with method to hash strings
 */
public class HashUtil {
	private static String algorithm = "MD5";
	
	public static String hashString(String message) throws Exception {
	 
	    try {
	        MessageDigest digest = MessageDigest.getInstance(HashUtil.algorithm);
	        byte[] hashedBytes = digest.digest(message.getBytes("UTF-8"));
	 
	        return convertByteArrayToHexString(hashedBytes);
	    } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
	        throw new Exception("Could not generate hash from String", ex);
	    }
	}
	
	private static String convertByteArrayToHexString(byte[] arrayBytes) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < arrayBytes.length; i++) {
            stringBuffer.append(Integer.toString((arrayBytes[i] & 0xff) + 0x100, 16)
                    .substring(1));
        }
        return stringBuffer.toString();
    }
}
