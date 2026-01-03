package Perpuskaan.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import Perpuskaan.demo.dto.LoginResponse;
import Perpuskaan.demo.dto.response.PemustakaResponse;
import Perpuskaan.demo.dto.RegisterRequest;
import Perpuskaan.demo.dto.request.RegisterPustakawanRequest;

import Perpuskaan.demo.entity.Pemustaka;
import Perpuskaan.demo.entity.Pustakawan;
import Perpuskaan.demo.entity.Role;
import Perpuskaan.demo.entity.StatusKeanggotaan;
import Perpuskaan.demo.entity.User;
import Perpuskaan.demo.repository.UserRepository;
import Perpuskaan.demo.security.JwtUtils;
import java.util.List;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    

    /**
     * Metode untuk login
     */
    public LoginResponse login(String username, String password) {
        // 1. Cari User
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Username tidak ditemukan"));

        // 2. Cek Password
        if (passwordEncoder.matches(password, user.getPassword())) {
            
            // 3. Generate Token
            String token = jwtUtils.generateToken(user.getUsername());
            
            // 4. Masukkan data ke LoginResponse
            // Kita convert Enum role ke String biar aman di JSON
            return new LoginResponse(
                    token,
                    user.getIdUser(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getRole().name() 
            );
            
        } else {
            throw new RuntimeException("Password salah");
        }
    }

    /**
     * Metode untuk mendaftarkan Pemustaka baru
     */
    public Pemustaka registerPemustaka(RegisterRequest request) {
        // 1. Validasi Username (Sudah ada)
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username sudah terdaftar");
        }

        // 2. TAMBAHAN: Validasi Email (Penting!)
        // Asumsi kamu punya method findByEmail di repository
        // Jika belum ada, buat dulu di UserRepository: Optional<User> findByEmail(String email);
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
             throw new RuntimeException("Email sudah digunakan");
        }
        
        // 3. Buat entitas Pemustaka baru
        Pemustaka pemustaka = new Pemustaka();
        
        // 4. Map data dari DTO ke Entity
        pemustaka.setUsername(request.getUsername());
        pemustaka.setEmail(request.getEmail());
        pemustaka.setAlamat(request.getAlamat());
        pemustaka.setNoTelpon(request.getNoTelpon());
        
        // 5. HASH PASSWORD
        pemustaka.setPassword(passwordEncoder.encode(request.getPassword()));
        
        // 6. Set nilai default
        pemustaka.setStatusKeanggotaan(StatusKeanggotaan.AKTIF);

        
        // --- POIN PENTING DISKUSI KITA TADI ---
        // 7. Set Role secara otomatis menjadi MEMBER
        pemustaka.setRole(Role.MEMBER); 
        
        // 8. Simpan ke database
        return userRepository.save(pemustaka);
    }
    
    /**
     * Metode untuk mendapatkan detail user berdasarkan ID.
     */
    public User getUserById(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));
    }
    public List<PemustakaResponse> getAllPemustaka() {
        return userRepository.findByRole(Role.MEMBER)
            .stream()
            .map(user -> {
                Pemustaka p = (Pemustaka) user;
                PemustakaResponse dto = new PemustakaResponse();
                dto.setIdUser(p.getIdUser());
                dto.setUsername(p.getUsername());
                dto.setEmail(p.getEmail());
                dto.setRole(p.getRole().name());
                dto.setNoAnggota(p.getNoAnggota());
                dto.setStatusKeanggotaan(p.getStatusKeanggotaan().name());
                return dto;
            })
            .toList();
    }



   public Pustakawan registerPustakawan(RegisterPustakawanRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username sudah digunakan");
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email sudah digunakan");
        }

        Pustakawan pustakawan = new Pustakawan();

        pustakawan.setUsername(request.getUsername());
        pustakawan.setEmail(request.getEmail());
        pustakawan.setPassword(passwordEncoder.encode(request.getPassword()));
        pustakawan.setRole(Role.ADMIN); 
        pustakawan.setNip(request.getNip());
        pustakawan.setJabatan(request.getJabatan());

        return userRepository.save(pustakawan);
    }



    
}