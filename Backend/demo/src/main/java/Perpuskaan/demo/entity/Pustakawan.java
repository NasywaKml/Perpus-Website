package Perpuskaan.demo.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true) // Penting untuk inheritance
@Entity
@DiscriminatorValue("PUSTAKAWAN") // Jika role='PUSTAKAWAN', ini adalah objek Pustakawan
public class Pustakawan extends User {

    private String nip;
    private String jabatan;
    
}
