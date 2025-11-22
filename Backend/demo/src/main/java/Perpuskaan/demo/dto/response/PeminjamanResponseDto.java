package Perpuskaan.demo.dto.response;

import java.util.Date;

import lombok.Data;

@Data
public class PeminjamanResponseDto {
    private Integer idPeminjaman;
    private String username;
    private String judulBuku;
    private Date tanggalPinjam;
    private Date tanggalKembali;
    private String status;
    private Double totalDenda;
}
