package Perpuskaan.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import Perpuskaan.demo.dto.response.BukuSearchResponseDto;
import Perpuskaan.demo.service.BukuService;

@RestController
@RequestMapping("/api/buku") // Base URL: localhost:8080/api/buku
@CrossOrigin(origins = "*")  // PENTING: Biar bisa diakses dari Frontend (Vue/React)
public class BukuController {

    private final BukuService bukuService;

    // Constructor Injection (Best Practice daripada @Autowired di field)
    public BukuController(BukuService bukuService) {
        this.bukuService = bukuService;
    }

    // ==========================================
    // 1. GET ALL BUKU
    // URL: GET localhost:8080/api/buku
    // ==========================================
    @GetMapping
    public ResponseEntity<List<BukuSearchResponseDto>> getAllBuku() {
        List<BukuSearchResponseDto> result = bukuService.getAllBuku();
        return ResponseEntity.ok(result);
    }

    // ==========================================
    // 2. SEARCH BUKU (By Judul OR Pengarang)
    // URL: GET localhost:8080/api/buku/search?keyword=harry
    // ==========================================
    @GetMapping("/search")
    public ResponseEntity<List<BukuSearchResponseDto>> searchBuku(
            @RequestParam("keyword") String keyword) {
        
        List<BukuSearchResponseDto> result = bukuService.searchBuku(keyword);
        return ResponseEntity.ok(result);
    }

    // ==========================================
    // 3. GET DETAIL BUKU (By ID)
    // URL: GET localhost:8080/api/buku/{1}
    // ==========================================
    @GetMapping("/{id}")
    public ResponseEntity<BukuSearchResponseDto> getBukuById(@PathVariable Integer id) {
        BukuSearchResponseDto result = bukuService.getBukuById(id);
        return ResponseEntity.ok(result);
    }

    // ==========================================
    // 4. GET ALL BUKU BY KATEGORI
    // URL: GET   
    // ==========================================
    @GetMapping("/kategori/{kategori}")
    public ResponseEntity<List<BukuSearchResponseDto>> getByKategori(
            @PathVariable String kategori) {
        return ResponseEntity.ok(bukuService.getAllBukuByKategori(kategori));
    }




}