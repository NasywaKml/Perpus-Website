package Perpuskaan.demo.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Perpuskaan.demo.dto.response.BukuSearchResponseDto;
import Perpuskaan.demo.entity.Buku; // Penting buat database transaction
import Perpuskaan.demo.repository.BukuRepository; // Kalau mau pake object request
import Perpuskaan.demo.dto.request.BukuCreateRequestDto;
import Perpuskaan.demo.dto.request.BukuStockUpdateRequestDto;

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
    // FITUR 4: Get All Buku (by Kategori)
    // ==========================================
    public List<BukuSearchResponseDto> getAllBukuByKategori(String kategori) {
        List<Buku> bukuKategori = bukuRepository.findByKategori(kategori);

        List<BukuSearchResponseDto> responseList = new ArrayList<>();
        for (Buku buku : bukuKategori) {
            responseList.add(convertToDto(buku));
        }

        return responseList;
    }
    // ==========================================
    // FITUR 5: Create Buku
    // ==========================================
    public BukuSearchResponseDto createBuku(BukuCreateRequestDto request) {
        try {
            Buku bukuBaru = new Buku();
            
            
            // Set semua field dari request
            bukuBaru.setJudul(request.getJudul());
            bukuBaru.setIsbn(request.getIsbn());
            bukuBaru.setKategori(request.getKategori());
            bukuBaru.setPengarang(request.getPengarang());
            bukuBaru.setPenerbit(request.getPenerbit());
            bukuBaru.setTahunTerbit(request.getTahunTerbit());
            bukuBaru.setJenis(request.getJenis());
            bukuBaru.setNoRak(request.getNoRak());
            bukuBaru.setAbstrak(request.getAbstrak());
            bukuBaru.setJumlahStok(request.getJumlahStok());
            bukuBaru.setHargaSewa(request.getHargaSewa());
            bukuBaru.setDendaPerHari(request.getDendaPerHari());
            bukuBaru.setStatus(request.getStatus());
            bukuBaru.setUrlGambarSampul(request.getUrlGambarSampul());
            
            Buku saved = bukuRepository.save(bukuBaru);
            return convertToDto(saved);
        } catch (Exception e) {
            System.err.println("ERROR di createBuku(): " + e.getClass().getSimpleName());
            System.err.println("Error message: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Gagal membuat buku: " + e.getMessage(), e);
        }
    }

    // ==========================================
    // FITUR 6: Adjust Stok Buku
    // ==========================================
    public BukuSearchResponseDto adjustStok(Integer idBuku, int delta) {
        Buku buku = bukuRepository.findById(idBuku)
            .orElseThrow(() -> new RuntimeException("Buku dengan ID " + idBuku + " tidak ditemukan"));
        int stokBaru = buku.getJumlahStok() + delta;
        if (stokBaru < 0) {
            throw new IllegalArgumentException("Jumlah stok tidak boleh negatif");
        }
        buku.setJumlahStok(stokBaru);
        return convertToDto(buku); // entity is managed; no explicit save() needed but call if preferred
    }

    // ==========================================
    // HELPER: Convert Entity -> DTO (Manual Setter)
    // ==========================================
    // Method ini dibuat private biar bisa dipanggil ulang oleh method search, get all, dll.
    private BukuSearchResponseDto convertToDto(Buku buku) {
        BukuSearchResponseDto dto = new BukuSearchResponseDto();
        dto.setIdBuku(buku.getIdBuku());
        dto.setJudul(buku.getJudul());
        dto.setIsbn(buku.getIsbn());
        dto.setKategori(buku.getKategori());
        dto.setPengarang(buku.getPengarang());
        dto.setPenerbit(buku.getPenerbit());
        dto.setTahunTerbit(buku.getTahunTerbit());
        dto.setJenis(buku.getJenis());
        dto.setNoRak(buku.getNoRak());
        dto.setAbstrak(buku.getAbstrak());
        dto.setJumlahStok(buku.getJumlahStok());
        dto.setHargaSewa(buku.getHargaSewa());
        dto.setDendaPerHari(buku.getDendaPerHari());
        dto.setStatus(buku.getStatus());
        dto.setUrlGambarSampul(buku.getUrlGambarSampul());

        return dto;
    }
}
