package Perpuskaan.demo.controller;


import Perpuskaan.demo.entity.Buku;
import Perpuskaan.demo.service.BukuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/buku") // Base URL untuk semua endpoint di controller ini
public class BukuController {

    @Autowired
    private BukuService bukuService;

    // API untuk mengambil SEMUA data buku
    // URL: GET http://localhost:8080/api/buku/all
    @GetMapping("/all")
    public ResponseEntity<List<Buku>> getAllBuku() {
        List<Buku> bukuList = bukuService.getAllBuku();
        return ResponseEntity.ok(bukuList); // Respon 200 OK
    }

    // API untuk mengambil SATU buku berdasarkan ID
    // URL: GET http://localhost:8080/api/buku/1 (angka 1 adalah contoh id)
    @GetMapping("/{id}")
    public ResponseEntity<Buku> getBukuById(@PathVariable Integer id) {
        Optional<Buku> buku = bukuService.getBukuById(id);
        return buku.<ResponseEntity<Buku>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    
    // API untuk mencari buku berdasarkan Judul
    // URL: GET http://localhost:8080/api/buku/judul/Laskar Pelangi
    @GetMapping("/judul/{judul}")
    public ResponseEntity<List<Buku>> getBukuByJudul(@PathVariable String judul) {
        List<Buku> bukuList = bukuService.getBukuByJudul(judul);
        return ResponseEntity.ok(bukuList);
    }

    // API untuk mencari buku berdasarkan Pengarang
    // URL: GET http://localhost:8080/api/buku/pengarang/Andrea Hirata
    @GetMapping("/pengarang/{namaPengarang}")
    public ResponseEntity<List<Buku>> getBukuByPengarang(@PathVariable String namaPengarang) {
        List<Buku> bukuList = bukuService.getBukuByPengarang(namaPengarang);
        return ResponseEntity.ok(bukuList);
    }

    @PostMapping("/add")
    public ResponseEntity<Buku> addBuku(@RequestBody Buku buku) {
        Buku bukuBaru = bukuService.createBuku(buku);
        // Kembalikan 201 CREATED + data buku yang baru dibuat
        return new ResponseEntity<>(bukuBaru, HttpStatus.CREATED);
    }
}