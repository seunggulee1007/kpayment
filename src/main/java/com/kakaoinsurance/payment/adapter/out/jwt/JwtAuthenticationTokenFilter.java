package com.kakaoinsurance.payment.adapter.out.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;
import static org.springframework.util.ObjectUtils.isEmpty;

@Slf4j
public class JwtAuthenticationTokenFilter extends GenericFilterBean {

    private static final Pattern BEARER = Pattern.compile("^Bearer$", Pattern.CASE_INSENSITIVE);

    private final String headerKey;

    private final Jwt jwt;

    public JwtAuthenticationTokenFilter(String headerKey, Jwt jwt) {
        this.headerKey = headerKey;
        this.jwt = jwt;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse res = (HttpServletResponse)response;
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            Optional<String> optionalAuthorizationToken = obtainAuthorizationToken(req);
            if (optionalAuthorizationToken.isPresent()) {
                String authorizationToken = optionalAuthorizationToken.get();
                try {
                    setNewAuthenticationToken(req, authorizationToken);
                } catch (Exception e) {
                    log.error("JWT processing failed : {}", e.getMessage());
                }
            }
        } else {
            log.debug("SecurityContextHolder not populated with security token, as it already contained: '{}'",
                      SecurityContextHolder.getContext().getAuthentication());
        }
        chain.doFilter(req, res);
    }

    private void setNewAuthenticationToken(HttpServletRequest req, String authorizationToken) {
        Jwt.Claims claims = verify(authorizationToken);
        Long id = claims.getId();
        String memberId = claims.getMemberId();
        String email = claims.getEmail();
        String nickname = claims.getNickname();

        if (nonNull(memberId) && !isEmpty(email) && nonNull(nickname)) {
            JwtAuthenticationToken authentication = new JwtAuthenticationToken(new JwtAuthentication(id, memberId, email, nickname), null,
                                                                               this.authorities(claims));
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }

    private Optional<String> obtainAuthorizationToken(HttpServletRequest req) {
        Optional<String> optionalToken = Optional.ofNullable(req.getHeader(headerKey));
        if (optionalToken.isPresent()) {
            String token = optionalToken.get();
            if (log.isDebugEnabled()) {
                log.debug("JWT authorization api detected : {}", token);
            }
            token = URLDecoder.decode(token, StandardCharsets.UTF_8);
            String[] parts = token.split(" ");
            if (parts.length == 2) {
                String scheme = parts[0];
                String credentials = parts[1];
                return BEARER.matcher(scheme).matches() ? Optional.ofNullable(credentials) : Optional.empty();
            }
        }
        return Optional.empty();
    }

    private Jwt.Claims verify(String token) {
        return jwt.verify(token);
    }

    public Collection<? extends GrantedAuthority> authorities(Jwt.Claims claims) {
        String[] roles = claims.getRoles();
        return Arrays.stream(roles).map(r -> new SimpleGrantedAuthority("ROLE_" + r)).collect(Collectors.toSet());
    }

}