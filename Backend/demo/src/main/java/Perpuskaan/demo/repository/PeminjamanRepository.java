package Perpuskaan.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import Perpuskaan.demo.entity.Peminjaman;

public interface PeminjamanRepository extends JpaRepository<Peminjaman, Integer> {
    @Query("""
        SELECT DISTINCT p FROM Peminjaman p
        JOIN FETCH p.pemustaka
        JOIN FETCH p.buku
    """)
    List<Peminjaman> findAllWithRelations();


    
    // Cari semua data, dimana Pemustaka -> IdUser = ?
    List<Peminjaman> findByPemustaka_IdUser(Integer idUser);
}
