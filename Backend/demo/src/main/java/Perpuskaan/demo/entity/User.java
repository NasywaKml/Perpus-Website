package Perpuskaan.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn; // Import
import jakarta.persistence.Entity; // Import
import jakarta.persistence.GeneratedValue; // Import
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
// HAPUS @Data

@Getter // <-- Ganti
@Setter // <-- Ganti
@NoArgsConstructor // <-- Ganti
@Entity
@Table(name = "users") // Nama tabel Anda mungkin beda
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "role") // Ini nama kolom pembedanya
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUser;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    
    private String alamat;
    private String noTelpon;

    public String getPassword(){
        return password;
    }

    
}