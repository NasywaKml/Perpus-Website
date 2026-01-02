package Perpuskaan.demo.dto.request;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
@Data
public class RegisterPustakawanRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String email;

    @NotBlank
    private String nip;

    @NotBlank
    private String jabatan;
    
}
