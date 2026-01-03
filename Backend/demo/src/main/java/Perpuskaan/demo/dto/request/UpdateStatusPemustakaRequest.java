package Perpuskaan.demo.dto.request;

import Perpuskaan.demo.entity.StatusKeanggotaan;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateStatusPemustakaRequest {
    private StatusKeanggotaan statusKeanggotaan;
}
