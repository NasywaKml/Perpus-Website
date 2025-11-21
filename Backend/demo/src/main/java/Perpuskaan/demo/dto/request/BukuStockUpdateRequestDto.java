package Perpuskaan.demo.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BukuStockUpdateRequestDto {

    /**
     * Perubahan stok:
     *  - nilai positif  = menambah stok
     *  - nilai negatif  = mengurangi stok
     */
    @NotNull
    private Integer delta;

    // Opsional: keterangan alasan perubahan stok
    private String keterangan;
}