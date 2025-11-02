package Perpuskaan.demo.controller;

import Perpuskaan.demo.dto.*;
import Perpuskaan.demo.entity.User;
import Perpuskaan.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*; // Pastikan import @RestController dll ada

@RestController // <-- 1. PINDAHKAN KE SINI (DI LUAR KELAS)
@RequestMapping("/api/auth") // <-- 2. PINDAHKAN KE SINI, dan ganti nilainya
public class AuthController { // <-- 3. HANYA DEKLARASI KELAS SATU KALI

    // @RestController, @RequestMapping, dan 'public class AuthController'
    // yang error di dalam sini sudah dihapus.

    @Autowired
    private UserService userService;

    /**
     * Endpoint untuk Login
     * POST /api/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            // Panggil service login yang kita buat sebelumnya
            User user = userService.login(loginRequest.getUsername(), loginRequest.getPassword());

            // SANGAT PENTING: Jangan pernah kirim password kembali ke client
            user.setPassword(null); 

            return ResponseEntity.ok(user);
            
        } catch (RuntimeException e) {
            // Jika username/password salah, kirim error Unauthorized
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Login Gagal: " + e.getMessage());
        }
    }

    /**
     * Endpoint untuk Logout
     * POST /api/auth/logout
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        // (Lihat catatan logout di percakapan sebelumnya)
        return ResponseEntity.ok("Logout berhasil");
    }
}