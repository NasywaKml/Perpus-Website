package Perpuskaan.demo.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter; // Import
import lombok.NoArgsConstructor; // Import
import lombok.Setter; // Import


@Getter
@Setter
@NoArgsConstructor
@Entity
@DiscriminatorValue("PUSTAKAWAN") // Jika role='PEMUSTAKA'
public class Pustakawan extends User {

    private String nip;
    private String jabatan;
    
}
