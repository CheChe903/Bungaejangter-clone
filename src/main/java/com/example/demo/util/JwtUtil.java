package com.example.demo.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;

public class JwtUtil {

    private final String SECRET_KEY;
    private final long ACCESS_TOKEN_EXPIRATION;


    public JwtUtil() throws IOException {

        Yaml yaml = new Yaml();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.yml")) {
            if(input == null)
                throw new RuntimeException("NOT FOUND");

            Map<String, Object> config = yaml.load(input);
            Map<String, Object> jwtConfig = (Map<String, Object>) config.get("jwt");

            this.SECRET_KEY = (String) jwtConfig.get("secret");
            Map<String, Object> accessConfig = (Map<String, Object>) jwtConfig.get("access");
            this.ACCESS_TOKEN_EXPIRATION = Long.parseLong(accessConfig.get("expiredMs").toString());
        } catch (IOException e) {
            throw new RuntimeException("I/O error while loading config", e);
        } catch (ClassCastException e) {
            throw new RuntimeException("Invalid config format", e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error while loading config", e);
        }
    }

    public String getSecretKey() {
        return SECRET_KEY;
    }

    public long getAccessTokenExpiration() {
        return ACCESS_TOKEN_EXPIRATION;
    }

    public String generateToken(Long memberId) {
        return Jwts.builder()
                .setSubject(memberId.toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public Long parseToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String subject = claims.getSubject();
            return Long.parseLong(subject);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse token", e);
        }
    }

}
