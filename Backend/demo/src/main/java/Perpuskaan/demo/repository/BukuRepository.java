package Perpuskaan.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Perpuskaan.demo.entity.Buku;

@Repository
public interface BukuRepository extends JpaRepository<Buku, Integer> {
    // Query otomatis:
    // SELECT * FROM buku WHERE UPPER(judul) LIKE UPPER(%key1%) OR UPPER(pengarang) LIKE UPPER(%key2%)
    List<Buku> findByJudulContainingIgnoreCaseOrPengarangContainingIgnoreCase(String key1, String key2);
    List<Buku> findByKategori(String kategori);

}
