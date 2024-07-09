package org.dreamorbit.bookstore.security;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtHelper {
	
	private String secret = "948fbx9w89e8w0jkhdfhdkljflsdfhdslfkj74938573hnkfh98re0pxk899e";
	
	public String generateToken(UserDetails user) {
		
		return Jwts.builder().setSubject(user.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis()+60000*30))
				.signWith(keyBuilder(), SignatureAlgorithm.HS256)
				.compact();
				
	}

	private Key keyBuilder() {
		byte[] key = Decoders.BASE64.decode(secret);
		return Keys.hmacShaKeyFor(key);
	}

	public String extractUserName(String jwtToken) {
		
		return extractClaims(jwtToken, Claims::getSubject);
	}
	
	private <T> T extractClaims(String token, Function<Claims, T> claimsResolvers) {
		final Claims claims = extractAllClaims(token);
		return claimsResolvers.apply(claims);
	} 
	
	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(keyBuilder()).build().parseClaimsJws(token).getBody();
	}

	public boolean isTokenValid(String token, UserDetails userDetails) {
		final String username = extractUserName(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	private boolean isTokenExpired(String token) {
		
		return extractClaims(token, Claims::getExpiration).before(new Date());
	}
}
