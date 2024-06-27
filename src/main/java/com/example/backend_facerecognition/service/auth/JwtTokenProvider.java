package com.example.backend_facerecognition.service.auth;

import com.example.backend_facerecognition.constant.ErrorCodeDefs;
import com.example.backend_facerecognition.dto.response.BaseResponseError;
import com.example.backend_facerecognition.exception.JwtTokenInvalid;
import com.example.backend_facerecognition.model.UserDetailsImpl;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
@Data
public class JwtTokenProvider {
    @Value("${jwt.jwtSecret}")
    private String jwtSecret;

    @Value("${jwt.jwtExpirationMs}")
    private Long jwtExpirationMs;

    @Value("${jwt.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;

    private static final String AUTHORITIES_KEY = "XAUTHOR";

    @PostConstruct
    public void init() {
        jwtSecret = Base64.getEncoder().encodeToString(jwtSecret.getBytes());
        log.info("jwt secret: {}", jwtSecret);
    }


    public String generateTokenWithAuthorities(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        final String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setExpiration(new Date(new Date().getTime() + jwtExpirationMs))
                .setIssuedAt(new Date())
                .setSubject(userPrincipal.getUserCode())
                .claim(AUTHORITIES_KEY , authorities)
                .signWith(SignatureAlgorithm.HS512 , jwtSecret).compact();

    }





    public UsernamePasswordAuthenticationToken getUserInfoFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        if (claims.get(AUTHORITIES_KEY) != null && claims.get(AUTHORITIES_KEY) instanceof String authoritiesStr && !Strings.isBlank(authoritiesStr)) {
            Collection authorities = Arrays.stream(authoritiesStr.split(","))
                    .map(SimpleGrantedAuthority::new)
                    .toList();
            return new UsernamePasswordAuthenticationToken(claims.getSubject(), "", authorities);

        } else {
            return new UsernamePasswordAuthenticationToken(claims.getSubject(), "", new ArrayList<>());
        }
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
//            throw new TokenExpired(ErrorCodeDefinitions.getErrMsg(ErrorCodeDefinitions.TOKEN_EXPIRED));
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }

        throw new JwtTokenInvalid(ErrorCodeDefs.getMessage(ErrorCodeDefs.TOKEN_INVALID));

    }
}
