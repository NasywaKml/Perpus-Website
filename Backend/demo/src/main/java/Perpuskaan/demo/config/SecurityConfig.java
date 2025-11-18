package Perpuskaan.demo.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource; // <-- IMPORT BARU

import jakarta.servlet.http.HttpServletResponse; // <-- IMPORT BARU

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    // =================================================================================
    // TAMBAHAN BARU: Bean Konfigurasi CORS
    // =================================================================================
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // 1. Izinkan origin frontend Anda
        configuration.setAllowedOrigins(List.of("http://localhost:5173")); 
        
        // 2. Izinkan semua method (GET, POST, PUT, DELETE, etc.)
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); 
        
        // 3. Izinkan semua header (seperti Content-Type, Authorization)
        configuration.setAllowedHeaders(List.of("*")); 
        
        // 4. Izinkan kredensial (penting untuk cookie/session/login)
        configuration.setAllowCredentials(true); 

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Terapkan konfigurasi ini untuk semua path di aplikasi Anda
        source.registerCorsConfiguration("/**", configuration); 
        return source;
    }
    // =================================================================================


    @Bean 
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Nonaktifkan CSRF
            .csrf(csrf -> csrf.disable())
            
            // =================================================================
            // TAMBAHAN BARU: Terapkan Konfigurasi CORS yang kita buat di atas
            .cors(withDefaults())
            // =================================================================

            // Pengaturan otorisasi (perizinan)
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/api/auth/**").permitAll() 
                .requestMatchers("/api/buku/**").permitAll()
                .anyRequest().authenticated()
            )

            .logout(logout -> logout
                .logoutUrl("/api/auth/logout") 
                .logoutSuccessHandler((request, response, authentication) -> {
                    response.setContentType("application/json");
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write("{\"message\": \"Logout berhasil!\"}");
                })
                .deleteCookies("JSESSIONID") 
                .invalidateHttpSession(true) 
            );

        return http.build();
    }
}