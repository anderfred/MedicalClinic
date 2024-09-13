package com.anderfred.medical.clinic.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenService {

  private final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
  private final long expirationTimeInMillis = 1000 * 60 * 60 * 24; // 24 hours
  public static final String TOKEN_ACTOR_ID = "actorId";
  public static final String TOKEN_KEY = "jwtToken";
  public static final String ROLES_KEY = "roles";

  public String generateToken(
      String userName, Collection<? extends GrantedAuthority> authorities, Long userId) {
    return Jwts.builder()
        .setSubject(userName)
        .setIssuedAt(new Date())
        .claim(
            ROLES_KEY,
            authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
        .setExpiration(new Date(System.currentTimeMillis() + expirationTimeInMillis))
        .signWith(secretKey)
        .claim(TOKEN_ACTOR_ID, userId)
        .compact();
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public String extractUsername(String token) {
    Claims claims =
        Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
    return claims.getSubject();
  }

  public Long extractActorId(String token) {
    Claims claims =
        Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
    return claims.get(TOKEN_ACTOR_ID, Long.class);
  }

  public Claims getAllClaimsFromToken(String token) {
    return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
  }

  public Date getIssuedAtDateFromToken(String token) {
    return getAllClaimsFromToken(token).getIssuedAt();
  }

  public Date getExpirationDateFromToken(String token) {
    return getAllClaimsFromToken(token).getExpiration();
  }

  public Collection<SimpleGrantedAuthority> getAuthorities(String token) {
    Claims claims = getAllClaimsFromToken(token);
    List<String> roles = claims.get(ROLES_KEY, List.class);
    return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
  }
}
