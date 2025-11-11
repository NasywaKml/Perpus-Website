package Perpuskaan.demo.config; // Anda mungkin perlu membuat package config

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Menggunakan BCrypt untuk hashing password
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean 
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Nonaktifkan CSRF: Wajib untuk API/Postman
            .csrf(csrf -> csrf.disable())
            
            // Pengaturan otorisasi (perizinan)
            .authorizeHttpRequests(authz -> authz
                // Izinkan (permitAll) semua request ke /api/auth/**
                // Ini PENTING agar login & register Anda tidak diblokir
                .requestMatchers("/api/auth/**").permitAll() 
                
                // (Opsional) Izinkan endpoint lain jika ada (misal: lihat buku)
                // .requestMatchers("/api/buku/public/**").permitAll()
                
                // Semua request LAINNYA harus diautentikasi (login)
                .anyRequest().authenticated()
            )

            .logout(logout -> logout
                // 1. Tentukan URL untuk trigger logout
                .logoutUrl("/api/auth/logout") 

                // 2. Apa yang terjadi setelah logout sukses
                .logoutSuccessHandler((request, response, authentication) -> {
                    response.setContentType("application/json");
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write("{\"message\": \"Logout berhasil!\"}");
                })
                
                // 3. Hapus cookie JSESSIONID saat logout
                .deleteCookies("JSESSIONID") 
                
                // 4. Pastikan sesi HTTP-nya benar-benar di-invalidate
                .invalidateHttpSession(true) 
            );

        return http.build();
    }
}