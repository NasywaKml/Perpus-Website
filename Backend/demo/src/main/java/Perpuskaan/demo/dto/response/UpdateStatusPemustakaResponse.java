package Perpuskaan.demo.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateStatusPemustakaResponse {
    private Integer idUser;
    private String username;
    private String statusKeanggotaan;
}
