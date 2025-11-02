package Perpuskaan.demo.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)// super ke parent (user)
@Entity
@DiscriminatorValue("PEMUSTAKA") // Jika role='PEMUSTAKA'
public class Pemustaka extends User {

    private String noAnggota;
    private String statusKeanggotaan;

    @OneToMany(mappedBy = "pemustaka") // 'pemustaka' adalah nama field di Peminjaman.java
    private Set<Peminjaman> riwayatPeminjaman;
    
    
}