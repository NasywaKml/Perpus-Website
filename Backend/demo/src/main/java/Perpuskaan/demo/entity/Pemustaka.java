package Perpuskaan.demo.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter; 
import lombok.NoArgsConstructor; 
import lombok.Setter; 

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@DiscriminatorValue("PEMUSTAKA") // Jika role='PEMUSTAKA'
public class Pemustaka extends User {

    private String noAnggota;
    private String statusKeanggotaan;

    // Relasi ini sudah benar
    @OneToMany(mappedBy = "pemustaka") // 'pemustaka' adalah nama field di Peminjaman.java
    private Set<Peminjaman> riwayatPeminjaman;
    
    
}