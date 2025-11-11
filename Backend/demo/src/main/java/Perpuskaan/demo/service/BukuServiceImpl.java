package Perpuskaan.demo.service;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Perpuskaan.demo.entity.Buku;
import Perpuskaan.demo.repository.BukuRepository;

@Service
public class BukuServiceImpl implements BukuService {

    // Suntik (Inject) Repository yang sudah Anda buat
    @Autowired
    private BukuRepository bukuRepository;

    @Override
    public List<Buku> getAllBuku() {
        // Method findAll() sudah ada dari JpaRepository
        return bukuRepository.findAll();
    }

    @Override
    public Optional<Buku> getBukuById(Integer id) {
        // 1. TAMBAHKAN PENGECEKAN INI
        if (id == null) {
            // Jika id-nya null, langsung kembalikan Optional kosong
            // Tidak perlu memanggil repository
            return Optional.empty();
        }

        // 2. Kode ini hanya akan jalan jika id TIDAK null
        return bukuRepository.findById(id);
    }

    @Override
    public List<Buku> getBukuByJudul(String judul) {
        // Memanggil method custom Anda
        return bukuRepository.findByJudul(judul);
    }

    @Override
    public List<Buku> getBukuByPengarang(String namaPengarang) {
        // Memanggil method custom Anda
        return bukuRepository.findByPengarang(namaPengarang);
    }

    @Override
    @SuppressWarnings("null")
    public Buku createBuku(Buku buku) {
        // Anda bisa tambahkan logika validasi di sini nanti
        return bukuRepository.save(buku);
    }

}
