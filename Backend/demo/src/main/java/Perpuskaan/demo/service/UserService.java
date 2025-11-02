package Perpuskaan.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Import juga model-model Anda
import Perpuskaan.demo.entity.User;
import Perpuskaan.demo.entity.Pemustaka;
import Perpuskaan.demo.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Metode untuk login (Versi Sederhana).
     * Metode ini akan mencari user berdasarkan username,
     * lalu membandingkan password-nya.
     */
    public User login(String username, String password) {
        // 1. Cari user berdasarkan username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Username tidak ditemukan"));

        // 2. Cek password (SANGAT TIDAK AMAN, lihat catatan di bawah)
        if (user.getPassword().equals(password)) {
            return user; // Login sukses
        } else {
            throw new RuntimeException("Password salah");
        }
    }

    /**
     * Metode untuk mendaftarkan Pemustaka baru.
     */
    public Pemustaka registerPemustaka(Pemustaka pemustaka) {
        // Di sini Anda bisa tambahkan validasi, 
        // misalnya cek apakah username/email sudah dipakai
        
        // HASHING PASSWORD (Contoh sederhana, idealnya pakai Spring Security)
        // pemustaka.setPassword(passwordEncoder.encode(pemustaka.getPassword()));
        
        return userRepository.save(pemustaka);
    }
    
    /**
     * Metode untuk mendapatkan detail user berdasarkan ID.
     */
    public User getUserById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));
    }

    // Anda bisa tambahkan metode lain seperti:
    // - registerPustakawan(Pustakawan pustakawan)
    // - updateUserProfile(Integer id, User userDetails)
    // - logout() (biasanya di-handle oleh Spring Security)
}
