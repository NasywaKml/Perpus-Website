package Perpuskaan.demo.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Perpuskaan.demo.entity.Peminjaman;
import Perpuskaan.demo.entity.Pemustaka;

import java.util.List;

@Repository
public interface PeminjamanRepository extends JpaRepository<Peminjaman, Integer> {
    List<Peminjaman> findByPemustaka(Pemustaka pemustaka);
    List<Peminjaman> findByStatus(String status);
}
