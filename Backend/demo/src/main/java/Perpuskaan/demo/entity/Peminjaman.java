package Perpuskaan.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.util.Date;

@Getter // Hanya generate getter
@Setter // Hanya generate setter
@NoArgsConstructor
@Entity
@Table(name = "peminjaman")
public class Peminjaman {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPeminjaman;

    // Ini adalah cara JPA menghubungkan relasi
    // Kita tidak pakai 'idPemustaka: Integer' tapi langsung objeknya
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pemustaka", nullable = false)
    private Pemustaka pemustaka;

    // Sama seperti di atas, kita pakai objek Buku
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_buku", nullable = false)
    private Buku buku;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date tanggalPinjam;

    @Temporal(TemporalType.DATE)
    private Date tanggalKembali;

    @Column(nullable = false)
    private String status; // Misal: "DIPINJAM", "KEMBALI", "TERLAMBAT"

    private Double totalDenda;
}
