package com.crm.admin.security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final String COOKIE_NAME = "jwt_token";

    private final JwtUtil jwtUtil;
    private final SessionManager sessionManager;

    public JwtAuthFilter(JwtUtil jwtUtil, SessionManager sessionManager) {
        this.jwtUtil = jwtUtil;
        this.sessionManager = sessionManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String token = resolveToken(request);

        if (token != null && jwtUtil.isValid(token)) {
            String username = jwtUtil.extractUsername(token);
            String role     = jwtUtil.extractRole(token);

            if (sessionManager.isValidSession(username, token)) {
                sessionManager.updateActivity(username, token);

                request.setAttribute("loggedInUser", username);
                request.setAttribute("loggedInRole", role);
                request.setAttribute("JWT_TOKEN",    token);

                var authority = new SimpleGrantedAuthority("ROLE_" + role.toUpperCase());
                var auth = new UsernamePasswordAuthenticationToken(
                        username, null, List.of(authority));
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (COOKIE_NAME.equals(cookie.getName())) {
                    String value = cookie.getValue();
                    if (value != null && !value.isBlank()) {
                        return value;
                    }
                }
            }
        }

        return null;
    }
}
