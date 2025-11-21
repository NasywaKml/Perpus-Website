package Perpuskaan.demo.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // perizinan untuk frontend
        configuration.setAllowedOrigins(List.of("http://localhost:5173")); 
        
        // 2. Izinkan method (GET, POST, PUT, DELETE, etc.)
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); 
        
        // 3. Izinkan header (seperti Content-Type, Authorization)
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

            .csrf(csrf -> csrf.disable())
            
            .cors(withDefaults())

            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/error").permitAll()
                .requestMatchers("/api/buku/kategori/*").permitAll()
                .requestMatchers("/api/buku/**").permitAll()
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers("/api/auth/**").permitAll() 
                
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
            )

           .exceptionHandling(e ->
            e.accessDeniedHandler((req, res, ex) -> {

                // TAMBAHKAN LOG INI
                System.out.println("==========================================================");
                System.out.println("=== 403 FORBIDDEN TERJADI UNTUK: " + req.getRequestURI());
                System.out.println("=== PESAN ERROR: " + ex.getMessage());
                System.out.println("=== PENYEBAB UTAMA (STACK TRACE):");
                ex.printStackTrace(); // Ini akan mencetak jejak lengkap ke konsol
                System.out.println("==========================================================");

                // Kode Anda sebelumnya untuk mengirim respon
                res.setContentType("application/json");
                res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                res.getWriter().write("{\"error\": \"Akses Ditolak. Periksa log server untuk detail.\"}");
            })
        );


        return http.build();
    }
}