package Perpuskaan.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;
import java.util.Set;

@Data
@Entity
@Table(name = "buku")
public class Buku {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idBuku;

    @Column(nullable = false)
    private String judul;

    private String isbn;
    private String kategori;
    private String pengarang;
    private String penerbit;

    @Temporal(TemporalType.DATE)
    private Date tahunTerbit;

    private String jenis;
    private String noRak;
    
    @Lob // Untuk teks panjang
    private String abstrak;

    private Integer jumlahStok;
    private Double hargaSewa;
    private String urlGambarSampul;

    @OneToMany(mappedBy = "buku")
    private Set<Peminjaman> peminjamanTerkait;
}