package Perpuskaan.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users") // Semua disimpan di satu tabel bernama 'users'
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // Strategi pewarisan
@DiscriminatorColumn(name = "role", discriminatorType = DiscriminatorType.STRING) // Kolom yang membedakan
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