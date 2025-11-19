package Perpuskaan.demo.entity;

import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

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
    @JsonIgnore
    private Set<Peminjaman> peminjamanTerkait;


    
}