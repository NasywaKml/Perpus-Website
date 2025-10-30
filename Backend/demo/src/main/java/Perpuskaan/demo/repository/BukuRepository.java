package Perpuskaan.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Perpuskaan.demo.entity.Buku;

@Repository
public interface BukuRepository extends JpaRepository<Buku, Integer> {
    List<Buku> findByJudul(String judul);
    List<Buku> findByPenulis(String namaPenulis);
}
