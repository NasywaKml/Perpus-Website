package Perpuskaan.demo.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Perpuskaan.demo.dto.response.BukuSearchResponseDto;
import Perpuskaan.demo.entity.Buku; // Penting buat database transaction
import Perpuskaan.demo.repository.BukuRepository; // Kalau mau pake object request

@Service
@Transactional // Menjaga konsistensi data database
public class BukuService {

    // 1. Inject Repository
    private final BukuRepository bukuRepository;

    public BukuService(BukuRepository bukuRepository) {
        this.bukuRepository = bukuRepository;
    }

    // ==========================================
    // FITUR 1: Search Buku (Judul ATAU Pengarang)
    // ==========================================
    public List<BukuSearchResponseDto> searchBuku(String keyword) {
        try {
            if (keyword == null || keyword.trim().isEmpty()) {
                return Collections.emptyList();
            }

            List<Buku> hasilSearch = bukuRepository
                    .findByJudulContainingIgnoreCaseOrPengarangContainingIgnoreCase(keyword, keyword);

            List<BukuSearchResponseDto> responseList = new ArrayList<>();
            for (Buku buku : hasilSearch) {
                responseList.add(convertToDto(buku));
            }

            return responseList;

        } catch (Exception e) {
            System.out.println("ERROR di searchBuku(): " + e.getClass().getSimpleName());
            e.printStackTrace();   // <<-- ini yang bakal kasih tau error aslinya
            throw e;
        }
    }


    // ==========================================
    // FITUR 2: Get All Buku (List Semua)
    // ==========================================
    public List<BukuSearchResponseDto> getAllBuku() {
        List<Buku> semuaBuku = bukuRepository.findAll();

        List<BukuSearchResponseDto> responseList = new ArrayList<>();
        for (Buku buku : semuaBuku) {
            responseList.add(convertToDto(buku));
        }

        return responseList;
    }

    // ==========================================
    // FITUR 3: Get Detail Buku by ID
    // ==========================================
    public BukuSearchResponseDto getBukuById(Integer id) {
        // Cari buku, kalau gak ada throw error
        Buku buku = bukuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Buku dengan ID " + id + " tidak ditemukan"));

        return convertToDto(buku);
    }

    // ==========================================
    // HELPER: Convert Entity -> DTO (Manual Setter)
    // ==========================================
    // Method ini dibuat private biar bisa dipanggil ulang oleh method search, get all, dll.
    private BukuSearchResponseDto convertToDto(Buku buku) {
        BukuSearchResponseDto dto = new BukuSearchResponseDto();

        dto.setJudul(buku.getJudul());
        dto.setIsbn(buku.getIsbn());
        dto.setPengarang(buku.getPengarang());
        dto.setTahunTerbit(buku.getTahunTerbit());
        dto.setNoRak(buku.getNoRak());
        dto.setAbstrak(buku.getAbstrak());
        dto.setJumlahStok(buku.getJumlahStok()); // Asumsi field ini ada di Entity Buku
        dto.setUrlGambarSampul(buku.getUrlGambarSampul());
        
        // Khusus untuk Kategori dan Jenis (Kalau di Entity pake relasi Object)
        // Pastikan entity Buku punya getter yang sesuai.
        // Contoh Logika Safety (Biar gak Null Pointer Exception):
        
        // if (buku.getKategori() != null) {
        //     dto.setKategori(buku.getKategori().getNamaKategori());
        // } else {
             dto.setKategori(buku.getKategori()); // Kalau di entity string biasa
        // }

        dto.setJenis(buku.getJenis());

        return dto;
    }
}