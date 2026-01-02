package Perpuskaan.demo.dto.response;
import lombok.Data;

@Data
public class PemustakaResponse {
    private Integer idUser;
    private String username;
    private String email;
    private String role;
    private Integer noAnggota;
    private String statusKeanggotaan;
}

