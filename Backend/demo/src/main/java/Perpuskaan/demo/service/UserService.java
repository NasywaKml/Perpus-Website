package Perpuskaan.demo.service;

// import java.util.UUID; // <-- HAPUS IMPORT INI

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service; 

import Perpuskaan.demo.dto.RegisterRequest;
import Perpuskaan.demo.entity.Pemustaka;
import Perpuskaan.demo.entity.User;
import Perpuskaan.demo.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Metode untuk login
     */
    public User login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Username tidak ditemukan"));

        if (passwordEncoder.matches(password, user.getPassword())) {
            return user; // Login sukses
        } else {
            throw new RuntimeException("Password salah");
        }
    }

    /**
     * Metode untuk mendaftarkan Pemustaka baru
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
        
        // 6. HAPUS BARIS 'setNoAnggota'
        // pemustaka.setNoAnggota(generateNoAnggota()); // <-- HAPUS INI
        // Database akan mengisinya secara otomatis (AUTO_INCREMENT = 1000)
        
        // 7. Simpan ke database
        return userRepository.save(pemustaka);
    }
    
    /**
     * Metode untuk mendapatkan detail user berdasarkan ID.
     */
    public User getUserById(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));
    }

    // --- HAPUS SELURUH METHOD generateNoAnggota ---
    /*
    private String generateNoAnggota() {
        // ...
    }
    */
}