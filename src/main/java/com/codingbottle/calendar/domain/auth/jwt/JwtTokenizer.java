package com.codingbottle.calendar.domain.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import lombok.Getter;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@Component
public class JwtTokenizer {
    @Getter
    @Value("${jwt.key}")
    private String secretKey;       // 토큰 시크릿 키

    @Getter
    @Value("${jwt.access-token-expiration-minutes}")
    private int accessTokenExpirationMinutes;     // accessToken 만료 시간

    @Getter
    @Value("${jwt.refresh-token-expiration-minutes}")
    private int refreshTokenExpirationMinutes;          // refreshToken 만료 시간

    public String generateAccessToken(Map<String, Object> claims,
                                      String audience) {
        Key key = createHmacShaKeyFromSecretKey(this.secretKey);
        Date expiration = getTokenExpiration(this.accessTokenExpirationMinutes);

        return Jwts.builder()
                .setClaims(claims)
                .setAudience(audience)
                .setIssuedAt(Calendar.getInstance().getTime())
                .setExpiration(expiration)
                .signWith(key)
                .compact();
    }

    public String generateRefreshToken(String audience) {
        Key key = createHmacShaKeyFromSecretKey(this.secretKey);
        Date expiration = getTokenExpiration(this.refreshTokenExpirationMinutes);

        return Jwts.builder()
                .setAudience(audience)
                .setIssuedAt(Calendar.getInstance().getTime())
                .setExpiration(expiration)
                .signWith(key)
                .compact();
    }

    public Jws<Claims> getClaims(String jws, String secretKey) {
        Key key = createHmacShaKeyFromSecretKey(secretKey);

        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jws);
        return claims;
    }

    public void verifySignature(String jws, String secretKey) {
        Key key = createHmacShaKeyFromSecretKey(secretKey);

        Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jws);
    }

    private Date getTokenExpiration(int expirationMinutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, expirationMinutes);
        Date expiration = calendar.getTime();

        return expiration;
    }

    public String getSecretKey() {
        return this.secretKey;
    }

    private Key createHmacShaKeyFromSecretKey(String secretKey) {
        byte[] keyBytes = secretKey.getBytes();
        Key key = Keys.hmacShaKeyFor(keyBytes);

        return key;
    }
}