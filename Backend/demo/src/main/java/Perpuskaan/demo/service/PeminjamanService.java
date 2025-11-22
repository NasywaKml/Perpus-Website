package Perpuskaan.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import Perpuskaan.demo.dto.request.PeminjamanRequestDto;
import Perpuskaan.demo.dto.response.PeminjamanResponseDto;
import Perpuskaan.demo.entity.Buku;
import Perpuskaan.demo.entity.Peminjaman;
import Perpuskaan.demo.entity.Pemustaka;
import Perpuskaan.demo.repository.BukuRepository;
import Perpuskaan.demo.repository.PeminjamanRepository;
import Perpuskaan.demo.repository.PemustakaRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PeminjamanService {

    private final PeminjamanRepository peminjamanRepo;
    private final PemustakaRepository pemustakaRepo;
    private final BukuRepository bukuRepo;

    // ================================
    //           PINJAM BUKU
    // ================================
    public PeminjamanResponseDto pinjam(PeminjamanRequestDto req) {

    Pemustaka pemustaka = pemustakaRepo.findById(req.getIdPemustaka())
                .orElseThrow(() -> new RuntimeException("Pemustaka tidak ditemukan"));

        Buku buku = bukuRepo.findById(req.getIdBuku())
                .orElseThrow(() -> new RuntimeException("Buku tidak ditemukan"));

        Peminjaman p = new Peminjaman();
        p.setPemustaka(pemustaka);
        p.setBuku(buku);
        p.setTanggalPinjam(req.getTanggalPinjam());
        p.setStatus("DIPINJAM");
        p.setTotalDenda(0.0);

        peminjamanRepo.save(p);

        return mapToResponse(p);
    }


    // ================================
    //        GET ALL PEMINJAMAN
    // ================================
    public List<PeminjamanResponseDto> getAll() {
        return peminjamanRepo.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ================================
    //         MAPPING RESPONSE
    // ================================
    private PeminjamanResponseDto mapToResponse(Peminjaman p) {
        PeminjamanResponseDto res = new PeminjamanResponseDto();
        res.setIdPeminjaman(p.getIdPeminjaman());
        
        // CEK NULL SAFETY - Agar tidak error 500 kalau data parent hilang
        if (p.getPemustaka() != null) {
            // Karena Pemustaka extends User, langsung panggil getUsername()
            res.setUsername(p.getPemustaka().getUsername());
        } else {
            res.setUsername("User Tidak Ditemukan");
        }

        if (p.getBuku() != null) {
            res.setJudulBuku(p.getBuku().getJudul());
        } else {
            res.setJudulBuku("Buku Tidak Ditemukan");
        }
        
        res.setTanggalPinjam(p.getTanggalPinjam());
        res.setTanggalKembali(p.getTanggalKembali());
        res.setStatus(p.getStatus());
        res.setTotalDenda(p.getTotalDenda());
        
        return res;
    }

    public PeminjamanResponseDto getById(Integer id) {
    Peminjaman p = peminjamanRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Data peminjaman tidak ditemukan"));
    
    return mapToResponse(p);
    }
    public List<PeminjamanResponseDto> getAllByUserId(Integer idUser) {
        // 1. Panggil repository yang baru kita buat
        List<Peminjaman> listPeminjaman = peminjamanRepo.findByPemustaka_IdUser(idUser);
        
        // 2. Ubah (Map) dari Entity ke DTO
        return listPeminjaman.stream()
                .map(this::mapToResponse) // Pakai method mapping yang sudah kamu punya
                .collect(Collectors.toList());
    }

}
