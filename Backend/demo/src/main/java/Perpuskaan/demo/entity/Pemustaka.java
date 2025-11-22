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
@PrimaryKeyJoinColumn(name = "id_user")
public class Pemustaka extends User {
    
    // Tambahkan @Column agar Hibernate yakin nama kolomnya "status_keanggotaan"
    @Column(name = "status_keanggotaan") 
    private String statusKeanggotaan;
    
    @Column(
        name = "no_anggota",
        unique = true,
        nullable = false,
        updatable = false,
        insertable = false 
    )
    private Integer noAnggota;

    @OneToMany(mappedBy = "pemustaka")
    private Set<Peminjaman> riwayatPeminjaman;
}