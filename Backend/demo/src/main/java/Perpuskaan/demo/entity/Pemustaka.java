package Perpuskaan.demo.entity;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "pemustaka")
@PrimaryKeyJoinColumn(name = "id_user") // <-- Ini sudah benar
public class Pemustaka extends User {

    // private String noAnggota; // <-- HAPUS BARIS INI
    
    // Ini field untuk STATUS
    private String statusKeanggotaan;
    
    // Ini field untuk NOMOR ANGGOTA (yang AUTO_INCREMENT)
    @Column(
        name = "no_anggota",
        unique = true,
        nullable = false,
        updatable = false,
        insertable = false  // <-- TAMBAHKAN INI
    )
    private Integer noAnggota; // <-- Tipe data harus Integer

    @OneToMany(mappedBy = "pemustaka")
    private Set<Peminjaman> riwayatPeminjaman;
}