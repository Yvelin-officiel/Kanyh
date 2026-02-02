package com.data.kanyh.config;

import com.data.kanyh.model.Role;
import com.data.kanyh.util.JwtUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            final String token = authHeader.substring(7);
            try {
                final String username = jwtUtil.extractUserName(token);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null
                        && jwtUtil.isTokenValid(token, username)) {

                    Role role = jwtUtil.extractRole(token);
                    List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role.name()));

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(username, null, authorities);

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception ex) {
                // token invalide ou erreur d'extraction -> laisser la requête continuer sans auth
            }
        }

        filterChain.doFilter(request, response);
    }
}
