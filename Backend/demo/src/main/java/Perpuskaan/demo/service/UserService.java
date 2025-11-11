package Perpuskaan.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder; // Import
import org.springframework.stereotype.Service;


import Perpuskaan.demo.dto.RegisterRequest; // Import DTO
import Perpuskaan.demo.entity.Pemustaka;
import Perpuskaan.demo.entity.User;
import Perpuskaan.demo.repository.UserRepository;

import java.util.UUID; // Untuk generate noAnggota

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    // 1. Inject PasswordEncoder
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Metode untuk login (Versi Sederhana).
     * CATATAN: Ini harus di-update untuk menggunakan passwordEncoder.
     */
    public User login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Username tidak ditemukan"));

        // 2. Cek password dengan passwordEncoder
        if (passwordEncoder.matches(password, user.getPassword())) {
            return user; // Login sukses
        } else {
            throw new RuntimeException("Password salah");
        }
    }

    /**
     * Metode untuk mendaftarkan Pemustaka baru menggunakan DTO.
     */
    public Pemustaka registerPemustaka(RegisterRequest request) {
        // 1. Cek apakah username sudah ada
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username sudah terdaftar");
        }
        
        // 2. Buat entitas Pemustaka baru
        Pemustaka pemustaka = new Pemustaka();
        
        // 3. Map data dari DTO ke Entity
        pemustaka.setUsername(request.getUsername());
        pemustaka.setEmail(request.getEmail());
        pemustaka.setAlamat(request.getAlamat());
        pemustaka.setNoTelpon(request.getNoTelpon());
        
        // 4. HASH PASSWORD sebelum disimpan!
        pemustaka.setPassword(passwordEncoder.encode(request.getPassword()));
        
        // 5. Set nilai default
        pemustaka.setStatusKeanggotaan("Aktif");
        pemustaka.setNoAnggota(generateNoAnggota()); // Panggil helper
        
        // 6. Simpan ke database
        return userRepository.save(pemustaka);
    }
    
    /**
     * Metode untuk mendapatkan detail user berdasarkan ID.
     */
    public User getUserById(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));
    }

    // --- Helper Method ---
    
    /**
     * Helper sederhana untuk membuat Nomor Anggota unik.
     * (Dalam aplikasi nyata, ini bisa lebih kompleks, misal: "P" + Tahun + urutan)
     */
    private String generateNoAnggota() {
        // Contoh: "ANGGOTA-8d8f8a8-4e4b-4a4a-..."
        return "ANGGOTA-" + UUID.randomUUID().toString().substring(0, 8);
    }

}