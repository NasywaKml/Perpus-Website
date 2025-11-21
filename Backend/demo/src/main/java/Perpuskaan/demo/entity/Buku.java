package Perpuskaan.demo.entity;

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
import lombok.Data;

@Data
@Entity
@Table(name = "buku")
public class Buku {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_buku")
    private Integer idBuku;

    @Column(nullable = false, name = "judul")
    private String judul;

        @Column(name = "isbn")
    private String isbn;
    
    @Column(name = "kategori")
    private String kategori;
    
    @Column(name = "pengarang")
    private String pengarang;
    
    @Column(name = "penerbit")
    private String penerbit;

    @Column(name = "tahun_terbit")
    private Integer tahunTerbit;

    @Column(name = "jenis")
    private String jenis;
    
    @Column(name = "no_rak")
    private String noRak;
    
    @Lob // Untuk teks panjang
    @Column(name = "abstrak")
    private String abstrak;

    @Column(name = "jumlah_stok")
    private Integer jumlahStok;
    
    @Column(name = "harga_sewa")
    private Double hargaSewa;
    
    @Column(name = "denda_per_hari")
    private Double dendaPerHari;
    
    @Column(name = "status")
    private String status;
    
    @Column(name = "url_gambar_sampul")
    private String urlGambarSampul;

    @OneToMany(mappedBy = "buku")
    @JsonIgnore
    private Set<Peminjaman> peminjamanTerkait;


    
}
