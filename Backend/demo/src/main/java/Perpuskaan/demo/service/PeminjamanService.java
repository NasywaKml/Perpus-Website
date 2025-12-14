package Perpuskaan.demo.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date; // Pakai java.util.Date sesuai entity kamu
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    
    // Konstanta batas peminjaman (7 Hari)
    private static final int BATAS_HARI_PINJAM = 7;

    // ================================
    //         PINJAM BUKU 
    // ================================
    @Transactional 
    public PeminjamanResponseDto pinjam(PeminjamanRequestDto req, String username) {

        // 1. Cari User
        Pemustaka pemustaka = pemustakaRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan (Token Invalid)"));

        // 2. Cari Buku
        Buku buku = bukuRepo.findById(req.getIdBuku())
                .orElseThrow(() -> new RuntimeException("Buku tidak ditemukan"));

        // 3. Cek Stok
        if (buku.getJumlahStok() == null || buku.getJumlahStok() < 1) {
            throw new RuntimeException("Stok buku habis, tidak bisa meminjam!");
        }

        // 4. Kurangi Stok
        buku.setJumlahStok(buku.getJumlahStok() - 1);
        bukuRepo.save(buku);

        // 5. Simpan Data Peminjaman
        Peminjaman p = new Peminjaman();
        p.setPemustaka(pemustaka);
        p.setBuku(buku);
        p.setTanggalPinjam(req.getTanggalPinjam()); // Ini tipe Date
        
        // [FIX] Konversi Date -> LocalDate untuk tambah hari -> Balikin ke Date
        LocalDate tglPinjamLocal = convertToLocalDate(req.getTanggalPinjam());
        LocalDate tglKembaliLocal = tglPinjamLocal.plusDays(BATAS_HARI_PINJAM);
        p.setTanggalKembali(convertToDate(tglKembaliLocal));
        
        p.setStatus("DIPINJAM");
        p.setTotalDenda(0.0);

        peminjamanRepo.save(p);

        return mapToResponse(p);
    }

    // ================================
    //      KEMBALIKAN BUKU
    // ================================
    @Transactional
    public PeminjamanResponseDto kembalikan(Integer idPeminjaman, String username) {
        Peminjaman p = peminjamanRepo.findById(idPeminjaman)
                .orElseThrow(() -> new RuntimeException("Peminjaman tidak ditemukan"));

        if ("DIKEMBALIKAN".equalsIgnoreCase(p.getStatus())) {
            throw new RuntimeException("Buku ini sudah dikembalikan sebelumnya!");
        }

        // 1. HITUNG DENDA
        Double denda = hitungDendaOtomatis(p);
        p.setTotalDenda(denda);

        // 2. Update Status
        p.setStatus("DIKEMBALIKAN");

        // 3. Kembalikan Stok Buku (+1)
        Buku buku = p.getBuku();
        buku.setJumlahStok(buku.getJumlahStok() + 1);
        bukuRepo.save(buku);

        peminjamanRepo.save(p);
        
        return mapToResponse(p);
    }

    // ================================
    //   HELPER: HITUNG DENDA (FIXED)
    // ================================
    private Double hitungDendaOtomatis(Peminjaman p) {
        // [FIX] Ubah Date dari database ke LocalDate biar bisa dihitung selisihnya
        LocalDate tanggalPinjam = convertToLocalDate(p.getTanggalPinjam());
        LocalDate hariIni = LocalDate.now(); 
        
        long durasiPinjamAktual = ChronoUnit.DAYS.between(tanggalPinjam, hariIni);

        if (durasiPinjamAktual > BATAS_HARI_PINJAM) {
            long hariTerlambat = durasiPinjamAktual - BATAS_HARI_PINJAM;
            Double dendaPerHari = p.getBuku().getDendaPerHari();
            if (dendaPerHari == null) dendaPerHari = 0.0; 

            return hariTerlambat * dendaPerHari;
        } 
        return 0.0;
    }

    // ================================
    //   HELPER: KONVERSI TANGGAL
    // ================================
    // Method ini yang menyelamatkan error "plusDays undefined" kamu
    private LocalDate convertToLocalDate(Date dateToConvert) {
        if (dateToConvert == null) return LocalDate.now();
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    private Date convertToDate(LocalDate dateToConvert) {
        return Date.from(dateToConvert.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }

    // ================================
    //       GET ALL & HISTORY
    // ================================
    public List<PeminjamanResponseDto> getAll() {
        return peminjamanRepo.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public PeminjamanResponseDto getById(Integer id) {
        Peminjaman p = peminjamanRepo.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        return mapToResponse(p);
    }

    public List<PeminjamanResponseDto> getAllByUsername(String username) {
        Pemustaka user = pemustakaRepo.findByUsername(username).orElseThrow(() -> new RuntimeException("User error"));
        return peminjamanRepo.findByPemustaka_IdUser(user.getIdUser()).stream().map(this::mapToResponse).collect(Collectors.toList());
    }
    
    public List<PeminjamanResponseDto> getAllByUserId(Integer idUser) {
        return peminjamanRepo.findByPemustaka_IdUser(idUser).stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    // ================================
    //        MAPPING RESPONSE
    // ================================
    private PeminjamanResponseDto mapToResponse(Peminjaman p) {
        PeminjamanResponseDto res = new PeminjamanResponseDto();
        res.setIdPeminjaman(p.getIdPeminjaman());
        
        if (p.getPemustaka() != null) res.setUsername(p.getPemustaka().getUsername());
        else res.setUsername("User Tidak Ditemukan");

        if (p.getBuku() != null) res.setJudulBuku(p.getBuku().getJudul());
        else res.setJudulBuku("Buku Tidak Ditemukan");
        
        res.setTanggalPinjam(p.getTanggalPinjam());
        res.setTanggalKembali(p.getTanggalKembali());
        res.setStatus(p.getStatus());
        res.setTotalDenda(p.getTotalDenda());
        
        return res;
    }
}