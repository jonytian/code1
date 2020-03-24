package com.legaoyi.common.util;

import java.security.MessageDigest;

public class PasswordHelper {

	public static String SHA1(String password, String salt) throws Exception {
		byte[] bytes = salt.getBytes("utf-8");
		MessageDigest msgDigest = MessageDigest.getInstance("SHA");
		if (salt != null && bytes.length > 0) {
			msgDigest.update(bytes);
		}

		byte[] digest = msgDigest.digest(password.getBytes("utf-8"));
		StringBuilder ret = new StringBuilder();
		for (int i = 0; i < digest.length; i++) {
			String hex = Integer.toHexString(digest[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			ret.append(hex.toUpperCase());
		}
		return ret.toString();
	}
}
