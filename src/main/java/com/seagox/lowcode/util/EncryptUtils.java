package com.seagox.lowcode.util;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.util.DigestUtils;

public class EncryptUtils {
	
	public static String md5Encode(String plainText) {
        try {
            return DigestUtils.md5DigestAsHex(plainText.getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
	
	/**
     * 加密
     */
	public static String hashpw(String password) {
		return BCrypt.hashpw(password, BCrypt.gensalt());
	}
	
	/**
     * 解密
     */
	public static boolean checkpw(String plaintext, String hashed) {
		return BCrypt.checkpw(plaintext, hashed);
	}

}
