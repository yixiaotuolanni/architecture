package edu.ynu.se.xiecheng.achitectureclass.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenUtil {
    private static long ttl = 60 * 1000 * 60;
    private SecretKey key = Jwts.SIG.HS256.key().build();

    public String generateToken(Long userId) {
        Date expiryDate = new Date(new Date().getTime() + ttl);
        String jws = null;
        try {
            jws = Jwts.builder()
                    .header()
                    .add("for","user")
                    .and()

                    .claim("userId",userId)
                    .expiration(expiryDate)
                    .issuedAt(new Date())

                    .signWith(key)
                    .compact();
        }catch (JwtException ex){
            ex.printStackTrace();
        }
        return jws;
    }

    public Long getUseIdFromToken(String token) {
        Long userId = null;
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            Object userIdObject = claims.get("userId");
            if (userIdObject instanceof Integer) {
                userId = ((Integer) userIdObject).longValue(); // 将 Integer 转换为 Long
            } else if (userIdObject instanceof Long) {
                userId = (Long) userIdObject;
            }

            System.out.println(userId);
        }catch (JwtException ex){
            ex.printStackTrace();
        }
        return userId;
    }

    public boolean validateToken(String token) {
        try{
            return !isTokenExpired(token);
        }catch (Exception exception){
            exception.printStackTrace();
            return false;
        }
    }

    private boolean isTokenExpired(String token) throws Exception{
        Date expiryDate = getExpirationDateFromToken(token);
        return expiryDate.before(new Date());
    }

    private Date getExpirationDateFromToken(String token) throws IllegalArgumentException {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return claims.getExpiration();
        }catch (IllegalArgumentException exception){
            throw exception;
        }
    }
}

