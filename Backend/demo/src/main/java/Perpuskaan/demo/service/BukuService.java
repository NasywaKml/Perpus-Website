package Perpuskaan.demo.service;


import java.util.List;
import java.util.Optional;

import Perpuskaan.demo.entity.Buku;

public interface BukuService {
    
    // Untuk mengambil SEMUA buku
    List<Buku> getAllBuku();

    // Untuk mengambil SATU buku berdasarkan ID
    Optional<Buku> getBukuById(Integer id);

    // Menggunakan method custom dari repository Anda
    List<Buku> getBukuByJudul(String judul);

    // Menggunakan method custom dari repository Anda
    List<Buku> getBukuByPengarang(String namaPengarang);

    Buku createBuku(Buku buku);
}
