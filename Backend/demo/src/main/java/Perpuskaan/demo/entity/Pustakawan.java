package Perpuskaan.demo.entity;

// import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter; // Import
import lombok.NoArgsConstructor; // Import
import lombok.Setter; // Import


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "pustakawan") // <-- Tentukan nama tabel
@PrimaryKeyJoinColumn(name = "id_user") // Jika role='PEMUSTAKA'
public class Pustakawan extends User {

    private String nip;
    private String jabatan;
    
}
