package Perpuskaan.demo.dto;



import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;

@Data // <- Ini akan meng-generate Getter, Setter, toString(), equals(), hashCode()
public class RegisterRequest {

    @NotBlank(message = "Username tidak boleh kosong")
    @Size(min = 4, message = "Username minimal 4 karakter")
    private String username;

    @NotBlank(message = "Password tidak boleh kosong")
    @Size(min = 6, message = "Password minimal 6 karakter")
    @ToString.Exclude // <- Rekomendasi: Sembunyikan dari method toString()
    private String password;

    @NotBlank(message = "Email tidak boleh kosong")
    @Email(message = "Format email tidak valid")
    private String email;

    private String alamat;
    private String noTelpon;

    // Tidak perlu kode getter/setter manual di sini
}
