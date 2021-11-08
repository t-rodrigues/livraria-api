package dev.thiagorodrigues.livraria.main.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenUtils {

    @Value("${jwt.secret}")
    private String secret;

    public String generateJwtToken(String userId) {
        return Jwts.builder().setSubject(userId).signWith(SignatureAlgorithm.HS256, secret).compact();
    }

    public boolean isValid(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Long getUserId(String token) {
        String subject = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();

        return Long.valueOf(subject);
    }

}
