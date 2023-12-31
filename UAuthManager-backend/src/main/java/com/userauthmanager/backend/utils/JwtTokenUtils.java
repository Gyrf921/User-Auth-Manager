package com.userauthmanager.backend.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenUtils {

    @Value("${jwt.lifetime}")
    private Duration jwtLifetime;

    private final SecretKey key = Jwts.SIG.HS256.key().build();

    private Claims getAllClaimsFromToken(String token) {
        log.info("[getAllClaimsFromToken] == some token");
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token).getPayload();
    }

    public String generateToken(UserDetails userDetails) {
        log.info("[generateToken] >> some userDetails");
        Map<String, Object> clains = new HashMap<>();
        List<String> rolesList = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        clains.put("roles", rolesList);
        clains.put("email", userDetails.getUsername()); //users email

        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + jwtLifetime.toMillis());

        log.info("[generateToken] << result is Jws");
        return Jwts.builder()
                .claims(clains)
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(expiredDate)
                .signWith(key)
                .compact();
    }

    public String getUsername(String jwt) {
        log.info("[getUsername] >> some email");
        return getAllClaimsFromToken(jwt).getSubject();
    }

    public List<String> getRoles(String jwt) {
        log.info("[getRoles] >> some roles");
        List<?> list = getAllClaimsFromToken(jwt).get("roles", List.class);

        List<String> totalList = new ArrayList<>();

        for (Object ob : list){
            totalList.add(ob.toString());
        }

        return totalList;
    }
}
