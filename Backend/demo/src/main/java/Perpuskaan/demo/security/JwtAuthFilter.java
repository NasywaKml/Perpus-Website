package Perpuskaan.demo.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger; // 1. Import Logger
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;
    
    // [HAPUS] private JwtService jwtService; -> Tidak dipakai, hapus saja.

    @Autowired
    private UserDetailsService userDetailsService; 

    // 2. Buat Logger
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Ambil Header Authorization
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        // Cek format Header
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            try {
                username = jwtUtils.extractUsername(token);
            } catch (Exception e) {
                // 3. Log error dengan benar (jangan System.out)
                logger.error("Gagal ekstrak username dari token: {}", e.getMessage());
            }
        }

        // Validasi dan Set Authentication
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            
            // Load data user dari DB
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // 4. [PENTING] Validasi Token biasanya membawa userDetails juga
            // Pastikan di JwtUtils method validateToken kamu support parameter (token, userDetails)
            // Kalau JwtUtils kamu cuma (token), kode ini tetap jalan, tapi lebih aman kalau di cek match-nya.
            if (jwtUtils.validateToken(token, userDetails)) { 
                
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, 
                        null, 
                        userDetails.getAuthorities()
                );
                
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                // Simpan sesi login di request ini saja
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}