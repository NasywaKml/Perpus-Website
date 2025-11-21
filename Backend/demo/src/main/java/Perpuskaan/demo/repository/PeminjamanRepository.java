package Perpuskaan.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import Perpuskaan.demo.entity.Peminjaman;

public interface PeminjamanRepository extends JpaRepository<Peminjaman, Integer> {
    
    // Cari semua data, dimana Pemustaka -> IdUser = ?
    List<Peminjaman> findByPemustaka_IdUser(Integer idUser);
}
