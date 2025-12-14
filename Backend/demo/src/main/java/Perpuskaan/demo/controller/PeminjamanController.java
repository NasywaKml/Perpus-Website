package Perpuskaan.demo.controller;

import java.util.List;
import java.security.Principal; // Import ini penting

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import Perpuskaan.demo.dto.request.PeminjamanRequestDto;
import Perpuskaan.demo.dto.response.PeminjamanResponseDto;
import Perpuskaan.demo.service.PeminjamanService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/peminjaman")
@RequiredArgsConstructor
public class PeminjamanController {

    private final PeminjamanService peminjamanService;

    // 1. PINJAM BUKU
    @PostMapping("/pinjam")
    public ResponseEntity<PeminjamanResponseDto> pinjam(
            @RequestBody PeminjamanRequestDto req, 
            Principal principal // <-- Magic Spring Security
    ) {
        // 'principal.getName()' otomatis berisi email/username dari Token JWT yang valid.
        // Kirim username ini ke service, biar service yang cari User ID-nya di DB.
        PeminjamanResponseDto response = peminjamanService.pinjam(req, principal.getName());
        
        return new ResponseEntity<>(response, HttpStatus.CREATED); // Return 201 Created
    }

    // 2. KEMBALIKAN BUKU (Tambahan Logic)
    @PutMapping("/kembali/{idPeminjaman}")
    public ResponseEntity<PeminjamanResponseDto> kembalikan(
            @PathVariable Integer idPeminjaman,
            Principal principal
    ) {
        // Logic pengembalian (hitung denda, update stok, update tanggal kembali)
        PeminjamanResponseDto response = peminjamanService.kembalikan(idPeminjaman, principal.getName());
        return ResponseEntity.ok(response);
    }

    // 3. GET HISTORY SENDIRI (User Biasa)
    @GetMapping("/history") // Gak perlu kasih parameter ID, ambil dari token
    public ResponseEntity<List<PeminjamanResponseDto>> getMyHistory(Principal principal) {
        // Ambil data berdasarkan siapa yang sedang login
        List<PeminjamanResponseDto> response = peminjamanService.getAllByUsername(principal.getName());
        return ResponseEntity.ok(response);
    }

    // 4. GET ALL (Hanya Admin)
    // Di SecurityConfig pastikan endpoint ini diprotect atau pakai anotasi ini:
    // @PreAuthorize("hasRole('ADMIN')") 
    @GetMapping
    public ResponseEntity<List<PeminjamanResponseDto>> getAll() {
        return ResponseEntity.ok(peminjamanService.getAll());
    }

    // 5. GET DETAIL
    @GetMapping("/{id}")
    public ResponseEntity<PeminjamanResponseDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(peminjamanService.getById(id));
    }
}