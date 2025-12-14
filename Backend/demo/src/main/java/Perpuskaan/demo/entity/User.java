package Perpuskaan.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.EnumType;   // <--- PENTING: Tambahkan ini
import jakarta.persistence.Enumerated;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED) // <-- Ini sudah benar
// @DiscriminatorColumn(name = "role") // <-- HAPUS BARIS INI
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
    // Tambahan: ROLE (Penting untuk Security)
    @Enumerated(EnumType.STRING) 
    @Column(nullable = false)
    private Role role;

   
}