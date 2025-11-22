package Perpuskaan.demo.dto.request;

import lombok.Data;
import java.util.Date;

@Data
public class PeminjamanRequestDto {
    private Integer idPemustaka;
    private Integer idBuku;
    private Date tanggalPinjam;
}
