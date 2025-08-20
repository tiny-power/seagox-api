package com.seagox.lowcode.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTokenUtils {
	
	/**
     * 密钥
     */
	public static final String SECRET = "48bf7c556cde81937e66044fcede328f";
	
	/**
     * 过期时间(单位：ms)
     */
	public static final long EXPIRATION = 86400 * 1000;
	
	public static final String TOKENHEAD = "Bearer ";

	public static String sign(Map<String, Object> payload, String secret, long expiration) {
		Date now = new Date(System.currentTimeMillis());
		return Jwts.builder().setClaims(payload).setExpiration(new Date(now.getTime() + expiration)).signWith(SignatureAlgorithm.HS512, secret).compact();
	}

	public static Map<String, Object> unSign(String token) {
		Map<String, Object> result = new HashMap<>();
		try {
			result = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token.replace(TOKENHEAD, "")).getBody();
			result.put("verify", true);
		} catch (Exception e) {
			result.put("verify", false);
			e.printStackTrace();
		}
		return result;
	}

}
