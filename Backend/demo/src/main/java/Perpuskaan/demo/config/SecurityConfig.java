package Perpuskaan.demo.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy; // <-- PENTING
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; // <-- PENTING
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import Perpuskaan.demo.security.JwtAuthFilter; // Import Filter yang kita buat tadi
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter jwtAuthFilter; // Inject Filter JWT

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // CSRF tidak diperlukan untuk JWT
            .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Gunakan config CORS di bawah

            // SETTING SESSION MENJADI STATELESS (PENTING UNTUK JWT)
            // Artinya server tidak akan menyimpan session/cookie user.
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/error").permitAll()
                .requestMatchers("/api/auth/**").permitAll() // Login & Register harus public
                
                // Public endpoints lainnya
                .requestMatchers(HttpMethod.GET, "/api/buku/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/peminjaman/**").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/buku/kategori/*").permitAll()
                
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // Handle Pre-flight CORS
                
                // Sisanya harus login
                .anyRequest().authenticated()
            )

            // SISIPKAN FILTER JWT SEBELUM FILTER LOGIN BAWAAN
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

            // Exception Handling Custom (seperti yang kamu punya sebelumnya)
            .exceptionHandling(e -> e
                .accessDeniedHandler((req, res, ex) -> {
                    System.out.println("=== 403 FORBIDDEN ===");
                    res.setContentType("application/json");
                    res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    res.getWriter().write("{\"error\": \"Akses Ditolak: " + ex.getMessage() + "\"}");
                })
                // Tambahkan ini untuk handle 401 (Belum Login / Token Salah)
                .authenticationEntryPoint((req, res, ex) -> {
                    res.setContentType("application/json");
                    res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    res.getWriter().write("{\"error\": \"Unauthorized: Harap login terlebih dahulu\"}");
                })
            );

        // CATATAN LOGOUT:
        // Di JWT, logout itu diatur di Client (hapus token dari localStorage).
        // Server tidak perlu logic logout khusus seperti session.deleteCookies().

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173")); 
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")); // Tambah PATCH
        configuration.setAllowedHeaders(List.of("*")); 
        configuration.setAllowCredentials(true); 

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); 
        return source;
    }
}