package com.eip.festevent.authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;

public class TokenHelper {

	private static final long TOKEN_EXPIRATION_TIME = 864000000; // 10 day
	private static final String ISSUER = "VisitCardPro";
	private static final String PRIVATE_KEY = "VisitCardPro";

	protected String value;
	protected Claims body;
	
	public TokenHelper(String token) {
		value = token;
		body = Jwts.parser()
				.setSigningKey(PRIVATE_KEY)
				.parseClaimsJws(value)
				.getBody();
	}
	
	public static String generateToken(String subject) {
		return Jwts.builder().setIssuedAt(new Date()).setIssuer(ISSUER)
				.setSubject(subject)
				.setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512, PRIVATE_KEY).compact();
	}
	public String getSubject() {
		return body.getSubject();
	}
	
	public Date getExpiration() {
		return body.getExpiration();		
	}
	
	public Date getIssuedDate() {
		return body.getIssuedAt();
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}