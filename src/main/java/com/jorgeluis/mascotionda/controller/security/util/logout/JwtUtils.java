package com.jorgeluis.mascotionda.controller.security.util.logout;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jorgeluis.mascotionda.controller.security.SimpleGrantedAuthorityJsonCreator;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import static com.jorgeluis.mascotionda.controller.security.util.auth.TokenJwtConfig.SECRET_KEY;


@Component
public class JwtUtils {
    private final ObjectMapper mapper;

    public JwtUtils() {
        this.mapper = new ObjectMapper()
                .addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityJsonCreator.class)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * Extrae y devuelve la fecha de expiración (exp) del JWT.
     * @param token
     * @return
     */
    public Date getExpirationDateFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getExpiration();
    }

    /**
     * Lee el subject (username) del JWT.
     *
     * Extrae el claim "authorities" y lo deserializa como lista de SimpleGrantedAuthority.
     *
     * Devuelve un objeto UsernamePasswordAuthenticationToken, que es el que Spring Security usa internamente.
     * @param token
     * @return
     */
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        String username = claims.getSubject();
        byte[] authBytes = claims.get("authorities").toString().getBytes();
        Collection<? extends GrantedAuthority> authorities = null;
        try {
            authorities = Arrays.asList(mapper.readValue(authBytes, SimpleGrantedAuthority[].class));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new UsernamePasswordAuthenticationToken(username, null, authorities);
    }
}

