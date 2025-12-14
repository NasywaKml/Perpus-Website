package Perpuskaan.demo.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import Perpuskaan.demo.entity.User;
import Perpuskaan.demo.repository.UserRepository;

@Service // <--- Anotasi ini PENTING biar dideteksi Spring
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. Cari user di database berdasarkan username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // 2. Konversi Role (Enum) kamu menjadi Authority yang dimengerti Spring Security
        // Role kamu: ADMIN / MEMBER -> Spring butuh: "ROLE_ADMIN" / "ROLE_MEMBER" (Opsional pake prefix ROLE_)
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().name());

        // 3. Kembalikan objek UserDetails bawaan Spring
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(authority) // List hak akses/role
        );
    }
}